/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.WebApplicationProject.control;

import com.WebApplicationProject.db.EventOccuranceFacade;
import com.WebApplicationProject.db.EventFacade;
import com.WebApplicationProject.db.ReminderFacade;
import com.WebApplicationProject.db.UsersFacade;
import com.WebApplicationProject.model.Event;
import com.WebApplicationProject.model.EventOccurance;
import com.WebApplicationProject.model.Users;
import com.WebApplicationProject.view.EventViewer;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author gabri
 */
@Named("scheduleController") 
@SessionScoped  
public class ScheduleController implements Serializable {
    
    @EJB
    private UsersFacade usersFacade;
    
    @EJB
    private EventFacade eventFacade;
    
    @EJB
    private ReminderFacade reminderFacade;
    
    @EJB
    private EventOccuranceFacade eventOccuranceFacade;
           
    @Getter
    @Setter
    private ScheduleModel eventModel;
        
    @Getter
    @Setter
    private EventViewer event = new EventViewer();
    
    @Getter
    @Setter
    private List<com.WebApplicationProject.model.Calendar> editableCalendars = new ArrayList<com.WebApplicationProject.model.Calendar>();
    
    @Getter
    @Setter
    private List<com.WebApplicationProject.model.Calendar> nonEditableCalendars = new ArrayList<com.WebApplicationProject.model.Calendar>();
    
    @Getter
    @Setter
    private List<com.WebApplicationProject.model.Calendar> selectedCalendars = new ArrayList<com.WebApplicationProject.model.Calendar>();
        
    @Getter
    @Setter
    private Users user = new Users(); 
    
    
    @PostConstruct
    public void init() {
        
        eventModel = new DefaultScheduleModel();
        
        //TODO: Only for testing. Must be changed to a real user search. 
        user = usersFacade.find(1L);
        
        //Get all events for the user
        getEvents();
        
        //Get user's calendars
        getCalendars();
    }
    
    public void getCalendars() {
        
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
    
    
    public void getEvents() {
                
        //Find all events created by the user
        user.getEventCollection().forEach((e) -> {            
            e.getEventOccuranceCollection().forEach((eo) -> {
                eventModel.addEvent(new EventViewer(
                        e.getTitle(),
                        eo.getStartDate(),
                        eo.getEndDate(),
                        e.getLocation(), 
                        e.getCalendar(), 
                        e.getReminder(),
                        e.getDescription(),
                        eo.getId(), 
                        e.getId()
                ));
            });
        });
        
        //Find all events where the user is included
        user.getEventOccuranceParticipantCollection().forEach((e) -> {
            eventModel.addEvent(new EventViewer(
                    e.getEventOccurance().getEvent().getTitle(),
                    e.getEventOccurance().getStartDate(), 
                    e.getEventOccurance().getEndDate(),
                    e.getEventOccurance().getEvent().getLocation(),
                    e.getEventOccurance().getEvent().getCalendar(),
                    e.getEventOccurance().getEvent().getReminder(),
                    e.getEventOccurance().getEvent().getDescription(),
                    e.getEventOccurance().getId(),
                    e.getEventOccurance().getEvent().getId()
            ));
        });
    }
    
    public List<Reminder> getReminders() {  
        //Returns all reminder types from database to be shown in interface
        List<Reminder> reminders = reminderFacade.findAll();
        return reminders;                 
    }
    
    public Date getInitialDate() {
        //Todo: must be based on user, not server
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), Calendar.FEBRUARY, calendar.get(Calendar.DATE), 0, 0, 0);
         
        return calendar.getTime();
    }
    
    private Calendar today() {
        //Todo: must be based on user, not server
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
 
        return calendar;
    }
    
    public void onEventSelect(SelectEvent selectEvent) {
        event = (EventViewer) selectEvent.getObject();
    }
     
    public void onDateSelect(SelectEvent selectEvent) {
        event = new EventViewer("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
    }
     
    public void onEventMove(ScheduleEntryMoveEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
         
        addMessage(message);
    }
     
    public void onEventResize(ScheduleEntryResizeEvent event) {
        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
         
        addMessage(message);
    }
     
    private void addMessage(FacesMessage message) {
        FacesContext.getCurrentInstance().addMessage(null, message);
    }
    
    public void clearEvent() {
        //Clear temporary event
        event = new EventViewer();
    }
    
    public void deleteAllEvents() {
        //Find choosen event in database
        Event e = eventFacade.find(event.getEventId());
        
        //Remove event and occurances from database en event model
        e.getEventOccuranceCollection().forEach((eo) -> {
            eventModel.deleteEvent(eventModel.getEvent(eo.getId().toString()));
            eventOccuranceFacade.remove(eo);
        });        
        eventFacade.remove(e);
                
        //Clear temporary event
        clearEvent();
        PrimeFaces.current().ajax().update("form:schedule");
        
    }
        
    public void deleteThisEvent() {
        //Find choosen event in database
        Event e = eventFacade.find(event.getEventId());
        EventOccurance eo = eventOccuranceFacade.find(event.getEventOccuranceId());
        
        //Delete event and occurances from database
        eventOccuranceFacade.remove(eo);
        
        //Only remove event if it is there is only one occurance
        if(e.getEventOccuranceCollection().size() > 1) {
            eventFacade.remove(e);
        }        
        
        //Delete event from event model
        eventModel.deleteEvent(event);
        
        //Clear temporary event
        clearEvent();
        
    }
    
    public void updateEvent() {
        
        //Update event in event model
        eventModel.updateEvent(event);
        
        //Get event entity
        Event e = eventFacade.find(event.getEventId());
        
        //Update entity
        e.setCalendar(event.getCalendar());
        e.setDescription(event.getDescription());
        e.setLocation(event.getLocation());
        e.setReminder(event.getReminder());
        e.setTitle(event.getTitle());        
        eventFacade.edit(e);
        
        //TODO: Update event occurances
        
        clearEvent();
    }
    
    public void addEvent() {        
        
        //Add event to schedule model, for it to be able to be shown i schedule
        eventModel.addEvent(event);        
                
        //Add event to Event-table in DB        
        Event e = new Event(
                event.getTitle(), 
                event.getCalendar(), 
                event.getLocation(), 
                user, 
                event.getReminder(),
                event.getDescription());        
    
        eventFacade.create(e);
        
        //Todo: When repeating this must be change to multiple occurances
        //Add event occurance to EventOccurance-table in DB        
        eventOccuranceFacade.create(new EventOccurance(
                eventFacade.find(e.getId()), 
                event.getStartDate(), 
                event.getEndDate())
        );
        
        //Remove temporary event in view
        clearEvent();          
    }
}
