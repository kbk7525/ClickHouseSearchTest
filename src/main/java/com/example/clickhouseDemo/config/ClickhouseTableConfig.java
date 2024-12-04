package com.example.clickhouseDemo.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class ClickhouseTableConfig implements ApplicationRunner {

    private final JdbcTemplate jdbcTemplate;
    public ClickhouseTableConfig(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void run(ApplicationArguments args) {
        String query = """
            create table if not exists weblog (
                ip String DEFAULT '',
                timestamp String DEFAULT '',
                method String DEFAULT '',
                url String DEFAULT '',
                protocol String DEFAULT '',
                status String DEFAULT '',
                size String DEFAULT '',
                ref String DEFAULT '',
                agent String DEFAULT ''
            ) engine = MergeTree()
            order by timestamp
            """;
        jdbcTemplate.execute(query);
    }
}
