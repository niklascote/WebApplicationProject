/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.WebApplicationProject.control;

import com.WebApplicationProject.db.UsersFacade;
import com.WebApplicationProject.model.Users;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ejb.EJB;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * @author niklascote
 */
@WebServlet(name = "Auth", urlPatterns = {"/Auth"})
public class AuthController extends HttpServlet {

    @EJB
    private com.WebApplicationProject.db.UsersFacade ufacade;

    public static boolean validate(String email, String pass){
        
        //Class.forName("com.derby.jdbc.Drivers");
        //Connection c = DriverManager.getConnection("jdbc:derby://localhost:1527/scheduleDatabase\", \"root\", \"root");
        //PreparedStatement ps = c.prepareStatement("select FIRSTNAME,PASSWORD from USERS where FIRSTNAME=? and PASSWORD=?");
        List user = ufacade.users(email,pass); //Passes arguments into query in UsersFacade
        //ps.setString(1, user); //Currently uses FIRSTNAME as User
        //ps.setString(2, pass);
        
        //ResultSet rs = ps.executeQuery();
        
        if(user.size()>0) {
            HttpSession session =request.getSession();
            session.setAttribute("user", email);
            response.sendRedirect("schedule/scheduleView.xhtml"); //Direction if login is successful (PLACEHOLDER)
        }else
            response.sendRedirect("schedule/loginView.xhtml");//Direction if login is NOT successful (PLACEHOLDER)
    }
}