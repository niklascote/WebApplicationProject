/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.WebApplicationProject.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
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
    , @NamedQuery(name = "Event.findByLocation", query = "SELECT e FROM Event e WHERE e.location = :location")
    , @NamedQuery(name = "Event.findByNotification", query = "SELECT e FROM Event e WHERE e.notification = :notification")
    , @NamedQuery(name = "Event.findByDescription", query = "SELECT e FROM Event e WHERE e.description = :description")
    , @NamedQuery(name = "Event.findByTitle", query = "SELECT e FROM Event e WHERE e.title = :title")})
public class Event implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    
    @Column(name = "LOCATION", length = 250)
    private String location;
    
    @Basic(optional = false)
    @Column(name = "NOTIFICATION")
    @Temporal(TemporalType.DATE)
    private Date notification;
    
    @Column(name = "DESCRIPTION", length = 250)
    private String description;
        
    @Column(name = "TITLE", length = 250, nullable = false)
    private String title;
    
    @OneToMany(mappedBy = "event")
    private Collection<EventParticipant> eventParticipantCollection;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "event")
    private Collection<EventOccurance> eventOccuranceCollection;
    
    @JoinColumn(name = "CALENDAR", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Calendar calendar;
    
    @JoinColumn(name = "OWNER", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Users owner;

    public Event() {
    }

    public Event(Long id) {
        this.id = id;
    }
    
    public Event(String title, Calendar cal, String location, Users owner, Date notification, String description) {
        this.title = title;
        this.calendar = cal; 
        this.location = location; 
        this.owner = owner;
        this.notification = notification; 
        this.description = description; 
    }
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public Date getNotification() {
        return notification;
    }

    public void setNotification(Date notification) {
        this.notification = notification;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
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
