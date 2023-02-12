package com.example.pastestorage.util;

import org.hibernate.Filter;
import org.hibernate.Session;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManager;
import java.time.Instant;

@Component
public class SessionUtil {
    private Session session;
    public SessionUtil(@Autowired EntityManager entityManager) {
        session = entityManager.unwrap(Session.class);
    }

    public void enableExpiredFilter() {
        Filter filter = session.enableFilter("expiredFilter");
        filter.setParameter("currentTime", Instant.now());
    }

    public void disableFilter(String filterName) {
        session.disableFilter(filterName);
    }
}
