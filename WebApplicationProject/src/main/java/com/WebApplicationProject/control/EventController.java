package com.WebApplicationProject.control;

import com.WebApplicationProject.db.EventFacade;
import com.WebApplicationProject.db.EventOccuranceFacade;
import com.WebApplicationProject.db.EventParticipantFacade;
import com.WebApplicationProject.db.UsersFacade;
import com.WebApplicationProject.model.Event;
import com.WebApplicationProject.model.EventOccurance;
import com.WebApplicationProject.model.EventParticipant;
import com.WebApplicationProject.model.Users;
import com.WebApplicationProject.view.EventViewer;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import lombok.Getter;
import lombok.Setter;
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

    @EJB
    private EventParticipantFacade eventParticipantFacade;

    @Getter
    @Setter
    private ScheduleModel eventModel = new DefaultScheduleModel();

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

    public String addEvent() {
        if (event.isRecurrent()) {
            System.out.println("Creating recurrent event");
            return addRecurrentEvent(event.getRecurrentForRange(), event.getRecurrentEveryRange());
        }
        return addEvent(new ArrayList<>());
    }

    public Boolean writePremission() {

        //If event has not been created yet
        if (event.getEvent().getId() == null) {
            return false;
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

    /**
     * Adds a recurrent event to the calendar
     *
     * @param forRange The duration of the recurring
     * @param everyRange The frequency of the recurring
     * @return Redirection to the schedule view through prettyfaces
     */
    public String addRecurrentEvent(int forRange, String everyRange) {
        System.out.println("For range: " + forRange);
        System.out.println("Every range: " + everyRange);
        LocalDate localStartDate = event.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localEndDate = event.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        for (int i = 0; i < forRange; i++) {

            switch (everyRange) {
                case "Day":
                    event.setStartDate(java.sql.Date.valueOf(localStartDate.plusDays(i)));
                    event.setEndDate(java.sql.Date.valueOf(localEndDate.plusDays(i)));
                    break;

                case "Week":
                    event.setStartDate(java.sql.Date.valueOf(localStartDate.plusWeeks(i)));
                    event.setEndDate(java.sql.Date.valueOf(localEndDate.plusWeeks(i)));
                    break;

                case "Month":
                    event.setStartDate(java.sql.Date.valueOf(localStartDate.plusMonths(i)));
                    event.setEndDate(java.sql.Date.valueOf(localEndDate.plusMonths(i)));
                    break;

                case "Year":
                    event.setStartDate(java.sql.Date.valueOf(localStartDate.plusYears(i)));
                    event.setEndDate(java.sql.Date.valueOf(localEndDate.plusYears(i)));
                    break;
            }

            System.out.println("Loop #" + i);
            System.out.println("Event #" + (i + 1) + "'s date: " + event.getStartDate());
            addRecurrentEvent(new ArrayList<>());

        }
        clearEvent();
        return "pretty:calendar";
    }

    public String addEvent(List<EventParticipant> participants) {

        //Add event to Event-table in DB        
        Event e = new Event(event.getEvent().getTitle(),
                event.getEvent().getCalendar(),
                event.getEvent().getLocation(),
                user,
                event.getEvent().getReminder(),
                event.getEvent().getDescription()
        );

        eventFacade.create(e);

        //Add event occurance to EventOccurance-table in DB    
        EventOccurance eo = new EventOccurance(
                eventFacade.find(e.getId()),
                event.getEventOccurance().getStartDate(),
                event.getEventOccurance().getEndDate());

        eventOccuranceFacade.create(eo);

        //Add event participant
        if (participants.size() > 0) {
            for (EventParticipant ep : participants) {
                ep.setEvent(e);
                if (!ep.equals(event.getEvent().getOwner())) {
                    eventParticipantFacade.create(ep);
                }
            }
        }

        eventModel.addEvent(new EventViewer(e, eo));
        clearEvent();

        return "pretty:calendar";
    }

    public void addRecurrentEvent(List<EventParticipant> participants) {

        //Add event to Event-table in DB        
        Event e = new Event(event.getEvent().getTitle(),
                event.getEvent().getCalendar(),
                event.getEvent().getLocation(),
                user,
                event.getEvent().getReminder(),
                event.getEvent().getDescription()
        );

        eventFacade.create(e);

        //Add event occurance to EventOccurance-table in DB    
        EventOccurance eo = new EventOccurance(
                eventFacade.find(e.getId()),
                event.getEventOccurance().getStartDate(),
                event.getEventOccurance().getEndDate());

        eventOccuranceFacade.create(eo);

        //Add event participant
        if (participants.size() > 0) {
            for (EventParticipant ep : participants) {
                ep.setEvent(e);
                if (!ep.equals(event.getEvent().getOwner())) {
                    eventParticipantFacade.create(ep);
                }
            }
        }

        eventModel.addEvent(new EventViewer(e, eo));

    }

    public String deleteTemporaryEvent() {
        clearEvent();
        return "pretty:calendar";
    }

    public String deleteParticipantEvent() {

        if (event.getEvent().getOwner().equals(user)) {
            //Delete this event if user is owner
            deleteThisEvent();
        } else {
            //Delete the event participants from event
            List<EventParticipant> participants = eventParticipantFacade.getEventParticipantByEvent(event.getEvent().getId());

            for (EventParticipant ep : participants) {
                if (ep.equals(user)) {
                    eventParticipantFacade.remove(ep);
                }
            }
        }

        return "pretty:calendar";
    }

    public String deleteAllEvents() {

        List<EventOccurance> occurances = eventOccuranceFacade.getEventOccurancesByEvent(event.getEvent().getId());

        //Remove event         
        for (EventOccurance eo : occurances) {
            eventOccuranceFacade.remove(eo);
        }

        //Delete all event participants from event
        List<EventParticipant> participants = eventParticipantFacade.getEventParticipantByEvent(event.getEvent().getId());
        if (participants.size() > 0) {
            eventParticipantFacade.deleteEventParticipantByEvent(event.getEvent().getId());
        }

        //Delete event
        eventFacade.remove(event.getEvent());

        getEvents();
        clearEvent();

        return "pretty:calendar";
    }

    public String deleteThisEvent() {

        //Delete event participants
        List<EventParticipant> participants = eventParticipantFacade.getEventParticipantByEvent(event.getEvent().getId());
        if (participants.size() > 0) {
            eventParticipantFacade.deleteEventParticipantByEvent(event.getEvent().getId());
        }

        //Delete event and occurances from database
        eventOccuranceFacade.remove(event.getEventOccurance());

        //Only remove event if it is there is only one occurance
        if (event.getEvent().getEventOccuranceCollection().size() <= 1) {
            eventFacade.remove(event.getEvent());
        }

        eventModel.deleteEvent(event);
        clearEvent();

        return "pretty:calendar";
    }

    public String updateEvent(List<EventParticipant> participants) {

        //Update entities
        eventFacade.edit(event.getEvent());
        eventOccuranceFacade.edit(event.getEventOccurance());

        //Update event participant
        if (participants.size() > 0) {

            eventParticipantFacade.deleteEventParticipantByEvent(event.getEvent().getId());

            for (EventParticipant ep : participants) {
                ep.setEvent(event.getEvent());

                //Do not add if the user is the owner
                if (!ep.equals(event.getEvent().getOwner())) {
                    eventParticipantFacade.create(ep);
                }
            }
        }

        //Update event model
        eventModel.updateEvent(event);
        clearEvent();

        return "pretty:calendar";
    }

    public String deleteTempEvent() {
        clearEvent();
        return "pretty:calendar";
    }

    public void getEvents() {

        eventModel.clear();

        //Find all events created by the user
        List<Event> events = eventFacade.getEventByOwner(user.getId());

        for (Event e : events) {
            List<EventOccurance> occurances = eventOccuranceFacade.getEventOccurancesByEvent(e.getId());
            for (EventOccurance eo : occurances) {
                eventModel.addEvent(new EventViewer(e, eo));
            }
        }

        //Find all events where the user is included
        List<EventParticipant> participantEvents = eventParticipantFacade.getEventParticipantByParticipant(user.getId());
        for (EventParticipant ep : participantEvents) {
            List<EventOccurance> occurances = eventOccuranceFacade.getEventOccurancesByEvent(ep.getEvent().getId());
            for (EventOccurance eo : occurances) {
                eventModel.addEvent(new EventViewer(ep.getEvent(), eo));
            }
        }
    }

    public void setSelectedEvent(EventViewer selected) {
        event = selected;
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
}
