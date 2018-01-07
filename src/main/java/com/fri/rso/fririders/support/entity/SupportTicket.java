package com.fri.rso.fririders.support.entity;

import org.eclipse.persistence.annotations.UuidGenerator;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "support_tickets")
@NamedQueries({
    @NamedQuery(name = "SupportTicket.findAll", query = "SELECT sp FROM SupportTicket sp"),
    @NamedQuery(name = "SupportTicket.findById", query = "SELECT sp FROM SupportTicket sp WHERE sp.uuid = :id"),
    @NamedQuery(name = "SupportTicket.findByUserId", query = "SELECT sp FROM SupportTicket sp WHERE sp.userId = :id"),
})
@UuidGenerator(name = "idGenerator")
public class SupportTicket implements Serializable {

    @Id
    @GeneratedValue(generator = "idGenerator")
    private String uuid;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "subject")
    private String subject;

    @Column(name = "msg")
    private String message;

    @Column(name = "is_mail_sent_to_user")
    private boolean isMailSentToUser;

    @Column(name = "is_mail_sent_to_admin")
    private boolean isMailSentToAdmin;

    @Column(name = "created_at", nullable = false, columnDefinition="TIMESTAMP DEFAULT CURRENT_TIMESTAMP")
    private Date createdAt;

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isMailSentToUser() {
        return isMailSentToUser;
    }

    public void setMailSentToUser(boolean mailSentToUser) {
        isMailSentToUser = mailSentToUser;
    }

    public boolean isMailSentToAdmin() {
        return isMailSentToAdmin;
    }

    public void setMailSentToAdmin(boolean mailSentToAdmin) {
        isMailSentToAdmin = mailSentToAdmin;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }
}
