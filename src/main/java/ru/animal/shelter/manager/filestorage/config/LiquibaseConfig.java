package ru.animal.shelter.manager.filestorage.config;

import liquibase.integration.spring.SpringLiquibase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class LiquibaseConfig {

    private final Logger LOG = LoggerFactory.getLogger(LiquibaseConfig.class);
    private final DataSource dataSource;
    private final String liquibaseChangeLogPath;
    private final String defaultSchema;

    public LiquibaseConfig(DataSource dataSource, @Value("${spring.liquibase.change-log}") String liquibaseChangeLogPath,
                           @Value("${spring.liquibase.default-schema}") String defaultSchema) {
        this.dataSource = dataSource;
        this.liquibaseChangeLogPath = liquibaseChangeLogPath;
        this.defaultSchema = defaultSchema;
        logPreInit();
    }

    @Bean
    public SpringLiquibase liquibase(){
        logInit();
        var liquibase = new SpringLiquibase();
        liquibase.setChangeLog(liquibaseChangeLogPath);
        liquibase.setDataSource(dataSource);
        liquibase.setDefaultSchema(defaultSchema);
        liquibase.setDropFirst(Boolean.FALSE);
        liquibase.setLiquibaseSchema(defaultSchema);
        return liquibase;
    }

    private void logPreInit() {
        LOG.info("LiquibaseConfig dataSource: [" + dataSource + "]");
        LOG.info("LiquibaseConfig liquibaseChangeLogPath: [" + liquibaseChangeLogPath + "]");
        LOG.info("LiquibaseConfig defaultSchema: [" + defaultSchema + "]");
    }

    private void logInit() {
        LOG.info("LiquibaseConfig initialized");
    }
}
