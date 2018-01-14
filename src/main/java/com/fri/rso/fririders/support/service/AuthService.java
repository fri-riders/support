package com.fri.rso.fririders.support.service;

import com.fri.rso.fririders.support.entity.Jwt;
import com.kumuluz.ee.discovery.annotations.DiscoverService;
import com.kumuluz.ee.discovery.enums.AccessType;
import com.kumuluz.ee.fault.tolerance.annotations.CommandKey;
import com.kumuluz.ee.fault.tolerance.annotations.GroupKey;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.faulttolerance.*;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@RequestScoped
@Log
@Bulkhead
@GroupKey("support")
public class AuthService {

    private static final Logger log = LogManager.getLogger(AuthService.class.getName());

    private Client http = ClientBuilder.newClient();

    @Inject
    @DiscoverService(value = "auth", version = "*", environment = "dev", accessType = AccessType.DIRECT)
    private Optional<String> authUrl;

    @CircuitBreaker
    @Fallback(fallbackMethod = "isTokenValidFallback")
    @CommandKey("http-auth-verify-jwt")
    @Timeout(value = 5, unit = ChronoUnit.SECONDS)
    @Asynchronous
    public boolean isTokenValid(Jwt jwt) {
        try {
            if (this.authUrl.isPresent()) {
                log.info("Call auth service: verify");
                log.info("URL: " + this.authUrl.get() + "/v1/auth/verify");

                Response response = http.target(this.authUrl.get() + "/v1/auth/verify")
                        .request(MediaType.APPLICATION_JSON)
                        .post(Entity.entity(jwt, MediaType.APPLICATION_JSON), Response.class);

                return response.getStatus() == 200;
            } else {
                log.warn("Auth service URL not available");

                return false;
            }
        } catch (Exception e) {
            log.error(e.getMessage());

            return false;
        }
    }

    public boolean isTokenValidFallback(Jwt jwt) {
        log.warn("Auth service URL not available (isTokenValidFallback invoked)");
        return false;
    }

}
