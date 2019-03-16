/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.WebApplicationProject.db;

import com.WebApplicationProject.db.AbstractFacade;
import com.WebApplicationProject.model.Calendar;
import com.WebApplicationProject.model.Users;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 *
 * @author gabri
 */
@Stateless
public class CalendarFacade extends AbstractFacade<Calendar> {

    @PersistenceContext(unitName = "com_WebApplicationProject_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public CalendarFacade() {
        super(Calendar.class);
    }
    
    public List<Calendar> userAccess(){
        boolean status = true;
        List<Calendar> calendars = em.createNamedQuery("Calendar.findByPublicAccess")
                .setParameter("publicAccess", status).getResultList();
        
        return calendars;
    }
    
}
