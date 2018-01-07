package com.fri.rso.fririders.support.resource;

import com.fri.rso.fririders.support.config.ConfigProperties;
import com.fri.rso.fririders.support.entity.Jwt;
import com.fri.rso.fririders.support.entity.SupportTicket;
import com.fri.rso.fririders.support.entity.User;
import com.fri.rso.fririders.support.service.NotificationService;
import com.fri.rso.fririders.support.service.SupportService;
import com.fri.rso.fririders.support.service.UserService;
import com.fri.rso.fririders.support.util.Helpers;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.metrics.annotation.Metered;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@RequestScoped
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
@Path("support")
@Log
public class SupportResource {

    private static final Logger log = LogManager.getLogger(SupportResource.class.getName());

    @Inject
    private SupportService supportService;

    @Inject
    private NotificationService notificationService;

    @Inject
    private UserService userService;

    @Inject
    private ConfigProperties config;

    @GET
    @Metered(name = "get_support_tickets")
    public Response getSupportTickets() {
        return Response.ok(supportService.getSupportTickets()).build();
    }

    @GET
    @Path("{ticketId}")
    @Metered(name = "get_support_ticket_by_id")
    public Response getSupportTicket(@PathParam("ticketId") String ticketId) {
        SupportTicket ticket = supportService.getSupportTicketById(ticketId);
        if (ticket == null) {
            log.warn("Support ticket not found.");
            return Response.status(Response.Status.NOT_FOUND).entity(Helpers.buildErrorJson("Support ticket not found.")).build();
        }

        return Response.ok(ticket).build();
    }

    @GET
    @Path("{userId}/user")
    @Metered(name = "get_support_ticket_for_user")
    public Response getSupportTicketsForUser(@Context HttpServletRequest request, @PathParam("userId") String userId) {
        String authToken = request.getHeader("authToken");
        if (authToken == null || authToken.equals("")) {
            log.warn("Auth token missing");
            return Response.status(Response.Status.UNAUTHORIZED).entity(Helpers.buildErrorJson("Auth token missing.")).build();
        }

        Jwt jwt = new Jwt();
        jwt.setToken(authToken);
        User user = userService.findUserById(jwt, userId);
        if (user == null) {
            log.warn("User for support ticket not found.");
            return Response.status(Response.Status.NOT_FOUND).entity(Helpers.buildErrorJson("User for payment not found.")).build();
        }

        return Response.ok(supportService.getSupportTicketsForUser(userId)).build();
    }

    @POST
    @Metered(name = "create_support_ticket")
    public Response createSupportTicket(@Context HttpServletRequest request, SupportTicket supportTicket) {
        String authToken = request.getHeader("authToken");
        if (authToken == null || authToken.equals("")) {
            log.warn("Auth token missing");
            return Response.status(Response.Status.UNAUTHORIZED).entity(Helpers.buildErrorJson("Auth token missing.")).build();
        }

        Jwt jwt = new Jwt();
        jwt.setToken(authToken);
        User user = userService.findUserById(jwt, supportTicket.getUserId());
        if (user == null) {
            log.warn("User for support ticket not found.");
            return Response.status(Response.Status.NOT_FOUND).entity(Helpers.buildErrorJson("User for payment not found.")).build();
        }

        SupportTicket createdTicket = supportService.createSupportTicket(supportTicket);
        if (createdTicket == null) {
            log.warn("Failed to create support ticket.");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Helpers.buildErrorJson("Failed to create support ticket.")).build();
        }

        boolean mailToUser = notificationService.sendNotification(config.getAdminEmail(), "Support Ticket Submitted: " + supportTicket.getSubject(), supportTicket.getMessage());
        log.info("Mail to user: " + mailToUser);
        supportTicket.setMailSentToUser(mailToUser);

        boolean mailToAdmin = notificationService.sendNotification(config.getAdminEmail(), "New Support Ticket: " + supportTicket.getSubject(), supportTicket.getMessage());
        log.info("Mail to admin: " + mailToAdmin);
        supportTicket.setMailSentToAdmin(mailToAdmin);

        SupportTicket updated = supportService.updateSupportTicket(supportTicket);
        if (updated == null) {
            log.warn("Updating support ticket failed, id = " + supportTicket.getUuid());
        }

        return Response.ok(createdTicket).build();
    }

}
