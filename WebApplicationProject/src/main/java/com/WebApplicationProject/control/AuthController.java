/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.WebApplicationProject.control;

import com.WebApplicationProject.model.SessionUtil;
import com.WebApplicationProject.model.Users;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;
import java.io.IOException;
import java.io.Serializable;
import javax.enterprise.context.RequestScoped;


/**
 *
 * @author niklascote
 */
@Named("authController")
@RequestScoped
public class AuthController extends HttpServlet implements Serializable{
    
    @Getter
    @Setter
    private String email;
    
    @Getter
    @Setter
    private String pass;
    
    private boolean loggedIn = false;

    @EJB
    private com.WebApplicationProject.db.UsersFacade ufacade;

    public String login() throws IOException {
        FacesContext context = FacesContext.getCurrentInstance();
        if (validate(email, pass)) {
            loggedIn = true;
            HttpSession session = SessionUtil.getSession();
            session.setAttribute("email", email);
            return "/schedule/scheduleView";
        } else {
            context.addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Incorrect Email and/or Password",
                            "Please enter correct Email and Password"));
            return "/index.xhtml";
        }
    }

    private boolean validate(String email, String pass) {
        Users user = ufacade.users(email);

        return !(user == null
                || (user.getPassword() == null ? pass != null : !user.getPassword().equals(pass)));
    }
    
    public boolean getLogin(){
        return loggedIn;
    }

    //Logout event, invalidate session
    public String logout() {
        HttpSession session = SessionUtil.getSession();
        session.invalidate();
        return "/index.xhtml";
    }

}
