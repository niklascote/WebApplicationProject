/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.WebApplicationProject.view;

import com.WebApplicationProject.control.EventFacade;
import com.WebApplicationProject.control.EventOccuranceFacade;
import com.WebApplicationProject.control.UsersFacade;
import com.WebApplicationProject.model.Event;
import com.WebApplicationProject.model.EventOccurance;
import com.WebApplicationProject.model.EventOccuranceParticipant;
import com.WebApplicationProject.model.Users;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleEvent;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author gabri
 */
@Named("scheduleView") 
@ViewScoped 
public class ScheduleView implements Serializable {
    
    @EJB
    private UsersFacade usersFacade;
    
    @EJB
    private EventOccuranceFacade eventOccuranceFacade; 
        
    @Getter
    @Setter
    private ScheduleModel eventModel;
    
    @Getter
    @Setter
    private ScheduleEvent event = new DefaultScheduleEvent();
    
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
    }
    
    
    public void getEvents() {
        
        //Find all events created by the user       
        for(Event e : user.getEventCollection()) {            
            for(EventOccurance eo : e.getEventOccuranceCollection()) {
                eventModel.addEvent(new DefaultScheduleEvent(e.getTitle(), eo.getStartDate(), eo.getEndDate()));
            }              
        }

        
        //Find all events where the user is included
        for(EventOccuranceParticipant e : user.getEventOccuranceParticipantCollection()) {
            
            eventModel.addEvent(new DefaultScheduleEvent(
                    e.getEventOccurance().getEvent().getTitle(),
                    e.getEventOccurance().getStartDate(), 
                    e.getEventOccurance().getEndDate()));  
        }
        
    }
    
    public Date getInitialDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), Calendar.FEBRUARY, calendar.get(Calendar.DATE), 0, 0, 0);
         
        return calendar.getTime();
    }
    
    private Calendar today() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
 
        return calendar;
    }
    
    public void onEventSelect(SelectEvent selectEvent) {
        event = (ScheduleEvent) selectEvent.getObject();
    }
     
    public void onDateSelect(SelectEvent selectEvent) {
        event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
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
    
}
