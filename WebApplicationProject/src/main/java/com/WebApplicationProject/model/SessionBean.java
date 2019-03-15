/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.WebApplicationProject.model;

import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;

/**
 *
 * @author niklascote
 */
@Named(value = "sessionBean")
@SessionScoped
public class SessionBean implements Serializable {

    
    private String email;
    private String password;
    private boolean LoggedIn = false;
    /**
     * Creates a new instance of SessionBean
     */
    public SessionBean() {
    }
    
}
