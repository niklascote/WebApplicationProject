package com.WebApplicationProject.control;

import com.WebApplicationProject.db.EventParticipantFacade;
import com.WebApplicationProject.db.UsersFacade;
import com.WebApplicationProject.model.Users;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import java.util.Map;
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
    
    @EJB
    private EventParticipantFacade eventParticipantFacade;
    
    @Getter
    @Setter
    private Users user = new Users();
    
    @Getter
    @Setter
    private List<Users> users = new ArrayList<Users>();
        
    //private HttpSession session = SessionUtil.getSession();
    
  
    public Users getSelected() {
        if (current == null) {
            current = new Users();
            //current.setTimezone(shortenTimeZone(TimeZone.getDefault().getDisplayName()));
            
            System.out.println("test");
            
            //current.setTimezone(getTimezone());
            
            selectedItemIndex = -1;
        }
        return current;
    }
    
    
    public String getTimezone(){
        return current.getTimezone();
    }
    
    public void onSetTimezone(){
        
        Map<String, String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        
        String timezone = params.get("timezone");
        
        //String timezone = (String) FacesContext.getCurrentInstance().getAttributes().get("timezone");
        System.out.println("Timezone: " + timezone);
        getSelected().setTimezone(timezone);
        //System.out.println("kwedjkedjekdjkedjekjdekjdekjdekd");
        //Locale locale = request.getLocale();
        //current.setTimezone(request.);
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
    
    private UsersFacade getFacade() {
        return usersFacade;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Users) getItems().getRowData();
        return "View";
    }

    public String prepareCreate() {
        current = new Users();
        selectedItemIndex = -1;
        return "Create";
    }

    public String create() {
        try {
            getFacade().create(current);
            return prepareCreate();
        } catch (Exception e) {
            return null;
        }
    }

    public String prepareEdit() {
        current = (Users) getItems().getRowData();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            return "View";
        } catch (Exception e) {
            return null;
        }
    }

    public String destroy() {
        current = (Users) getItems().getRowData();
        performDestroy();
        recreateModel();
        return "List";
    }

    public String destroyAndView() {
        performDestroy();
        recreateModel();
        updateCurrentItem();
        if (selectedItemIndex >= 0) {
            return "View";
        } else {
            // all items were removed - go back to list
            recreateModel();
            return "List";
        }
    }

    private void performDestroy() {
        try {
            getFacade().remove(current);
        } catch (Exception e) {
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    public Users getUsers(java.lang.Long id) {
        return usersFacade.find(id);
    }

    
}
