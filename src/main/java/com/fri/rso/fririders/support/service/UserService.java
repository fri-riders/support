package com.fri.rso.fririders.support.service;

import com.fri.rso.fririders.support.entity.Jwt;
import com.fri.rso.fririders.support.entity.User;
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
import javax.ws.rs.core.MediaType;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

@RequestScoped
@Log
@Bulkhead
@GroupKey("support")
public class UserService {

    private static final Logger log = LogManager.getLogger(AuthService.class.getName());

    private Client http = ClientBuilder.newClient();

    @Inject
    @DiscoverService(value = "users", version = "*", environment = "dev", accessType = AccessType.DIRECT)
    private Optional<String> usersUrl;

    @CircuitBreaker
    @Fallback(fallbackMethod = "findUserByIdFallback")
    @CommandKey("http-user-find")
    @Timeout(value = 5, unit = ChronoUnit.SECONDS)
    @Asynchronous
    public User findUserById(Jwt jwt, String userId) {
        if (this.usersUrl.isPresent()) {
            log.info("Calling user service");

            return http.target(this.usersUrl.get() + "/v1/users/" + userId)
                    .request(MediaType.APPLICATION_JSON)
                    .header("authToken", jwt.getToken())
                    .get(User.class);
        } else {
            log.warn("User service URL not available");

            return null;
        }
    }

    public User findUserByIdFallback(Jwt jwt, String userId) {
        log.warn("User service URL not available (findUserByIdFallback invoked)");
        return null;
    }
}
