/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.WebApplicationProject.view;

import com.WebApplicationProject.db.CalendarFacade;
import com.WebApplicationProject.db.EventFacade;
import com.WebApplicationProject.db.EventOccuranceFacade;
import com.WebApplicationProject.db.EventParticipantFacade;
import com.WebApplicationProject.db.UsersFacade;
import com.WebApplicationProject.model.Calendar;
import com.WebApplicationProject.model.Event;
import com.WebApplicationProject.model.EventOccurance;
import com.WebApplicationProject.model.EventParticipant;
import com.WebApplicationProject.model.EventViewModel;
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
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author gabri
 */

@Named("calendarView")
@ViewScoped
public class CalendarView implements Serializable {    
    
    @Getter
    @Setter
    private ScheduleModel eventModel = new DefaultScheduleModel();
    
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
    
    @EJB
    private UsersFacade userFacade;
    
    @EJB
    private EventFacade eventFacade;
    
    @EJB 
    private EventOccuranceFacade eventOccuranceFacade;
    
    @EJB
    private EventParticipantFacade eventParticipantFacade;
    
    @EJB
    private CalendarFacade calenderFacade;
    
    @PostConstruct
    public void init() {

        //TODO: Only for testing. Must be changed to a real user search. 
        HttpSession session = SessionUtil.getSession();
        String email = (String) session.getAttribute("email");
        
        user = userFacade.users(email);

        //Get all events for the user
        getEvents();
        
        //Set user's calendars
        setCalendars();
    }
    
    public List<Calendar> getByUserAccess(){
        return calenderFacade.userAccess();
    }
    
    public void setCalendars() {
        
        //All the user's created calendards
        user.getCalendarCollection().forEach((c) -> {
            System.out.println("Adding editable calendar...");
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
    
    
    public void getEvents() {

        //Find all events created by the user
        List<Event> events = eventFacade.getEventByOwner(user.getId());

        for (Event e : events) {
            List<EventOccurance> occurances = eventOccuranceFacade.getEventOccurancesByEvent(e.getId());
            for (EventOccurance eo : occurances) {
                eventModel.addEvent(new EventViewModel(e, eo));
            }
        }

        //Find all events where the user is included
        List<EventParticipant> participantEvents = eventParticipantFacade.getEventParticipantByParticipant(user.getId());
        for (EventParticipant ep : participantEvents) {
            List<EventOccurance> occurances = eventOccuranceFacade.getEventOccurancesByEvent(ep.getEvent().getId());
            for (EventOccurance eo : occurances) {
                eventModel.addEvent(new EventViewModel(ep.getEvent(), eo));
            }
        }
    }
}
