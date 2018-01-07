package com.fri.rso.fririders.support.config;

import com.kumuluz.ee.configuration.utils.ConfigurationUtil;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.context.Initialized;
import javax.enterprise.event.Observes;
import java.util.logging.Logger;

public class ConfigurationEventHandler {

    private static final Logger log = Logger.getLogger(ConfigurationEventHandler.class.getName());

    public void init(@Observes @Initialized(ApplicationScoped.class) Object init) {
        String adminEmailConfigProperty = "support-config.admin-email";

        ConfigurationUtil.getInstance().subscribe(adminEmailConfigProperty, (String key, String value) -> {
            if (adminEmailConfigProperty.equals(key)) {
                log.info("Admin email is now " + value);
            }
        });
    }

}
