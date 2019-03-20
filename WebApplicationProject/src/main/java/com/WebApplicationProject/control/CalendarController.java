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
    
    /**
     * Finds user by email(username)
     * 
     * If user has a collection of at least 1 calendar, 
     * return the first calendar of that user's calendar
     * collection.
     * If user has no calendars in collection, create a
     * new empty public calendar
     */
    @PostConstruct
    public void init() {
        HttpSession session = SessionUtil.getSession();
        String email = (String) session.getAttribute("email");
        allUsers = usersFacade.findAll();
        user = usersFacade.users(email);
        
        List<Calendar> col = (List<Calendar>) user.getCalendarCollection();
        if(col.isEmpty()){
            currentCal = new Calendar();
            create();
        } else{
            currentCal = col.get(0);
        }
    }
    
    public Calendar getSelected() {
        if (currentCal == null) {
            currentCal = new Calendar();
        }
        return currentCal;
    }
        
    
    public CalendarFacade getFacade(){
        return calendarFacade;
    }
    
    /**
     * Reset currentCal
     * 
     * @return redirection to calendar page
     */
    public String prepareCreate(){
        currentCal = new Calendar();
        return "pretty:calendar";
    }
    
    /**
     * Creates a new calendar, sets owner to current user
     * and then adds the calendar to all users' collections
     * if calendar is set to public
     * 
     * @return call to prepareCreate()
     */
    public String create(){
            currentCal.setOwner(user);
            getFacade().create(currentCal);
            user.getCalendarCollection().add(currentCal);
            if(currentCal.getPublicAccess()){
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
