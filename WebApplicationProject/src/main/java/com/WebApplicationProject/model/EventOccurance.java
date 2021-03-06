/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.WebApplicationProject.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gabri
 */
@Entity
@Table(name = "EVENT_OCCURANCE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EventOccurance.findAll", query = "SELECT e FROM EventOccurance e")
    , @NamedQuery(name = "EventOccurance.findById", query = "SELECT e FROM EventOccurance e WHERE e.id = :id")
    , @NamedQuery(name = "EventOccurance.findByStartDate", query = "SELECT e FROM EventOccurance e WHERE e.startDate = :startDate")
    , @NamedQuery(name = "EventOccurance.findByEndDate", query = "SELECT e FROM EventOccurance e WHERE e.endDate = :endDate")
    , @NamedQuery(name = "EventOccurance.findByEvent", query = "SELECT e FROM EventOccurance e WHERE e.event.id = :eventId")
})
public class EventOccurance implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    
    @Basic(optional = false)
    @Column(name = "START_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date startDate;
    
    @Basic(optional = false)
    @Column(name = "END_DATE", nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date endDate;
        
    @JoinColumn(name = "EVENT", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private Event event;

    public EventOccurance() {
    }

    public EventOccurance(Event event, Date startDate, Date endDate) {
        this.event = event; 
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    public EventOccurance(Date startDate, Date endDate) {
        this.event = event; 
        this.startDate = startDate;
        this.endDate = endDate;
    }
    
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }
   
    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
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
        if (!(object instanceof EventOccurance)) {
            return false;
        }
        EventOccurance other = (EventOccurance) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.WebApplicationProject.model.EventOccurance[ id=" + id + " ]";
    }
    
}
