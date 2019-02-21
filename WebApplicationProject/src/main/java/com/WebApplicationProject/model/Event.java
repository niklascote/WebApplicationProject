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
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "EVENT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Event.findAll", query = "SELECT e FROM Event e")
    , @NamedQuery(name = "Event.findById", query = "SELECT e FROM Event e WHERE e.id = :id")
    , @NamedQuery(name = "Event.findByTitle", query = "SELECT e FROM Event e WHERE e.title = :title")
    , @NamedQuery(name = "Event.findByLocation", query = "SELECT e FROM Event e WHERE e.location = :location")
    , @NamedQuery(name = "Event.findByNotification", query = "SELECT e FROM Event e WHERE e.notification = :notification")
    , @NamedQuery(name = "Event.findByDescription", query = "SELECT e FROM Event e WHERE e.description = :description")})
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "TITLE")
    private String title;
    @Size(max = 250)
    @Column(name = "LOCATION")
    private String location;
    @Column(name = "NOTIFICATION")
    private Integer notification;
    @Size(max = 250)
    @Column(name = "DESCRIPTION")
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
    private Collection<EventParticipant> eventParticipantCollection;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
    private Collection<EventOccurance> eventOccuranceCollection;
    @JoinColumn(name = "ACCESS", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Access access;
    @JoinColumn(name = "CALENDAR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Calendar calendar;
    @JoinColumn(name = "OWNER", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Users owner;

    public Event() {
    }

    public Event(Long id) {
        this.id = id;
    }

    public Event(Long id, String title) {
        this.id = id;
        this.title = title;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Integer getNotification() {
        return notification;
    }

    public void setNotification(Integer notification) {
        this.notification = notification;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @XmlTransient
    public Collection<EventParticipant> getEventParticipantCollection() {
        return eventParticipantCollection;
    }

    public void setEventParticipantCollection(Collection<EventParticipant> eventParticipantCollection) {
        this.eventParticipantCollection = eventParticipantCollection;
    }

    @XmlTransient
    public Collection<EventOccurance> getEventOccuranceCollection() {
        return eventOccuranceCollection;
    }

    public void setEventOccuranceCollection(Collection<EventOccurance> eventOccuranceCollection) {
        this.eventOccuranceCollection = eventOccuranceCollection;
    }

    public Access getAccess() {
        return access;
    }

    public void setAccess(Access access) {
        this.access = access;
    }

    public Calendar getCalendar() {
        return calendar;
    }

    public void setCalendar(Calendar calendar) {
        this.calendar = calendar;
    }

    public Users getOwner() {
        return owner;
    }

    public void setOwner(Users owner) {
        this.owner = owner;
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
        if (!(object instanceof Event)) {
            return false;
        }
        Event other = (Event) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.WebApplicationProject.model.Event[ id=" + id + " ]";
    }
    
}
