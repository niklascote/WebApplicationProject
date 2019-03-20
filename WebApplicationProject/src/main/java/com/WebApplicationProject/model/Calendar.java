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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author gabri
 */
@Entity
@Table(name = "CALENDAR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Calendar.findAll", query = "SELECT c FROM Calendar c")
    , @NamedQuery(name = "Calendar.findById", query = "SELECT c FROM Calendar c WHERE c.id = :id")
    , @NamedQuery(name = "Calendar.findByName", query = "SELECT c FROM Calendar c WHERE c.name = :name")
    , @NamedQuery(name = "Calendar.findByDescription", query = "SELECT c FROM Calendar c WHERE c.description = :description")
    , @NamedQuery(name = "Calendar.findByPublicAccess", query = "SELECT c FROM Calendar c WHERE c.publicAccess = :publicAccess")})
public class Calendar implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    
    @Basic(optional = false)
    @Column(name = "NAME", nullable = false, length = 250)
    private String name;
    
    @Column(name = "DESCRIPTION", length = 250)
    private String description;
    
    @Basic(optional = false)
    @Column(name = "PUBLIC_ACCESS", nullable = false)
    private Boolean publicAccess = false;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "calendar")
    private Collection<CalendarParticipant> calendarParticipantCollection;
    
    @JoinColumn(name = "OWNER", referencedColumnName = "ID", nullable = false)
    @ManyToOne
    private Users owner;
    
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "calendar")
    private Collection<Event> eventCollection;

    public Calendar() {
        this.name = "Empty";
        this.publicAccess = true;
    }

    public Calendar(Long id) {
        this.id = id;
    }

    public Calendar(Long id, String name, Boolean publicAccess) {
        this.id = id;
        this.name = name;
        this.publicAccess = publicAccess;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Boolean getPublicAccess() {
        return publicAccess;
    }

    public void setPublicAccess(Boolean publicAccess) {
        this.publicAccess = publicAccess;
    }

    @XmlTransient
    public Collection<CalendarParticipant> getCalendarParticipantCollection() {
        return calendarParticipantCollection;
    }

    public void setCalendarParticipantCollection(Collection<CalendarParticipant> calendarParticipantCollection) {
        this.calendarParticipantCollection = calendarParticipantCollection;
    }

    public Users getOwner() {
        return owner;
    }

    public void setOwner(Users owner) {
        this.owner = owner;
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
        if (!(object instanceof Calendar)) {
            return false;
        }
        Calendar other = (Calendar) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.WebApplicationProject.model.Calendar[ id=" + id + " ]";
    }
    
}
