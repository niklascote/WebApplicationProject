package com.WebApplicationProject.control;

import com.WebApplicationProject.db.UsersFacade;
import com.WebApplicationProject.model.Users;
import com.WebApplicationProject.model.Calendar;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;

@Named("calendarController")
@ViewScoped
public class CalendarController implements Serializable {
    
    @EJB
    private UsersFacade usersFacade;

    @Getter
    @Setter
    private Users user = new Users(); 
    
    @Getter
    @Setter
    private List<Calendar> editableCalendars = new ArrayList<Calendar>();
    
    @Getter
    @Setter
    private List<Calendar> nonEditableCalendars = new ArrayList<Calendar>();
    
    @Getter
    @Setter
    private List<Calendar> selectedCalendars = new ArrayList<Calendar>();
    
    @PostConstruct
    public void init() {
        
        //TODO: Only for testing. Must be changed to a real user search. 
        user = usersFacade.find(1L);
        setCalendars();
        
    }
    
    public void setCalendars() {
        
        //All the user's created calendards
        user.getCalendarCollection().forEach((c) -> {
            editableCalendars.add(c);
        });
        
        //All the user's shared calendars
        user.getCalendarParticipantCollection().forEach((c) -> {
            if(c.getWritePermission()) {
                editableCalendars.add(c.getCalendar());
            }
            else {
                nonEditableCalendars.add(c.getCalendar());
            }
        });
    }   

}
