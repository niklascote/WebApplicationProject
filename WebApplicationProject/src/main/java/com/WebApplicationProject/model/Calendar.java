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
@Table(name = "CALENDAR")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Calendar.findAll", query = "SELECT c FROM Calendar c")
    , @NamedQuery(name = "Calendar.findById", query = "SELECT c FROM Calendar c WHERE c.id = :id")
    , @NamedQuery(name = "Calendar.findByName", query = "SELECT c FROM Calendar c WHERE c.name = :name")
    , @NamedQuery(name = "Calendar.findByDescription", query = "SELECT c FROM Calendar c WHERE c.description = :description")})
public class Calendar implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "NAME")
    private String name;
    @Size(max = 250)
    @Column(name = "DESCRIPTION")
    private String description;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "calendar")
    private Collection<CalendarParticipant> calendarParticipantCollection;
    @JoinColumn(name = "ACCESS", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Access access;
    @JoinColumn(name = "OWNER", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Users owner;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "calendar")
    private Collection<Event> eventCollection;

    public Calendar() {
    }

    public Calendar(Long id) {
        this.id = id;
    }

    public Calendar(Long id, String name) {
        this.id = id;
        this.name = name;
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

    @XmlTransient
    public Collection<CalendarParticipant> getCalendarParticipantCollection() {
        return calendarParticipantCollection;
    }

    public void setCalendarParticipantCollection(Collection<CalendarParticipant> calendarParticipantCollection) {
        this.calendarParticipantCollection = calendarParticipantCollection;
    }

    public Access getAccess() {
        return access;
    }

    public void setAccess(Access access) {
        this.access = access;
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
