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
        if (validate()) {
            HttpSession session = SessionUtil.getSession();
            session.setAttribute("email", tmp.getEmail());
            return "schedule/scheduleView";
        } else {
            FacesContext.getCurrentInstance().addMessage(
                    null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                            "Incorrect Email and Password",
                            "Please enter correct Email and Password"));
            return "";
        }
    }

    private boolean validate() {
        List<Users> users = ufacade.users(tmp.getEmail());

        return !(users == null
                || users.size() != 1 //Only one user with this email exist.
                || users.get(0) == null
                || (users.get(0).getPassword() == null ? tmp.getPass() != null : !users.get(0).getPassword().equals(tmp.getPass())));
    }

    //Logout event, invalidate session
    public String logout() {
        HttpSession session = SessionUtil.getSession();
        session.invalidate();
        return "index.xhtml";
    }

}
