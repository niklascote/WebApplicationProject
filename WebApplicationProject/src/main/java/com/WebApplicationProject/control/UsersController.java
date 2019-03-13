package com.WebApplicationProject.control;

import com.WebApplicationProject.db.EventParticipantFacade;
import com.WebApplicationProject.db.UsersFacade;
import com.WebApplicationProject.model.EventParticipant;
import com.WebApplicationProject.model.Users;
import com.WebApplicationProject.view.util.JsfUtil;
import com.WebApplicationProject.view.util.PaginationHelper;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import java.util.Map;
import java.util.ResourceBundle;
import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
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
    private PaginationHelper pagination;
    
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
    private List<Users> attendeesList = new ArrayList<Users>(); 
    
    @Getter
    private List<Users> selectedAttendees = new ArrayList<Users>();
    
    @Setter
    private List<EventParticipant> selectedEventAttendees = new ArrayList<EventParticipant>();
    
    private Boolean firstTime = true; 
    
    public List<EventParticipant> getSelectedEventAttendees() {
        return selectedEventAttendees;
    }
        
    public List<EventParticipant> getSelectedEventAttendees(Long eventId) {
        
        if(firstTime && eventId != null){
            this.selectedEventAttendees = eventParticipantFacade.getEventParticipantByEvent(eventId);
            firstTime = false;
        }
        
        return this.selectedEventAttendees;
    }
        

    @PostConstruct 
    public void init() {
        //TODO: Only for testing. Must be changed to a real user search. 
        user = usersFacade.find(1L);
    }

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
    
    public List<Users> completeAttendees(String query) {
        List<Users> allUsers = setAttendeesList();
        attendeesList = new ArrayList<>();
         
        for (int i = 0; i < allUsers.size(); i++) {
            Users skin = allUsers.get(i);
            
            if(skin.getFirstname().toLowerCase().contains(query) || skin.getLastname().toLowerCase().contains(query)) {
                attendeesList.add(skin);
            }
        }
         
        return attendeesList;
    }
    
    public List<Users> setAttendeesList() {
        List<Users> users = userFacade.findAll();
        users.remove(user);
        return users; 
    }
    
    public void setSelectedAttendees(List<Users> selectedAttendees){
                
        if(selectedAttendees == null) { return; }
        
        this.selectedAttendees = selectedAttendees;
        
        for(Users u : selectedAttendees) {  
            Boolean alreadyParticipant = false;
                        
            for(EventParticipant ep : selectedEventAttendees)  {
                if (ep.getParticipant().equals(u)) { 
                    alreadyParticipant = true;
                }
            }
        
            if(!alreadyParticipant){
                selectedEventAttendees.add(new EventParticipant(u));
            }
        }
    }
    
    public void removeAttendee(EventParticipant participant) {
        this.selectedEventAttendees.remove(participant);
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

    public PaginationHelper getPagination() {
        if (pagination == null) {
            pagination = new PaginationHelper(10) {

                @Override
                public int getItemsCount() {
                    return getFacade().count();
                }

                @Override
                public DataModel createPageDataModel() {
                    return new ListDataModel(getFacade().findRange(new int[]{getPageFirstItem(), getPageFirstItem() + getPageSize()}));
                }
            };
        }
        return pagination;
    }

    public String prepareList() {
        recreateModel();
        return "List";
    }

    public String prepareView() {
        current = (Users) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsersCreated"));
            return prepareCreate();
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String prepareEdit() {
        current = (Users) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        return "Edit";
    }

    public String update() {
        try {
            getFacade().edit(current);
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsersUpdated"));
            return "View";
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
            return null;
        }
    }

    public String destroy() {
        current = (Users) getItems().getRowData();
        selectedItemIndex = pagination.getPageFirstItem() + getItems().getRowIndex();
        performDestroy();
        recreatePagination();
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
            JsfUtil.addSuccessMessage(ResourceBundle.getBundle("/Bundle").getString("UsersDeleted"));
        } catch (Exception e) {
            JsfUtil.addErrorMessage(e, ResourceBundle.getBundle("/Bundle").getString("PersistenceErrorOccured"));
        }
    }

    private void updateCurrentItem() {
        int count = getFacade().count();
        if (selectedItemIndex >= count) {
            // selected index cannot be bigger than number of items:
            selectedItemIndex = count - 1;
            // go to previous page if last page disappeared:
            if (pagination.getPageFirstItem() >= count) {
                pagination.previousPage();
            }
        }
        if (selectedItemIndex >= 0) {
            current = getFacade().findRange(new int[]{selectedItemIndex, selectedItemIndex + 1}).get(0);
        }
    }

    public DataModel getItems() {
        if (items == null) {
            items = getPagination().createPageDataModel();
        }
        return items;
    }

    private void recreateModel() {
        items = null;
    }

    private void recreatePagination() {
        pagination = null;
    }

    public String next() {
        getPagination().nextPage();
        recreateModel();
        return "List";
    }

    public String previous() {
        getPagination().previousPage();
        recreateModel();
        return "List";
    }

    public Users getUsers(java.lang.Long id) {
        return usersFacade.find(id);
    }

    
}
