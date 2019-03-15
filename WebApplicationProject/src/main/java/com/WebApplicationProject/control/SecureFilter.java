/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.WebApplicationProject.control;

/**
 *
 * @author niklascote
 */

import java.io.IOException;
 
import javax.faces.application.ResourceHandler;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;  
public class SecureFilter implements Filter{
 //private FilterConfig fc;
 
 public void doFilter(ServletRequest req, ServletResponse resp,  
     FilterChain chain) throws IOException, ServletException {  
           
     HttpServletRequest request = (HttpServletRequest) req;
     HttpServletResponse response = (HttpServletResponse) resp;
     SessionBean session = (SessionBean)request.getSession().getAttribute("bean");
     
     String loginURI = request.getRequestURI();
     
     
     boolean loggedIn = session != null && session. != false ;
     boolean loginRequest = request.getRequestURI().equals(loginURI);
     boolean resourceRequest = request.getRequestURI().startsWith(request.getContextPath() + ResourceHandler.RESOURCE_IDENTIFIER);

    if (loggedIn || loginRequest || resourceRequest) {
        chain.doFilter(request, response);
    } else {
        response.sendRedirect(request.getServletContext().getContextPath() + "/index.xhtml");
    }
    }  
 
    @Override
    public void destroy() {}

    @Override
    public void init(FilterConfig arg) throws ServletException {
        //this.fc = arg;
    }  
 
}