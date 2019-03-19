/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.WebApplicationProject.control;

import com.WebApplicationProject.model.SessionUtil;
import com.WebApplicationProject.model.Users;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author erik1
 */
@Named("profileController")
@RequestScoped
public class ProfileController {
    @EJB
    private com.WebApplicationProject.db.UsersFacade ufacade;
    
    private HttpSession session;
    
    @Getter
    @Setter
    private Users user;
    
    @PostConstruct
    public void init() {
        session = SessionUtil.getSession();
        user = ufacade.users((String)session.getAttribute("email"));
    }
    
    public String showProfile() {
        return "/schedule/profileView.xhtml";
    }
}
