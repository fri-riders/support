package com.fri.rso.fririders.support.config;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import java.util.logging.Logger;

public class ConfigurationEventHandler {

    private static final Logger log = Logger.getLogger(ConfigurationEventHandler.class.getName());

    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {

        ConfigurationUtil.getInstance().subscribe("support-config.healthy", (String key, String value) -> {
            if ("support-config.healthy".equals(key)) {
                if ("true".equals(value.toLowerCase())) {
                    log.info("Support service IS healthy.");
                } else {
                    log.info("Support service is NOT healthy.");
                }
            }
        });
    }

}
