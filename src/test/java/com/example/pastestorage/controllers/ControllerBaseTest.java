package com.example.pastestorage.controllers;

import com.example.pastestorage.models.Paste;
import com.example.pastestorage.models.User;
import com.example.pastestorage.services.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hibernate.Session;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.BDDMockito.given;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public abstract class ControllerBaseTest {
    @MockBean
    protected SecurityContextHolder securityContextHolder;
    @MockBean
    protected Authentication authentication;
    @MockBean
    protected SecurityContext securityContext;
    @Autowired
    protected MockMvc mockMvc;

    protected static String asJsonString(final Object obj) {
        try {
            return new ObjectMapper().writeValueAsString(obj);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    protected <T> Page<T> getPageFromList(
            List<T> entityList,
            int page,
            int size,
            String orderBy,
            String orderDirection,
            Class<T> cls) {
        Pageable paging;
        if (orderBy == null && orderDirection == null) {
            paging = PageRequest.of(page, size);
        } else {
            paging = PageRequest.of(page, size, Sort.by(Sort.Direction.fromString(orderDirection), orderBy));
        }
        final int start = (int)paging.getOffset();
        final int end = Math.min((start + paging.getPageSize()), entityList.size());
        final Page<T> pagePaste = new PageImpl<>(entityList.subList(start, end), paging, entityList.size());
        return pagePaste;
    }

}
