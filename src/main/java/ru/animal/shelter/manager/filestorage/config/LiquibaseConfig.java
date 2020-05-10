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

    private static final Logger LOG = LoggerFactory.getLogger(LiquibaseConfig.class);
    private final DataSource dataSource;
    private final String liquibaseChangeLogPath;
    private final String defaultSchema;

    public LiquibaseConfig(DataSource dataSource, @Value("${spring.liquibase.change-log}") String liquibaseChangeLogPath,
                           @Value("${spring.liquibase.default-schema}") String defaultSchema) {
        this.dataSource = dataSource;
        this.liquibaseChangeLogPath = liquibaseChangeLogPath;
        this.defaultSchema = defaultSchema;
    }

    @Bean
    public SpringLiquibase liquibase(){
        var liquibase = new SpringLiquibase();
        liquibase.setChangeLog(liquibaseChangeLogPath);
        liquibase.setDataSource(dataSource);
        liquibase.setDefaultSchema(defaultSchema);
        liquibase.setDropFirst(Boolean.FALSE);
        liquibase.setLiquibaseSchema(defaultSchema);
        logPreInit(liquibase);
        return liquibase;
    }

    private void logPreInit(SpringLiquibase liquibase) {
        LOG.info(String.format("LiquibaseConfig initialized: [ dataSource = %s, changeLogPath = %s, defaultSchema = %s ]",
                liquibase.getDataSource(), liquibase.getChangeLog(), liquibase.getDefaultSchema()));
    }

}
