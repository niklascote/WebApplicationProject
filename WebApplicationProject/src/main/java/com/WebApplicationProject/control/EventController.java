package com.WebApplicationProject.control;

import com.WebApplicationProject.db.EventFacade;
import com.WebApplicationProject.db.EventOccuranceFacade;
import com.WebApplicationProject.db.ReminderFacade;
import com.WebApplicationProject.db.UsersFacade;
import com.WebApplicationProject.model.Event;
import com.WebApplicationProject.model.EventOccurance;
import com.WebApplicationProject.model.Users;
import com.WebApplicationProject.view.EventViewer;
import com.WebApplicationProject.view.util.JsfUtil;
import com.WebApplicationProject.view.util.PaginationHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.faces.model.SelectItem;
import javax.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.PrimeFaces;
import org.primefaces.event.ScheduleEntryMoveEvent;
import org.primefaces.event.ScheduleEntryResizeEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

@Named("eventController")
@SessionScoped
public class EventController implements Serializable {

    @EJB
    private UsersFacade usersFacade;

    @EJB
    private EventFacade eventFacade;

    @EJB
    private EventOccuranceFacade eventOccuranceFacade;

    @Getter
    @Setter
    private ScheduleModel eventModel = new DefaultScheduleModel();
    ;
        
    @Getter
    @Setter
    private EventViewer event = new EventViewer();

    @Getter
    @Setter
    private Users user = new Users();

    @PostConstruct
    public void init() {
        //TODO: Only for testing. Must be changed to a real user search. 
        user = usersFacade.find(1L);

        //Get all events for the user
        getEvents();
    }

    public void addEvent() {

        //Add event to Event-table in DB        
        Event e = new Event(event.getEvent().getTitle(),
                event.getEvent().getCalendar(),
                event.getEvent().getLocation(),
                user,
                event.getEvent().getReminder(),
                event.getEvent().getDescription()
        );

        eventFacade.create(e);

        //Todo: When repeating this must be change to multiple occurances
        //Add event occurance to EventOccurance-table in DB    
        EventOccurance eo = new EventOccurance(
                eventFacade.find(e.getId()),
                event.getEventOccurance().getStartDate(),
                event.getEventOccurance().getEndDate());
                
        eventOccuranceFacade.create(eo);
        
        eventModel.addEvent(new EventViewer(e, eo));
    }

    public String deleteAllEvents() {
        //Find choosen event in database
        Event e = eventFacade.find(event.getEvent().getId());

        //Remove event and occurances from database 
        e.getEventOccuranceCollection().forEach((eo) -> {
            eventOccuranceFacade.remove(eo);
        });
        eventFacade.remove(e);

        updateEventModel();

        return "pretty:calendar";
    }

    public void updateEventModel() {
        eventModel = new DefaultScheduleModel();
        getEvents();
    }

    public String deleteThisEvent() {
        //Find choosen event in database
        Event e = eventFacade.find(event.getEvent().getId());
        EventOccurance eo = eventOccuranceFacade.find(event.getEventOccurance().getId());

        //Delete event and occurances from database
        eventOccuranceFacade.remove(eo);

        //Only remove event if it is there is only one occurance
        if (e.getEventOccuranceCollection().size() > 1) {
            eventFacade.remove(e);
            eventModel.deleteEvent(event);
        }

        return "pretty:calendar";

    }

    public String updateEvent() {

        //Get event entity
        Event e = eventFacade.find(event.getEvent().getId());

        //Update entity
        e.setCalendar(event.getEvent().getCalendar());
        e.setDescription(event.getEvent().getDescription());
        e.setLocation(event.getEvent().getLocation());
        e.setReminder(event.getEvent().getReminder());
        e.setTitle(event.getEvent().getTitle());
        eventFacade.edit(e);

        eventModel.updateEvent(event);

        //TODO: Update event occurances
        return "pretty:calendar";
    }

    public String deleteTempEvent() {
        clearEvent();
        return "pretty:calendar";
    }

    public void getEvents() {

        //Find all events created by the user
        user.getEventCollection().forEach((e) -> {
            e.getEventOccuranceCollection().forEach((eo) -> {
                eventModel.addEvent(new EventViewer(e, eo));
            });
        });

        //Find all events where the user is included
        user.getEventOccuranceParticipantCollection().forEach((eo) -> {
            eventModel.addEvent(new EventViewer(eo.getEventOccurance().getEvent(), eo.getEventOccurance()));
        });
    }
    
    public void setSelectedEvent(EventViewer selected) {
        
        event = selected; 
    }

    public void onEventSelect(SelectEvent selectEvent) {
        event = (EventViewer) selectEvent.getObject();
    }

    public void onDateSelect(SelectEvent selectEvent) {
        event = new EventViewer("", (Date)selectEvent.getObject(), (Date)selectEvent.getObject());
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
}
