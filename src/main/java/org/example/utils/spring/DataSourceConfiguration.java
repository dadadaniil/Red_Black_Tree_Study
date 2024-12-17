package org.example.utils.spring;

import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Log4j2
@Configuration
public class DataSourceConfiguration {

    @Value("${spring.datasource.url}")
    private String url;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Bean
    public DataSource dataSource() throws SQLException {
        DataSource dataSource = DataSourceBuilder.create()
            .url(url)
            .username(username)
            .password(password)
            .build();

        try (Connection connection = dataSource.getConnection()) {
            if (!connection.isValid(2)) {
                throw new SQLException("Failed to validate database connection.");
            }
        }

        log.info("Application was run with password {}", password);
        return dataSource;
    }
}
