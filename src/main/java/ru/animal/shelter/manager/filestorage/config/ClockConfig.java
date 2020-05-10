package ru.animal.shelter.manager.filestorage.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import java.time.Clock;
import java.time.ZoneId;

@Configuration
class ClockConfig {

    private static final Logger LOG = LoggerFactory.getLogger(ClockConfig.class);
    private final String timeZone;

    ClockConfig(@Value("${time.zone}") String timeZone) {
        this.timeZone = timeZone;
    }

    @Bean
    public Clock clock() {
        logInit(timeZone);
        return Clock.system(ZoneId.of(timeZone));
    }

    private void logInit(String clock) {
        LOG.info(String.format("Clock config: timeZone = %s", clock));
    }
}