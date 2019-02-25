/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.WebApplicationProject.view;

import com.WebApplicationProject.model.Calendar;
import java.io.Serializable;
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
    }
    
    public EventViewer(String title, Date start, Date end) {
        super(title, start, end);
    }
    
    public EventViewer(String title, Date start, Date end, boolean allDay) {
        super(title, start, end, allDay);
    }

    public EventViewer(String title, Date start, Date end, String styleClass) {
        super(title, start, end, styleClass);
    }

    public EventViewer(String title, Date start, Date end, Object data) {
        super(title, start, end, data);
    }
    
    public EventViewer(String title, Date start, Date end, String location, Calendar calendar) {
        super.setTitle(title);
        super.setStartDate(start);
        super.setEndDate(end);
        this.location = location; 
        this.calendar = calendar; 
    }
}
