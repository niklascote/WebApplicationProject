/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.WebApplicationProject.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author gabri
 */
@Entity
@Table(name = "USERS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Users.findUser", query = "SELECT u FROM Users u WHERE u.email = :email AND u.password = :password")//Login Query
    , @NamedQuery(name = "Users.findAll", query = "SELECT u FROM Users u")
    , @NamedQuery(name = "Users.findById", query = "SELECT u FROM Users u WHERE u.id = :id")
    , @NamedQuery(name = "Users.findByFirstname", query = "SELECT u FROM Users u WHERE u.firstname = :firstname")
    , @NamedQuery(name = "Users.findByLastname", query = "SELECT u FROM Users u WHERE u.lastname = :lastname")
    , @NamedQuery(name = "Users.findByPhone", query = "SELECT u FROM Users u WHERE u.phone = :phone")
    , @NamedQuery(name = "Users.findByEmail", query = "SELECT u FROM Users u WHERE u.email = :email")
    , @NamedQuery(name = "Users.findByPassword", query = "SELECT u FROM Users u WHERE u.password = :password")
    , @NamedQuery(name = "Users.findByTimezone", query = "SELECT u FROM Users u WHERE u.timezone = :timezone")
    , @NamedQuery(name = "Users.findByNotifications", query = "SELECT u FROM Users u WHERE u.notifications = :notifications")})
public class Users implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    
    @Column(name = "FIRSTNAME", length = 250, nullable = false)
    private String firstname;
    
    @Column(name = "LASTNAME", length = 250, nullable = false) 
    private String lastname;

    @Column(name = "PHONE", length = 20)
    private String phone;

    @Basic(optional = false)
    @Column(unique = true, name = "EMAIL", nullable = false, length = 100)
    private String email;
    
    @Basic(optional = false)
    @Column(name = "PASSWORD", length = 250, nullable = false)
    private String password;
    
    @Basic(optional = false)
    @Column(name = "TIMEZONE", nullable = false, length = 10)
    private String timezone;
    
    @Basic(optional = false)
    @Column(name = "NOTIFICATIONS", nullable = false)
    private Boolean notifications = true;
    
    @OneToMany(mappedBy = "participant")
    private Collection<EventParticipant> eventParticipantCollection;
    
    @OneToMany(mappedBy = "participant")
    private Collection<EventOccuranceParticipant> eventOccuranceParticipantCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "participant")
    private Collection<CalendarParticipant> calendarParticipantCollection;
    
    @OneToMany(mappedBy = "owner")
    private Collection<Calendar> calendarCollection;
    
    @OneToMany(mappedBy = "owner")
    private Collection<Event> eventCollection;

    public Users() {
    }

    public Users(Long id) {
        this.id = id;
    }

    public Users(Long id, String email, String password, String timezone, Boolean notifications) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.timezone = timezone;
        this.notifications = notifications;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getTimezone() {
        return timezone;
    }

    public void setTimezone(String timezone) {
        this.timezone = timezone;
    }

    public Boolean getNotifications() {
        return notifications;
    }

    public void setNotifications(Boolean notifications) {
        this.notifications = notifications;
    }

    @XmlTransient
    public Collection<EventParticipant> getEventParticipantCollection() {
        return eventParticipantCollection;
    }

    public void setEventParticipantCollection(Collection<EventParticipant> eventParticipantCollection) {
        this.eventParticipantCollection = eventParticipantCollection;
    }

    @XmlTransient
    public Collection<EventOccuranceParticipant> getEventOccuranceParticipantCollection() {
        return eventOccuranceParticipantCollection;
    }

    public void setEventOccuranceParticipantCollection(Collection<EventOccuranceParticipant> eventOccuranceParticipantCollection) {
        this.eventOccuranceParticipantCollection = eventOccuranceParticipantCollection;
    }

    @XmlTransient
    public Collection<CalendarParticipant> getCalendarParticipantCollection() {
        return calendarParticipantCollection;
    }

    public void setCalendarParticipantCollection(Collection<CalendarParticipant> calendarParticipantCollection) {
        this.calendarParticipantCollection = calendarParticipantCollection;
    }

    @XmlTransient
    public Collection<Calendar> getCalendarCollection() {
        return calendarCollection;
    }

    public void setCalendarCollection(Collection<Calendar> calendarCollection) {
        this.calendarCollection = calendarCollection;
    }

    @XmlTransient
    public Collection<Event> getEventCollection() {
        return eventCollection;
    }

    public void setEventCollection(Collection<Event> eventCollection) {
        this.eventCollection = eventCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Users)) {
            return false;
        }
        Users other = (Users) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.WebApplicationProject.model.Users[ id=" + id + " ]";
    }
    
}
