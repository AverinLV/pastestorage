package com.example.pastestorage.services;

import com.example.pastestorage.models.Paste;
import com.example.pastestorage.models.User;
import com.example.pastestorage.repositories.PasteRepository;
import com.example.pastestorage.security.AuthorityUtil;
import com.example.pastestorage.types.AccessType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

public class PasteServiceTest extends ServiceBaseTest{
    @MockBean
    private PasteRepository pasteRepository;
    @Autowired
    private PasteService pasteService;
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
    }

    @Test
    @DisplayName("Test save")
    void testSave() {
        Paste paste = pasteList.get(0);
        given(pasteRepository.save(paste)).willReturn(paste);
        pasteService.save(paste);
        verify(pasteRepository, times(1)).save(paste);
    }

    @Test
    @DisplayName("Test get")
    void testGet() {
        Paste paste = pasteList.get(0);
        given(pasteRepository.findById(paste.getId())).willReturn(Optional.of(paste));
        Paste foundPaste = pasteService.get(paste.getId());
        verify(pasteRepository, times(1)).findById(paste.getId());
        assertEquals(paste.getId(), foundPaste.getId());
    }

    @Test
    @DisplayName("Test delete")
    void testDelete() {
        MockedStatic<AuthorityUtil> utilities = Mockito.mockStatic(AuthorityUtil.class);
        utilities.when(() -> AuthorityUtil.isAllowedAction(user.getUsername()))
                .thenReturn(true);

        Paste paste = pasteList.get(0);
        given(pasteRepository.findById(paste.getId())).willReturn(Optional.of(paste));
        doNothing().when(pasteRepository).delete(paste);

        Paste deletedPaste = pasteService.delete(paste.getId());
        verify(pasteRepository, times(1)).findById(paste.getId());
        verify(pasteRepository, times(1)).delete(paste);
        assertEquals(paste.getId(), deletedPaste.getId());
        utilities.close();
    }

    @Test
    @DisplayName("Test findAll")
    void testFindAll() {
        final int PAGE = 0;
        final int SIZE = 10;
        final String ORDER_BY = "createdAt";
        final String ORDER_DIR = "desc";
        final String MIN_START_DATE = "1970-01-01T00:00:00.00Z";
        final String MAX_START_DATE = "9999-12-31T00:00:00.00Z";
        final AccessType accessType = AccessType.PUBLIC;
        Pageable paging = PageRequest.of(PAGE, SIZE, Sort.by(Sort.Direction.fromString(ORDER_DIR), ORDER_BY));
        given(pasteRepository.findNotExpired(
                accessType,
                Instant.parse(MIN_START_DATE),
                Instant.parse(MAX_START_DATE),
                paging)).willReturn(getPageFromList(
                        pasteList,
                        PAGE,
                        SIZE,
                        ORDER_BY,
                        ORDER_DIR,
                        Paste.class));
        Page<Paste> pastePage = pasteService.getPublic(
                PAGE,
                SIZE,
                ORDER_BY,
                ORDER_DIR,
                Instant.parse(MIN_START_DATE),
                Instant.parse(MAX_START_DATE));
        verify(pasteRepository, times(1)).findNotExpired(
                accessType,
                Instant.parse(MIN_START_DATE),
                Instant.parse(MAX_START_DATE),
                paging);
        assertEquals(pastePage.getTotalElements(), pasteList.size());

    }

    @Test
    @DisplayName("Test deleteExpired")
    void testDeleteExpired() {
        doNothing().when(pasteRepository).deleteExpired(any(Instant.class));

        pasteService.deleteExpired();

        verify(pasteRepository, times(1)).deleteExpired(any(Instant.class));
    }


}
