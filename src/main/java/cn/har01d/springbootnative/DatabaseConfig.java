package cn.har01d.springbootnative;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.core.JdbcTemplate;

import com.zaxxer.hikari.HikariDataSource;

@Configuration
public class DatabaseConfig {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    @Value("${sqlite.db.file:application.db}")
    private String dbFile;

    @Value("${sqlite.pool.size:3}")
    private int poolSize;

    @Bean
    @Primary
    @ConfigurationProperties("spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean
    @Primary
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public DataSource sqlite3DataSource() {
        logger.info("Initializing SQLite database at: {}", dbFile);

        HikariDataSource dataSource = DataSourceBuilder.create()
                .type(HikariDataSource.class)
                .url("jdbc:sqlite:" + dbFile)
                .driverClassName("org.sqlite.JDBC")
                .build();

        // Connection pool configuration
        dataSource.setMaximumPoolSize(poolSize);
        dataSource.setConnectionTestQuery("SELECT 1");
        dataSource.setConnectionTimeout(30000); // 30 seconds
        dataSource.setIdleTimeout(600000); // 10 minutes
        dataSource.setMaxLifetime(1800000); // 30 minutes

        // Enable WAL mode for better concurrency
        try (Connection conn = dataSource.getConnection(); Statement stmt = conn.createStatement()) {
            stmt.execute("PRAGMA journal_mode=WAL");
            stmt.execute("PRAGMA synchronous=NORMAL");
            stmt.execute("PRAGMA foreign_keys=ON");
            logger.info("Configured SQLite WAL mode and foreign keys");
        } catch (SQLException e) {
            logger.error("Failed to configure SQLite database", e);
        }

        return dataSource;
    }

    @Bean
    public JdbcTemplate sqlite3JdbcTemplate(@Qualifier("sqlite3DataSource") DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }
}
