/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.WebApplicationProject.control;

import com.WebApplicationProject.db.ReminderFacade;
import com.WebApplicationProject.model.Reminder;
import java.io.Serializable;
import java.util.List;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;

/**
 *
 * @author gabri
 */
@Named("reminderController")
@ViewScoped
public class ReminderController implements Serializable {
    
    @EJB
    private ReminderFacade reminderFacade;
    
    public List<Reminder> getReminders() {  
        //Returns all reminder types from database to be shown in interface
        List<Reminder> reminders = reminderFacade.findAll();
        return reminders;                 
    }
    
}
