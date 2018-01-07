package com.fri.rso.fririders.support.service;

import com.fri.rso.fririders.support.util.Helpers;
import com.kumuluz.ee.fault.tolerance.annotations.CommandKey;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.faulttolerance.Asynchronous;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Timeout;

import javax.enterprise.context.RequestScoped;
import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.net.URLEncoder;
import java.time.temporal.ChronoUnit;

@RequestScoped
@Log
public class NotificationService {

    private static final Logger log = LogManager.getLogger(NotificationService.class.getName());

    private Client http = ClientBuilder.newClient();

    private String notificationsUrl = "http://notifications:8083";

    @CircuitBreaker
    @Fallback(fallbackMethod = "sendNotificationFallback")
    @CommandKey("http-notifications-send")
    @Timeout(value = 5, unit = ChronoUnit.SECONDS)
    @Asynchronous
    public boolean sendNotification(String recipient, String subject, String message) {
        try {
            String url = notificationsUrl + "/v1/notifications/mail";
            url += "?recipient=" + URLEncoder.encode(recipient, "UTF-8");
            url += "&subject=" + URLEncoder.encode(subject, "UTF-8");
            url += "&message=" + URLEncoder.encode(message, "UTF-8");

            Response response = http
                    .target(url)
                    .request(MediaType.APPLICATION_JSON)
                    .post(null);

            System.out.println("response = " + response.getStatus());

            return response.getStatus() < 400;
        } catch (Exception e) {
            log.error(e.getMessage());

            return false;
        }
    }

    public boolean sendNotificationFallback(String recipient, String subject, String message) {
        log.warn("sendNotificationFallback invoked");

        return false;
    }

}
