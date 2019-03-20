/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.WebApplicationProject.db;

import com.WebApplicationProject.model.Event;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author gabri
 */
@Stateless
public class EventFacade extends AbstractFacade<Event> {

    @PersistenceContext(unitName = "com_WebApplicationProject_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public EventFacade() {
        super(Event.class);
    }    
    
    /**
     * Gets a list of events created by a specified user
     * 
     * @param userId The user's ID
     * @return The list of events of a specified user
     */
    public List<Event> getEventByOwner(Long userId) {
       List results = em.createNamedQuery("Event.findByOwner")
               .setParameter("ownerId", userId)
               .getResultList();

       return results;
   }
    
}
