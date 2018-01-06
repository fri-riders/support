package com.fri.rso.fririders.support.health;

import com.fri.rso.fririders.support.config.ConfigProperties;
import org.eclipse.microprofile.health.Health;
import org.eclipse.microprofile.health.HealthCheck;
import org.eclipse.microprofile.health.HealthCheckResponse;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@Health
@ApplicationScoped
public class SupportServiceHealthCheck implements HealthCheck {

    @Inject
    private ConfigProperties configProperties;

    @Override
    public HealthCheckResponse call() {
        if (configProperties.isHealthy()) {
            return HealthCheckResponse.named(SupportServiceHealthCheck.class.getSimpleName()).up().build();
        } else {
            return HealthCheckResponse.named(SupportServiceHealthCheck.class.getSimpleName()).down().build();
        }
    }

}
