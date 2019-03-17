package com.WebApplicationProject.control;

import com.WebApplicationProject.db.UsersFacade;
import com.WebApplicationProject.model.EventParticipant;
import com.WebApplicationProject.model.SessionUtil;
import com.WebApplicationProject.model.Users;
import com.WebApplicationProject.view.EventView;

import java.io.Serializable;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.inject.spi.CDI;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;

@Named("eventController")
@ViewScoped
public class EventController implements Serializable {
    
    private EventView eventView; 
    
    @EJB
    private UsersFacade usersFacade;

    @Getter
    @Setter
    private Users user = new Users();

    @PostConstruct
    public void init() {
        eventView = CDI.current().select(EventView.class).get();
        //Set user
        HttpSession session = SessionUtil.getSession();
        String email = (String) session.getAttribute("email");
        user = usersFacade.users(email);
        eventView.setUser(user);
        //Get all events for the user
        eventView.getEvents();
    }

    public String addEvent(List<EventParticipant> participants) {
        eventView.addEvent();
        return "pretty:calendar";
    }

    public String addRecurrentEvent(int forRange, String everyRange) {
        eventView.addRecurrentEvent(forRange, everyRange);
        return "pretty:calendar";
    }

    public String deleteParticipantEvent() {
        eventView.deleteParticipantEvent();
        return "pretty:calendar";
    }

    public String deleteAllEvents() {
        eventView.deleteAllEvents();
        return "pretty:calendar";
    }

    public String deleteThisEvent() {
        eventView.deleteThisEvent();
        return "pretty:calendar";
    }

    public String updateEvent(List<EventParticipant> participants) {
        eventView.updateEvent(participants);
        return "pretty:calendar";
    }

    public String deleteTempEvent() {
        eventView.clearEvent();
        return "pretty:calendar";
    }
}
