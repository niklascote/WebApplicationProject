/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.WebApplicationProject.control;

import com.WebApplicationProject.model.SessionUtil;
import com.WebApplicationProject.model.Users;
import java.io.IOException;
import java.io.Serializable;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author erik1
 */
@Named("profileController")
@ViewScoped
public class ProfileController implements Serializable {

    @EJB
    private com.WebApplicationProject.db.UsersFacade ufacade;

    private HttpSession session;

    @Getter
    @Setter
    private Users user;

    @Getter
    @Setter
    private boolean edit;

    @PostConstruct
    public void init() {
        session = SessionUtil.getSession();
        user = ufacade.users((String) session.getAttribute("email"));
        edit = false;
    }

    public void editMode() {
        user = ufacade.users((String) session.getAttribute("email"));
        edit = true;
    }

    public String profile() {
        return "/schedule/profileView.xhtml";
    }

    public String calendar() {
        user = ufacade.users((String) session.getAttribute("email"));
        return "/schedule/scheduleView.xhtml";
    }

    public void save() {
        try {
            ufacade.edit(user);
            session.setAttribute("email", user.getEmail());
            user = ufacade.users((String) session.getAttribute("email"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        edit = false;
    }

    public void cancel() {
        user = ufacade.users((String) session.getAttribute("email"));
        edit = false;
    }
}
