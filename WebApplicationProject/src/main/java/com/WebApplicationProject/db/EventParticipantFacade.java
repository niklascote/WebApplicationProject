/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.WebApplicationProject.db;

import com.WebApplicationProject.model.Event;
import com.WebApplicationProject.model.EventParticipant;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author gabri
 */
@Stateless
public class EventParticipantFacade extends AbstractFacade<EventParticipant> {

    @PersistenceContext(unitName = "com_WebApplicationProject_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EventParticipantFacade() {
        super(EventParticipant.class);
    }
    
    public List<EventParticipant> getEventParticipantByParticipant(Long userId) {
       List results = em.createNamedQuery("EventParticipant.findByParticipant")
               .setParameter("participantId", userId)
               .getResultList();

       return results;
   }
}
