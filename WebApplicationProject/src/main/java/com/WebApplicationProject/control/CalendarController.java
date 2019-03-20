package com.WebApplicationProject.control;

import com.WebApplicationProject.db.CalendarFacade;
import com.WebApplicationProject.db.UsersFacade;
import com.WebApplicationProject.model.Users;
import com.WebApplicationProject.model.Calendar;
import com.WebApplicationProject.model.SessionUtil;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
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
        
        System.out.println("User: " + user.getFirstname());
        List<Calendar> col = (List<Calendar>) user.getCalendarCollection();
        if(col.isEmpty()){
            System.out.println("User calendar list empty...");
            currentCal = new Calendar();
            create();
        } else{
            System.out.println("User calendar list not empty...");
            currentCal = col.get(0);
        }
    }
    
    public Calendar getSelected() {
        if (currentCal == null) {
            currentCal = new Calendar();
            
            System.out.println("test");
        }
        return currentCal;
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
            user.getCalendarCollection().add(currentCal);
            System.out.println("New calender created!");
            System.out.println("ID: " + currentCal.getId());
            System.out.println("Name: " + currentCal.getName());
            System.out.println("Desc: " + currentCal.getDescription());
            System.out.println("PA: " + currentCal.getPublicAccess());
            if(currentCal.getPublicAccess()){ //Adds calendar if public to all users
                for(Users u:allUsers){
                    if(!u.getEmail().equals(user.getEmail())){
                    u.getCalendarCollection().add(currentCal);
                    u.setCalendarCollection(u.getCalendarCollection());
                    }
                }
            }
            
            return prepareCreate();
    }

}
