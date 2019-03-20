/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.WebApplicationProject.db;

import com.WebApplicationProject.model.EventOccurance;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author gabri
 */
@Stateless
public class EventOccuranceFacade extends AbstractFacade<EventOccurance> {

    @PersistenceContext(unitName = "com_WebApplicationProject_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EventOccuranceFacade() {
        super(EventOccurance.class);
    }
    
    /**
     * Gets a list of event occurrences by the repeated event's ID
     * 
     * @param eventId The ID of the event
     * @return The list of event occurrences of the event specified
     */
    public List<EventOccurance> getEventOccurancesByEvent(Long eventId) {
       List results = em.createNamedQuery("EventOccurance.findByEvent")
               .setParameter("eventId", eventId)
               .getResultList();

       return results;
   }
           
}
