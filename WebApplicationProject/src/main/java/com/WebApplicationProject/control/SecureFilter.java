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
     HttpSession session = request.getSession();
     
     String loginURI = request.getContextPath()+"/index.xhtml";
     
     
     boolean loggedIn = (session != null && session.getAttribute("email") != null) ;
     //boolean loginRequest = request.getRequestURI().equals(loginURI);
     //boolean resourceRequest = request.getRequestURI().startsWith(request.getContextPath() + ResourceHandler.RESOURCE_IDENTIFIER);

    if (!loggedIn) {
        if(loginURI.indexOf("/scheduleView.xhtml")>=0 || loginURI.indexOf("/eventView.xhtml")>=0){
            response.sendRedirect(request.getServletContext().getContextPath() + "/index.xhtml");
        } else
            chain.doFilter(req, resp);
        } 
    }  
 
    @Override
    public void destroy() {}

    @Override
    public void init(FilterConfig arg) throws ServletException {
        //this.fc = arg;
    }  
 
}