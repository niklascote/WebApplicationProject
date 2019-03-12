/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.WebApplicationProject.model;
import java.io.Serializable;
import javax.annotation.ManagedBean;
import javax.enterprise.context.Dependent;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author niklascote
 */
@Dependent
@ManagedBean
public class Auth implements Serializable {
	private static final long serialVersionUID = 1094801825228386363L;
	
        @Getter
        @Setter
	private String pass;
        
        @Getter
        @Setter
	private String email;

        public Auth() {}
}
