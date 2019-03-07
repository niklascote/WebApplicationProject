package com.WebApplicationProject.control;

import com.WebApplicationProject.db.UsersFacade;
import com.WebApplicationProject.model.Users;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.view.ViewScoped;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.TransferEvent;
import org.primefaces.event.UnselectEvent;

@Named("usersController")
@ViewScoped
public class UsersController implements Serializable {

    private Users current;
    private DataModel items = null;
    private int selectedItemIndex;
    
    @EJB
    private com.WebApplicationProject.db.UsersFacade userFacade;
    
    @EJB
    private UsersFacade usersFacade;
    
    @Getter
    @Setter
    private Users user = new Users();
        
    @Getter
    @Setter
    private List<Users> attendeesList = new ArrayList<Users>(); 
    

    @PostConstruct 
    public void init() {
        //TODO: Only for testing. Must be changed to a real user search. 
        user = usersFacade.find(1L);
    }

    public Users getSelected() {
        if (current == null) {
            current = new Users();
            current.setTimezone(shortenTimeZone(TimeZone.getDefault().getDisplayName()));
            selectedItemIndex = -1;
        }
        return current;
    }
    
    public String shortenTimeZone(String name){
        StringBuilder sB = new StringBuilder();
        char[] arr = name.toCharArray();
        for(char c : arr){
            if(Character.isUpperCase(c)){
                sB.append(c);
            }
        }
        return sB.toString();
    }
    
    public List<Users> completeAttendees(String query) {
        List<Users> allUsers = setAttendeesList();
        List<Users> filteredUsers = new ArrayList<>();
         
        for (int i = 0; i < allUsers.size(); i++) {
            Users skin = allUsers.get(i);
            
            if(skin.getFirstname().toLowerCase().contains(query) || skin.getLastname().toLowerCase().contains(query)) {
                filteredUsers.add(skin);
            }
        }
         
        return filteredUsers;
    }
    
    public List<Users> setAttendeesList() {
        List<Users> users = userFacade.findAll();
        users.remove(user);
        return users; 
    }
    
    public void onTransfer(TransferEvent event) {
        StringBuilder builder = new StringBuilder();
        for(Object item : event.getItems()) {
            builder.append(((Users) item).getFirstname()).append("<br />");
        }
         
        FacesMessage msg = new FacesMessage();
        msg.setSeverity(FacesMessage.SEVERITY_INFO);
        msg.setSummary("Items Transferred");
        msg.setDetail(builder.toString());
         
        FacesContext.getCurrentInstance().addMessage(null, msg);
    }  
     
    public void onSelect(SelectEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Selected", event.getObject().toString()));
    }
     
    public void onUnselect(UnselectEvent event) {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "Item Unselected", event.getObject().toString()));
    }
     
    public void onReorder() {
        FacesContext context = FacesContext.getCurrentInstance();
        context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_INFO, "List Reordered", null));
    }
}
