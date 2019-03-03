/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.login;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

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
@WebServlet(name = "Login", urlPatterns = {"/Login"})
public class Login extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String user = request.getParameter("user"); //Fetches data from loginView.xhtml
        String pass = request.getParameter("pass");
        try{
            Class.forName("com.derby.jdbc.Driver");
            Connection c = DriverManager.getConnection("jdbc:derby://localhost:1527/scheduleDatabase\", \"root\", \"root");
            PreparedStatement ps = c.prepareStatement("select FIRSTNAME,PASSWORD from USERS where FIRSTNAME=? and PASSWORD=?");
            ps.setString(1, user); //Currently uses FIRSTNAME as User
            ps.setString(2, pass);
 
            ResultSet rs = ps.executeQuery();
 
            while (rs.next()) {
                HttpSession session =request.getSession();
                session.setAttribute("user", user);
                response.sendRedirect("success.html"); //Direction if login is successful (PLACEHOLDER)
		return;
            }
            response.sendRedirect("error.html");//Direction if login is NOT successful (PLACEHOLDER)
        }
        catch (ClassNotFoundException | SQLException ex) {
                Logger.getLogger(Login.class.getName()).log(Level.SEVERE, null, ex);
        }   
    }
}
