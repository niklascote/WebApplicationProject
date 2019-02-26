/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.WebApplicationProject.view;

import com.WebApplicationProject.model.Calendar;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.DefaultScheduleEvent;
import org.primefaces.model.ScheduleEvent;

/**
 *
 * @author gabri
 */
public class EventViewer extends DefaultScheduleEvent implements Serializable {

    @Getter
    @Setter
    private String location; 
    
    @Getter
    @Setter
    private Calendar calendar; 
       
        
    public EventViewer() {
        super();
        super.setAllDay(true);
    }
    
    public EventViewer(String title, Date start, Date end) {
        super(title, start, end);
        super.setAllDay(true);
    }
    
    public EventViewer(String title, Date start, Date end, boolean allDay) {
        super(title, start, end, allDay);
        super.setAllDay(true);
    }

    public EventViewer(String title, Date start, Date end, String styleClass) {
        super(title, start, end, styleClass);
        super.setAllDay(true);
    }

    public EventViewer(String title, Date start, Date end, Object data) {
        super(title, start, end, data);
        super.setAllDay(true);
    }
    
    public EventViewer(String title, Date start, Date end, String location, Calendar calendar) {
        super.setAllDay(true);
        super.setTitle(title);
        super.setStartDate(start);
        super.setEndDate(end);
        this.location = location; 
        this.calendar = calendar; 
        
    }
    
    @Override
    public void setStartDate(Date startDate) {
        super.setStartDate(startDate);
        super.setAllDay(false);
    }
    
    @Override
    public void setEndDate(Date endDate) {
        super.setEndDate(endDate);
        super.setAllDay(false);
    }        
    
    @Override
    public void setAllDay(boolean allDay) {
        
        if(allDay) {
            
            //Set start date to 00:00
            java.util.Calendar cal = java.util.Calendar.getInstance();  
            cal.setTime(super.getStartDate());  
            cal.set(java.util.Calendar.HOUR_OF_DAY, 0);  
            cal.set(java.util.Calendar.MINUTE, 0);              
            
            //Set end date to 23:59
            cal.setTime(super.getStartDate());  
            cal.set(java.util.Calendar.HOUR_OF_DAY, 23);  
            cal.set(java.util.Calendar.MINUTE, 59);  
        }  
        
        else {
            //Set start date to 00:00
            java.util.Calendar cal = java.util.Calendar.getInstance();  
            cal.setTime(super.getStartDate());  
            cal.set(java.util.Calendar.HOUR_OF_DAY, 8);  
            cal.set(java.util.Calendar.MINUTE, 0);              
            
            //Set end date to 23:59
            cal.setTime(super.getStartDate());  
            cal.set(java.util.Calendar.HOUR_OF_DAY, 8);  
            cal.set(java.util.Calendar.MINUTE, 30);  
        }
    }
    
    
}
