package com.example.pastestorage.services;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.*;

import java.util.List;

@SpringBootTest
public class ServiceBaseTest {
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
