package com.dave.dev.jakarta.app.util;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import java.util.HashMap;
import java.util.Map;

public final class JPAUtil {

    private static final EntityManagerFactory ENTITY_MANAGER_FACTORY =
            buildEntityManagerFactory();

    private JPAUtil() {
    }

    private static EntityManagerFactory buildEntityManagerFactory() {
        Map<String, Object> overrides = new HashMap<>();
        putIfPresent(overrides, "jakarta.persistence.jdbc.driver", "APP_DB_DRIVER");
        putIfPresent(overrides, "jakarta.persistence.jdbc.url", "APP_DB_URL");
        putIfPresent(overrides, "jakarta.persistence.jdbc.user", "APP_DB_USER");
        putIfPresent(overrides, "jakarta.persistence.jdbc.password", "APP_DB_PASSWORD");

        if (overrides.isEmpty()) {
            return Persistence.createEntityManagerFactory("autoPartsPU");
        }
        return Persistence.createEntityManagerFactory("autoPartsPU", overrides);
    }

    private static void putIfPresent(Map<String, Object> overrides, String persistenceKey, String envVar) {
        String value = System.getenv(envVar);
        if (value != null && !value.isBlank()) {
            overrides.put(persistenceKey, value);
        }
    }

    public static EntityManager getEntityManager() {
        return ENTITY_MANAGER_FACTORY.createEntityManager();
    }

    public static EntityManagerFactory getEntityManagerFactory() {
        return ENTITY_MANAGER_FACTORY;
    }

    public static void shutdown() {
        if (ENTITY_MANAGER_FACTORY.isOpen()) {
            ENTITY_MANAGER_FACTORY.close();
        }
    }
}
