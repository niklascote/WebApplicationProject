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
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author gabri
 */
@Entity
@Table(name = "EVENT_PARTICIPANT")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "EventParticipant.findAll", query = "SELECT e FROM EventParticipant e")
    , @NamedQuery(name = "EventParticipant.findById", query = "SELECT e FROM EventParticipant e WHERE e.id = :id")
    , @NamedQuery(name = "EventParticipant.findByWritePermission", query = "SELECT e FROM EventParticipant e WHERE e.writePermission = :writePermission")
    , @NamedQuery(name = "EventParticipant.findByParticipant", query = "SELECT e FROM EventParticipant e WHERE e.participant.id = :participantId")
    , @NamedQuery(name = "EventParticipant.deleteByEvent", query = "DELETE FROM EventParticipant e WHERE e.event.id = :eventId")
    , @NamedQuery(name = "EventParticipant.getByEvent", query = "SELECT e FROM EventParticipant e WHERE e.event.id = :eventId")

})
public class EventParticipant implements Serializable {

    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Long id;
    
    @Column(name = "WRITE_PERMISSION", nullable = false)
    private Boolean writePermission = false;
    
    @JoinColumn(name = "EVENT", referencedColumnName = "ID")
    @ManyToOne
    private Event event;
    
    @JoinColumn(name = "PARTICIPANT", referencedColumnName = "ID")
    @ManyToOne
    private Users participant;

    public EventParticipant() {
    }

    public EventParticipant(Long id) {
        this.id = id;
    }
    
    public EventParticipant(Users participant) {
        this.participant = participant;
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

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
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
        if (!(object instanceof EventParticipant)) {
            return false;
        }
        EventParticipant other = (EventParticipant) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.WebApplicationProject.model.EventParticipant[ id=" + id + " ]";
    }
    
}
