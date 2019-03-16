package com.WebApplicationProject.control;

import com.WebApplicationProject.db.CalendarFacade;
import com.WebApplicationProject.db.UsersFacade;
import com.WebApplicationProject.model.Users;
import com.WebApplicationProject.model.Calendar;
import com.WebApplicationProject.model.SessionUtil;
import com.WebApplicationProject.view.util.JsfUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.view.ViewScoped;
import javax.servlet.http.HttpSession;
import lombok.Getter;
import lombok.Setter;

@Named("calendarController")
@ViewScoped
public class CalendarController implements Serializable {
    
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
    private List<Users> allUsers= new ArrayList<Users>();
    
    @Getter
    @Setter
    private Calendar calendar = new Calendar();
    
    @PostConstruct
    public void init() {
        //user = usersFacade.find(1L);
        
        //TODO: Only for testing. Must be changed to a real user search. 
        HttpSession session = SessionUtil.getSession();
        String email = (String) session.getAttribute("email");
        allUsers = usersFacade.findAll();
        user = usersFacade.users(email);
        //user = users.get(0);
        
        
        List<Calendar> col = (List<Calendar>) user.getCalendarCollection();
        if(col.isEmpty()){
            currentCal = new Calendar();
            create();
        } else{
            currentCal = col.get(0);
        }
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
    
    public List<Calendar> getAllCalendars(){
        return getFacade().findAll();
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
    
    public CalendarFacade getFacade(){
        return calendarFacade;
    }
    
    public String prepareCreate(){
        currentCal = new Calendar();
        return "pretty:calendar";
    }
    
    public String create(){
        
        
            currentCal.setOwner(user);
            getFacade().create(currentCal);
            System.out.println("New calender created!");
            System.out.println("ID: " + currentCal.getId());
            System.out.println("Name: " + currentCal.getName());
            System.out.println("Desc: " + currentCal.getDescription());
            System.out.println("PA: " + currentCal.getPublicAccess());
            if(currentCal.getPublicAccess()){ //Adds calendar if public to all users
                for(Users user:allUsers){
                    user.getCalendarCollection().add(currentCal);
                }
            }
            return prepareCreate();
    }

}
