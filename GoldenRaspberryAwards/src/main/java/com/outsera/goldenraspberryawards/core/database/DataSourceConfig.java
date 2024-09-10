package com.outsera.goldenraspberryawards.core.database;

import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

//@Configuration
public class DataSourceConfig {

    @Bean
    public DataSource dataSource(DatabaseProperties databaseProperties) {
        final String url = String.format("jdbc:mysql://%s:%s/%s?serverTimezone=UTC&useSSL=false&createDatabaseIfNotExist=false",
                databaseProperties.getHost(),
                databaseProperties.getPort(),
                databaseProperties.getDbname());
        return DataSourceBuilder
            .create()
            .url(url)
            .username(databaseProperties.getUsername())
            .password(databaseProperties.getPassword())
            .build();
    }
}