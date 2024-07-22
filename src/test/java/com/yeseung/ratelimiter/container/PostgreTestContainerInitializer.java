package com.yeseung.ratelimiter.container;

import org.testcontainers.containers.PostgreSQLContainer;

public class PostgreTestContainerInitializer {

    static final PostgreSQLContainer POSTGRE_SQL_CONTAINER;

    static {
        POSTGRE_SQL_CONTAINER = new PostgreSQLContainer("postgres");
        POSTGRE_SQL_CONTAINER.start();
    }
}