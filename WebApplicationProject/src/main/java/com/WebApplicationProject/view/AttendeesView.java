/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.WebApplicationProject.view;

import com.WebApplicationProject.db.EventParticipantFacade;
import com.WebApplicationProject.db.UsersFacade;
import com.WebApplicationProject.model.EventParticipant;
import com.WebApplicationProject.model.SessionUtil;
import com.WebApplicationProject.model.Users;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import javax.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author gabri
 */
@Named("attendeesView")
@ViewScoped
public class AttendeesView implements Serializable {    
    
    @Getter
    @Setter
    private List<Users> attendeesList = new ArrayList<Users>(); 
    
    @Getter
    private List<Users> selectedAttendees = new ArrayList<Users>();
    
    @Setter
    private List<EventParticipant> selectedEventAttendees = new ArrayList<EventParticipant>();
        
    private Boolean firstTime = true; 
    
    @Getter
    @Setter
    private Users user = new Users();
    
    @EJB
    private UsersFacade userFacade;
    
    @EJB
    private EventParticipantFacade eventParticipantFacade;
        
    @PostConstruct
    public void init() {

        //Get user from session
        HttpSession session = SessionUtil.getSession();
        String email = (String) session.getAttribute("email");
        user = userFacade.users(email);
    }
    
    public List<Users> completeAttendees(String query) {
        List<Users> allUsers = setAttendeesList();
        attendeesList = new ArrayList<>();
         
        for (int i = 0; i < allUsers.size(); i++) {
            Users skin = allUsers.get(i);
            
            if(skin.getFirstname().toLowerCase().contains(query) || skin.getLastname().toLowerCase().contains(query)) {
                attendeesList.add(skin);
            }
        }
         
        return attendeesList;
    }
    
    public List<Users> setAttendeesList() {
        List<Users> users = userFacade.findAll();
        users.remove(user);
        return users; 
    }
    
    public void setSelectedAttendees(List<Users> selectedAttendees){
                
        if(selectedAttendees == null) { return; }
        
        for(Users u : selectedAttendees) {  
            Boolean alreadyParticipant = false;
                        
            for(EventParticipant ep : selectedEventAttendees)  {
                if (ep.getParticipant().equals(u)) { 
                    alreadyParticipant = true;
                }
            }
        
            if(!alreadyParticipant){
                selectedEventAttendees.add(new EventParticipant(u));
            }
        }
        
        this.selectedAttendees = new ArrayList<Users>();
    }
    
    public void removeAttendee(EventParticipant participant) {
        this.selectedEventAttendees.remove(participant);
    }
    
    public List<EventParticipant> getSelectedEventAttendees() {
        return selectedEventAttendees;
    }
        
    public List<EventParticipant> getSelectedEventAttendees(Long eventId) {
        
        if(firstTime && eventId != null){
            this.selectedEventAttendees = eventParticipantFacade.getEventParticipantByEvent(eventId);
            firstTime = false;
        }
        
        return this.selectedEventAttendees;
    }
}
