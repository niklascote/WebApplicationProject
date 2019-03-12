/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.WebApplicationProject.control;

import com.WebApplicationProject.model.Auth;
import com.WebApplicationProject.model.SessionUtil;
import com.WebApplicationProject.model.Users;
import java.util.List;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author niklascote
 */
@Named("authController")
@SessionScoped
public class AuthController extends HttpServlet {

    @EJB
    private com.WebApplicationProject.db.UsersFacade ufacade;

    @Getter
    @Setter
    private Auth tmp = new Auth();

    public String login() {
        System.out.println("E1");
        if (validate()) {
            System.out.println("E3_A");
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Incorrect Email and Password",
                            "Please enter correct Email and Password"));
            return "users/loginView";
        } 
        else {
            System.out.println("E3_B");
            HttpSession session = SessionUtil.getSession();
            session.setAttribute("email", tmp.getEmail());
            return "schedule/scheduleView";
        }
    }
    
    private boolean validate() {
        System.out.println("E2");
        return (ufacade.users(tmp.getEmail(), tmp.getPass())).size() <= 0;
    }

    //Logout event, invalidate session
    public String logout() {
        HttpSession session = SessionUtil.getSession();
        session.invalidate();
        return "users/loginView";
    }

}
