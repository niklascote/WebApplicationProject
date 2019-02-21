/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.WebApplicationProject.view;

import com.WebApplicationProject.model.ScheduleEvent;
import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.view.ViewScoped;
import javax.inject.Named;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.DefaultScheduleModel;
import org.primefaces.model.ScheduleModel;

/**
 *
 * @author gabri
 */
@Named("scheduleView") 
@ViewScoped 
public class ScheduleView implements Serializable {
        
    @Getter
    @Setter
    private ScheduleModel eventModel;
    
    @PostConstruct
    public void init() {
         eventModel = new DefaultScheduleModel();
    }
    
    public Date getInitialDate() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), Calendar.FEBRUARY, calendar.get(Calendar.DATE), 0, 0, 0);
         
        return calendar.getTime();
    }
    
    private Calendar today() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DATE), 0, 0, 0);
 
        return calendar;
    }
    
//    public void onEventSelect(SelectEvent selectEvent) {
//        event = (ScheduleEvent) selectEvent.getObject();
//    }
//     
//    public void onDateSelect(SelectEvent selectEvent) {
//        event = new DefaultScheduleEvent("", (Date) selectEvent.getObject(), (Date) selectEvent.getObject());
//    }
//     
//    public void onEventMove(ScheduleEntryMoveEvent event) {
//        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event moved", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
//         
//        addMessage(message);
//    }
//     
//    public void onEventResize(ScheduleEntryResizeEvent event) {
//        FacesMessage message = new FacesMessage(FacesMessage.SEVERITY_INFO, "Event resized", "Day delta:" + event.getDayDelta() + ", Minute delta:" + event.getMinuteDelta());
//         
//        addMessage(message);
//    }
//     
//    private void addMessage(FacesMessage message) {
//        FacesContext.getCurrentInstance().addMessage(null, message);
//    }
    
}
