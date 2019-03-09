/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.WebApplicationProject.jsf.beans;
import com.WebApplicationProject.control.AuthController;
import com.WebApplicationProject.jsf.beans.SessionUtil;
import java.io.Serializable;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
/**
 *
 * @author niklascote
 */


@ManagedBean
@SessionScoped
public class Login implements Serializable {

	private static final long serialVersionUID = 1094801825228386363L;
	
	private String pass;
	private String msg;
	private String email;

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

//	//Validate login
	public String validateUsernamePassword() {
		boolean valid = AuthController.validate(email, pass);
		if (valid) {
			HttpSession session = SessionUtil.getSession();
			session.setAttribute("email", email);
			return "schedule/scheduleView";
		} else {
			FacesContext.getCurrentInstance().addMessage(
					null,
					new FacesMessage(FacesMessage.SEVERITY_WARN,
							"Incorrect Email and Password",
							"Please enter correct Email and Password"));
			return "users/loginView";
		}
	}

	//Logout event, invalidate session
	public String logout() {
		HttpSession session = SessionUtil.getSession();
		session.invalidate();
		return "users/loginView";
	}
}
