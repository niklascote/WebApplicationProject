package com.WebApplicationProject.control;

import com.WebApplicationProject.db.CalendarFacade;
import com.WebApplicationProject.db.UsersFacade;
import com.WebApplicationProject.model.Auth;
import com.WebApplicationProject.model.Users;
import com.WebApplicationProject.model.Calendar;
import com.WebApplicationProject.view.util.JsfUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;

@Named("calendarController")
@ViewScoped
public class CalendarController implements Serializable {
    
    @Getter
    @Setter
    private Auth tmp = new Auth();
    
    private Calendar currentCal;
    
    @EJB
    private UsersFacade usersFacade;
    
    @EJB
    private CalendarFacade calendarFacade;

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
    
    @Getter
    @Setter
    private List<Users> users= new ArrayList<Users>();
    
    @Getter
    @Setter
    private Calendar calendar = new Calendar();
    
    @PostConstruct
    public void init() {
        user = usersFacade.find(1L);
        
        //TODO: Only for testing. Must be changed to a real user search. 
        //users = usersFacade.users(tmp.getEmail());
        //user = users.get(1);
        //currentCal = calendarFacade.find(1L);
        setCalendars();
        
    }
    
    public Calendar getSelected() {
        if (currentCal == null) {
            currentCal = new Calendar();
            
            System.out.println("test");
        }
        return currentCal;
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
    
    public CalendarFacade getFacade(){
        return calendarFacade;
    }
    
    public String prepareCreate(){
        currentCal = new Calendar();
        System.out.println("New calender created!");
        System.out.println("ID: " + currentCal.getId());
        System.out.println("Name: " + currentCal.getName());
        System.out.println("Desc: " + currentCal.getDescription());
        System.out.println("PA: " + currentCal.getPublicAccess());
        return "Create";
    }
    
    public String create(){
        System.out.println("Calendar name in create(): " + currentCal.getName());
        currentCal.setOwner(user);
        try {
            getFacade().create(currentCal);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("CalendarCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

}
