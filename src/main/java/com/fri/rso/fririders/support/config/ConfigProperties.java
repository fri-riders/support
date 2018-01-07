package com.fri.rso.fririders.support.config;

import com.kumuluz.ee.configuration.cdi.ConfigBundle;
import com.kumuluz.ee.configuration.cdi.ConfigValue;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@ConfigBundle("support-config")
public class ConfigProperties {

    @ConfigValue(watch = true)
    private String adminEmail;

    private boolean healthy;

    public String getAdminEmail() {
        return adminEmail;
    }

    public void setAdminEmail(String adminEmail) {
        this.adminEmail = adminEmail;
    }

    public boolean isHealthy() {
        return healthy;
    }

    public void setHealthy(boolean healthy) {
        this.healthy = healthy;
    }

    public String toJsonString() {
        return String.format(
                "{" +
                    "\"adminEmail\": \"%s\"," +
                    "\"healthy\": %b" +
                "}",
                this.getAdminEmail(),
                this.isHealthy()
        );
    }
}
