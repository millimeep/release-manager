package com.millimeep.releasemanager.config;

import com.millimeep.releasemanager.build.Build;
import com.millimeep.releasemanager.component.ReleaseComponent;
import com.millimeep.releasemanager.component.ReleaseComponentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class LoadDatabase {
    private static final Logger log = LoggerFactory.getLogger(LoadDatabase.class);

    @Bean
    CommandLineRunner initDatabase(ReleaseComponentRepository repository) {

        return args -> {
            ReleaseComponent config = new ReleaseComponent("Config", "https://github.com/millimeep/release-manager.git");
            config.addBuild(new Build("http://nexus/something", "1.23.4"));
            ReleaseComponent payments = new ReleaseComponent("Payments", "https://github.com/millimeep/release-manager.git");
            log.info("Preloading " + repository.save(config));
            log.info("Preloading " + repository.save(payments));
        };
    }
}
