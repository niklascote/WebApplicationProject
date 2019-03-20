package com.WebApplicationProject.control;

import com.WebApplicationProject.db.EventFacade;
import com.WebApplicationProject.db.EventOccuranceFacade;
import com.WebApplicationProject.db.EventParticipantFacade;
import com.WebApplicationProject.db.UsersFacade;
import com.WebApplicationProject.model.Event;
import com.WebApplicationProject.model.EventOccurance;
import com.WebApplicationProject.model.EventParticipant;
import com.WebApplicationProject.model.EventViewModel;
import com.WebApplicationProject.model.SessionUtil;
import com.WebApplicationProject.model.Users;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;

@Named("eventController")
@ViewScoped
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
    private Users user = new Users();

    @PostConstruct
    public void init() {

        //Set user based on session
        HttpSession session = SessionUtil.getSession();
        String email = (String) session.getAttribute("email");
        user = usersFacade.users(email);
    }
    
    public String addEvent(EventViewModel event) {
        List<EventParticipant> participtans = new ArrayList<EventParticipant>();
        addEvent(event, participtans);
        return "";
    }

    public String addEvent(EventViewModel event, List<EventParticipant> participants) {
        
        if (event.isRecurrent()) {
            return addRecurrentEvent(event, participants);
        }

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

        return "pretty:calendar";
    }

    /**
     * Create a new event occurance for the repeated 
     * event and add it to the database
     * 
     * Basically does the same thing as addEvent, but with events fetched from 
     * addRecurrentEvent(EventViewModel event, List<EventParticipant> participants)
     * to make sure the same event is being repeated, and not creating new events
     * for every iteration
     * 
     * @param event Event to repeat
     * @param Id ID of event to repeat
     * @param participants Participants of event
     */
    public void addRecurrentEvent(EventViewModel event, Long Id, List<EventParticipant> participants) {
        Event e = eventFacade.find(Id);
        
        //Create new event occurance
        EventOccurance eo = new EventOccurance(
                eventFacade.find(Id),
                event.getEventOccurance().getStartDate(),
                event.getEventOccurance().getEndDate());

        //Add event occurrance to database
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
    }
    
    /**
     * Create new instances of one event, each with an incremented 
     * date depending on the chosen interval in @param event
     * 
     * @param event The event to repeated
     * @param participants Event participants
     * @return Redirection to calendar view
     */
    public String addRecurrentEvent(EventViewModel event, List<EventParticipant> participants) {
        //Create an instance of the event to be repeated
        Event e = new Event(event.getEvent().getTitle(),
                event.getEvent().getCalendar(),
                event.getEvent().getLocation(),
                user,
                event.getEvent().getReminder(),
                event.getEvent().getDescription()
        );

        //Add event to database
        eventFacade.create(e);
        
        //java.util.Date is mostly deprecated, so conversion to LocalDate is necessary for both original start and end date of event
        LocalDate localStartDate = event.getStartDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        LocalDate localEndDate = event.getEndDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        
        for (int i = 0; i < event.getRecurrentForRange(); i++) {
            
            //Increment days, weeks, months or years depending on the interval of the repetition
            switch (event.getRecurrentEveryRange()) {
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
            
            
            addRecurrentEvent(event, e.getId(), participants);
        }
        return "pretty:calendar";
    }
    
    public String deleteParticipantEvent(EventViewModel event) {

        if (event.getEvent().getOwner().equals(user)) {
            //Delete this event if user is owner
            deleteThisEvent(event);
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

    /**
     * Deletes all occurrences of an event
     * 
     * @param event The event to delete
     * @return Redirection to the calendar view
     */
    public String deleteAllEvents(EventViewModel event) {

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

        return "pretty:calendar";
    }

    public String deleteThisEvent(EventViewModel event) {

        //Delete event participants
        List<EventParticipant> participants = eventParticipantFacade.getEventParticipantByEvent(event.getEvent().getId());
        if (participants.size() > 0) {
            eventParticipantFacade.deleteEventParticipantByEvent(event.getEvent().getId());
        }

        //Delete event and occurances from database
        eventOccuranceFacade.remove(event.getEventOccurance());
        System.out.println("Delete event and occurances from database");

        //Only remove event if it is there is only one occurance
        if (event.getEvent().getEventOccuranceCollection().size() <= 1) {
            System.out.println("Only remove event if it it there is only one occurance");
            eventFacade.remove(event.getEvent());
        }

        System.out.println("Delete event");

        return "pretty:calendar";
    }

    public String updateEvent(EventViewModel event, List<EventParticipant> participants) {

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

        return "pretty:calendar";
    }

    public String deleteTempEvent() {
        return "pretty:calendar";
    }

}