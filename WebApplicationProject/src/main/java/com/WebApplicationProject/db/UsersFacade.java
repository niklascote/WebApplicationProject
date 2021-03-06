/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.WebApplicationProject.db;

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
public class UsersFacade extends AbstractFacade<Users> {

    @PersistenceContext(unitName = "com_WebApplicationProject_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }

    public UsersFacade() {
        super(Users.class);
    }

    /**
     * Gets a user by his/her email(username)
     * 
     * @param email The searched for user's email
     * @return The user with a matching email
     */
    public Users users(String email){
        List<Users> users = em.createNamedQuery("Users.findByEmail")
                .setParameter("email", email).getResultList();
        return users.get(0);
    }
    
}
