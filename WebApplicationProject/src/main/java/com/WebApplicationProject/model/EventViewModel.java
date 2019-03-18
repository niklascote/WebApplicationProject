/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.WebApplicationProject.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import org.primefaces.model.DefaultScheduleEvent;

/**
 *
 * @author gabri
 */
public class EventViewModel extends DefaultScheduleEvent implements Serializable {

    @Getter
    @Setter
    private Event event = new Event();

    @Getter
    @Setter
    private boolean recurrent = false;

    @Getter
    @Setter
    private int recurrentForRange = 0;

    @Getter
    @Setter
    private String recurrentEveryRange = null;

    @Getter
    @Setter
    private EventOccurance eventOccurance = new EventOccurance();
            
    public EventViewModel() {
        super();
        super.setAllDay(true);
    }

    public EventViewModel(String title, Date start, Date end) {
        super(title, start, end);
        super.setAllDay(true);
        changeAllDay();
    }

    public EventViewModel(Event e, EventOccurance eo) {
        super.setAllDay(true);
        super.setTitle(e.getTitle());
        super.setStartDate(eo.getStartDate());
        super.setEndDate(eo.getEndDate());
        super.setDescription(e.getDescription());
        this.event = e;
        this.eventOccurance = eo;
    }

    public void changeAllDay() {
        if (super.isAllDay()) {

            //Set start date to 00:00
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(super.getStartDate());
            cal.set(java.util.Calendar.HOUR_OF_DAY, 0);
            cal.set(java.util.Calendar.MINUTE, 0);
            setStartDate(cal.getTime());

            //Set end date to 23:59
            cal.setTime(super.getEndDate());
            cal.set(java.util.Calendar.HOUR_OF_DAY, 23);
            cal.set(java.util.Calendar.MINUTE, 59);
            setEndDate(cal.getTime());
        } else {
            //Set start date to 00:00
            java.util.Calendar cal = java.util.Calendar.getInstance();
            cal.setTime(super.getStartDate());
            cal.set(java.util.Calendar.HOUR_OF_DAY, 8);
            cal.set(java.util.Calendar.MINUTE, 0);
            setStartDate(cal.getTime());

            //Set end date to 23:59
            cal.setTime(super.getEndDate());
            cal.set(java.util.Calendar.HOUR_OF_DAY, 8);
            cal.set(java.util.Calendar.MINUTE, 30);
            setEndDate(cal.getTime());
        }
    }

    @Override
    public void setTitle(String title) {
        super.setTitle(title);
        event.setTitle(title);
    }

    @Override
    public void setStartDate(Date startDate) {
        super.setStartDate(startDate);
        eventOccurance.setStartDate(startDate);
    }

    @Override
    public void setEndDate(Date endDate) {
        super.setEndDate(endDate);
        eventOccurance.setEndDate(endDate);
    }
}
