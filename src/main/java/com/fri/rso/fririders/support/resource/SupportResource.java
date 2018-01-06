package com.fri.rso.fririders.support.resource;

import com.fri.rso.fririders.support.entity.SupportTicket;
import com.fri.rso.fririders.support.service.SupportService;
import com.fri.rso.fririders.support.util.Helpers;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.metrics.annotation.Metered;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
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
    public Response getSupportTicketsForUser(@PathParam("userId") String userId) {
        return Response.ok(supportService.getSupportTicketsForUser(userId)).build();
    }

    @POST
    @Metered(name = "create_support_ticket")
    public Response createSupportTicket(SupportTicket supportTicket) {
        SupportTicket createdTicket = supportService.createSupportTicket(supportTicket);
        if (createdTicket == null) {
            log.warn("Failed to create support ticket.");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(Helpers.buildErrorJson("Failed to create support ticket.")).build();
        }



        return Response.ok(createdTicket).build();
    }

}
