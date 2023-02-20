package com.example.pastestorage.controllers;

import com.example.pastestorage.dto.mapper.PasteMapper;
import com.example.pastestorage.dto.request.CreatePasteDTO;
import com.example.pastestorage.dto.request.EditPasteDTO;
import com.example.pastestorage.models.Paste;
import com.example.pastestorage.models.User;
import com.example.pastestorage.rest.controllers.PasteController;
import com.example.pastestorage.services.PasteService;
import com.example.pastestorage.services.UserService;
import com.example.pastestorage.types.AccessType;
import com.example.pastestorage.types.LifetimeType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.doNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class PasteControllerTest {
    @Autowired
    private PasteController pasteController;
    @Autowired
    private PasteMapper pasteMapper;
    @MockBean
    private PasteService pasteService;
    @MockBean
    private SecurityContextHolder securityContextHolder;
    @MockBean
    private Authentication authentication;
    @MockBean
    private SecurityContext securityContext;
    @MockBean
    private UserService userService;
    @Autowired
    private MockMvc mockMvc;
    private List<Paste> pasteList;
    private User user;


    @BeforeEach
    void setUp() {
        this.user = new User();
        user.setUsername("testUser");
        this.pasteList = new ArrayList<>();
        pasteList.add(new Paste(
                UUID.fromString("91c8f139-845e-428b-9237-a765d41b2a51"),
                "textData",
                Instant.now().plus(1, ChronoUnit.DAYS),
                Instant.now(), AccessType.PUBLIC, user)
        );
        pasteList.add(new Paste(
                UUID.fromString("7eb024bd-a3dd-48e2-b75d-0926177ce673"),
                "textData 2",
                Instant.now().plus(2, ChronoUnit.DAYS),
                Instant.now(), AccessType.PUBLIC, user)
        );
        given(authentication.getName()).willReturn(user.getUsername());
        given(securityContext.getAuthentication()).willReturn(authentication);
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    public void shouldFetchPaste() throws Exception {
        Paste paste = pasteList.get(0);
        given(this.pasteService.get(paste.getId())).willReturn(paste);
        this.mockMvc.perform(get("/pastes/{id}", paste.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(paste.getId().toString()));
    }

    @Test
    public void shouldFetchPagePastes() throws Exception {
        final int PAGE = 0;
        final int SIZE = 10;
        final String ORDER_BY = "createdAt";
        final String ORDER_DIR = "desc";
        final String MIN_START_DATE = "1970-01-01T00:00:00.00Z";
        final String MAX_START_DATE = "9999-12-31T00:00:00.00Z";
        given(pasteService.getPublic(
                PAGE,
                SIZE,
                ORDER_BY,
                ORDER_DIR,
                Instant.parse(MIN_START_DATE),
                Instant.parse(MAX_START_DATE)))
                .willReturn(getPageFromList(
                        this.pasteList,
                        PAGE,
                        SIZE,
                        ORDER_BY,
                        ORDER_DIR
                ));
        this.mockMvc.perform(get("/pastes")
                        .param("page", String.valueOf(PAGE))
                        .param("size", String.valueOf(SIZE))
                        .param("orderBy", ORDER_BY)
                        .param("orderDirection", ORDER_DIR)
                        .param("minStartDate", MIN_START_DATE)
                        .param("maxStartDate", MAX_START_DATE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalItems").value(this.pasteList.size()));
    }

    @Test
    public void shouldCreatePaste() throws Exception {
        CreatePasteDTO createPasteDTO = new CreatePasteDTO(
                "Test text",
                1,
                LifetimeType.DAYS,
                AccessType.PUBLIC);

        given(userService.get(user.getUsername())).willReturn(user);
        doNothing().when(pasteService).save(any(Paste.class));

        this.mockMvc.perform(post("/pastes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(asJsonString(createPasteDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.textData").value(createPasteDTO.getTextData()));
    }

    @Test
    public void shouldDeletePaste() throws Exception {
        Paste paste = this.pasteList.get(0);
        given(pasteService.delete(paste.getId())).willReturn(paste);
        this.mockMvc.perform(delete("/pastes/{id}", paste.getId().toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.textData").value(paste.getTextData()));
    }

    @Test
    public void shouldEditPaste() throws Exception {
        EditPasteDTO editPasteDTO = new EditPasteDTO();
        editPasteDTO.setTextData("Edited text data");
        Paste pasteToEdit = this.pasteList.get(0);
        given(pasteService.get(pasteToEdit.getId())).willReturn(pasteToEdit);
        given(userService.get(user.getUsername())).willReturn(user);
        doNothing().when(pasteService).save(any(Paste.class));

        this.mockMvc.perform(patch("/pastes/{id}", pasteToEdit.getId().toString())
                .contentType(MediaType.APPLICATION_JSON)
                .content(asJsonString(editPasteDTO)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.textData").value(pasteToEdit.getTextData()));
    }

    private static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Page<Paste> getPageFromList(List<Paste> pasteList,
                                        int page,
                                        int size,
                                        String orderBy,
                                        String orderDirection) {
        Pageable paging = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(orderDirection), orderBy));
        final int start = (int)paging.getOffset();
        final int end = Math.min((start + paging.getPageSize()), pasteList.size());
        final Page<Paste> pagePaste = new PageImpl<>(pasteList.subList(start, end), paging, pasteList.size());
        return pagePaste;
    }

}
