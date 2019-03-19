/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.WebApplicationProject.view;

import com.WebApplicationProject.db.EventParticipantFacade;
import com.WebApplicationProject.db.ReminderFacade;
import com.WebApplicationProject.db.UsersFacade;
import com.WebApplicationProject.model.EventParticipant;
import com.WebApplicationProject.model.EventViewModel;
import com.WebApplicationProject.model.Reminder;
import com.WebApplicationProject.model.SessionUtil;
import com.WebApplicationProject.model.Users;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.SelectEvent;

/**
 *
 * @author gabri
 */
@Named("eventView")
@SessionScoped
public class EventView implements Serializable {

    @Getter
    @Setter
    private EventViewModel event = new EventViewModel();
    
    @Getter
    @Setter
    private List<Users> users = new ArrayList<Users>();
    
    @EJB
    private ReminderFacade reminderFacade;
    
    @EJB
    private UsersFacade userFacade;
    
    @EJB
    private EventParticipantFacade eventParticipantFacade;
    
    @Getter
    @Setter
    private Users user = new Users();
        
    @PostConstruct
    public void init() {

        //Get user from session
        HttpSession session = SessionUtil.getSession();
        String email = (String) session.getAttribute("email");
        
        user = userFacade.users(email);
    }
    
    public List<Reminder> getReminders() {  
        //Returns all reminder types from database to be shown in interface
        List<Reminder> reminders = reminderFacade.findAll();
        return reminders;                 
    }
    
    public Boolean writePremission() {

        //If event has not been created yet the user is the owner and has write permissions
        if (event.getEvent().getId() == null) {
            return true;
        }

        //Check if user has write permission on event
        if (event.getEvent().getOwner().equals(user)) {
            return true;
        }

        List<EventParticipant> participants = eventParticipantFacade.getEventParticipantByParticipant(event.getEvent().getId());
        for (EventParticipant ep : participants) {
            if (ep.equals(user)) {
                return true;
            }
        }

        return false;
    }
     
    public void setSelectedEvent(EventViewModel selected) {
        event = selected;
    }

    public void onEventSelect(SelectEvent selectEvent) {
        event = (EventViewModel) selectEvent.getObject();
    }

    public void onDateSelect(SelectEvent selectEvent) {
        event = new EventViewModel("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
    }
}
