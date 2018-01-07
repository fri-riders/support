package com.fri.rso.fririders.support.service;

import com.fri.rso.fririders.support.entity.SupportTicket;
import com.kumuluz.ee.logs.LogManager;
import com.kumuluz.ee.logs.Logger;
import com.kumuluz.ee.logs.cdi.Log;
import org.eclipse.microprofile.metrics.annotation.Counted;

import javax.enterprise.context.RequestScoped;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import java.util.List;

@RequestScoped
@Log
public class SupportService {

    private static final Logger log = LogManager.getLogger(SupportService.class.getName());

    @PersistenceContext
    private EntityManager entityManager;

    public List<SupportTicket> getSupportTickets() {
        return entityManager.createNamedQuery("SupportTicket.findAll", SupportTicket.class).getResultList();
    }

    public SupportTicket getSupportTicketById(String id) {
        try {
            return entityManager.createNamedQuery("SupportTicket.findById", SupportTicket.class).setParameter("id", id).getSingleResult();
        } catch (NoResultException e) {
            log.error(e.getMessage());

            return null;
        }
    }

    public List<SupportTicket> getSupportTicketsForUser(String userId) {
        return entityManager.createNamedQuery("SupportTicket.findByUserId", SupportTicket.class).setParameter("id", userId).getResultList();
    }

    @Transactional
    @Counted(name = "create_support_ticket_counter")
    public SupportTicket createSupportTicket(SupportTicket supportTicket) {
        try {
            beginTransaction();
            supportTicket.setMailSentToUser(false);
            supportTicket.setMailSentToAdmin(false);
            entityManager.persist(supportTicket);
            commitTransaction();

            return supportTicket;
        } catch (Exception e) {
            rollbackTransaction();

            log.error(e.getMessage());

            return null;
        }
    }

    @Transactional
    @Counted(name = "update_support_ticket_counter")
    public SupportTicket updateSupportTicket(SupportTicket supportTicket) {
        try {
            beginTransaction();
            entityManager.merge(supportTicket);
            entityManager.flush();
            commitTransaction();

            return supportTicket;
        } catch (Exception e) {
            rollbackTransaction();

            log.error(e.getMessage());

            return null;
        }
    }

    private void beginTransaction() {
        if (!entityManager.getTransaction().isActive())
            entityManager.getTransaction().begin();
    }

    private void commitTransaction() {
        if (entityManager.getTransaction().isActive())
            entityManager.getTransaction().commit();
    }

    private void rollbackTransaction() {
        if (entityManager.getTransaction().isActive())
            entityManager.getTransaction().rollback();
    }
}
