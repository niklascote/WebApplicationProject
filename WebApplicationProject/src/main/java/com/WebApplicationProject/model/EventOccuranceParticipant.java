/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.WebApplicationProject.model;

import java.io.Serializable;
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
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gabri
 */
@Entity
@Table(name = "EVENT_OCCURANCE_PARTICIPANT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EventOccuranceParticipant.findAll", query = "SELECT e FROM EventOccuranceParticipant e")
    , @NamedQuery(name = "EventOccuranceParticipant.findById", query = "SELECT e FROM EventOccuranceParticipant e WHERE e.id = :id")
    , @NamedQuery(name = "EventOccuranceParticipant.findByWritePermission", query = "SELECT e FROM EventOccuranceParticipant e WHERE e.writePermission = :writePermission")})
public class EventOccuranceParticipant implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "WRITE_PERMISSION")
    private Boolean writePermission;
    @JoinColumn(name = "EVENT_OCCURANCE", referencedColumnName = "ID")
    @ManyToOne(optional = false)
    private EventOccurance eventOccurance;
    @JoinColumn(name = "PARTICIPANT", referencedColumnName = "ID")
    @ManyToOne
    private Users participant;

    public EventOccuranceParticipant() {
    }

    public EventOccuranceParticipant(Long id) {
        this.id = id;
    }

    public EventOccuranceParticipant(Long id, Boolean writePermission) {
        this.id = id;
        this.writePermission = writePermission;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean getWritePermission() {
        return writePermission;
    }

    public void setWritePermission(Boolean writePermission) {
        this.writePermission = writePermission;
    }

    public EventOccurance getEventOccurance() {
        return eventOccurance;
    }

    public void setEventOccurance(EventOccurance eventOccurance) {
        this.eventOccurance = eventOccurance;
    }

    public Users getParticipant() {
        return participant;
    }

    public void setParticipant(Users participant) {
        this.participant = participant;
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
        if (!(object instanceof EventOccuranceParticipant)) {
            return false;
        }
        EventOccuranceParticipant other = (EventOccuranceParticipant) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.WebApplicationProject.model.EventOccuranceParticipant[ id=" + id + " ]";
    }
    
}
