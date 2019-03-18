package com.WebApplicationProject.control;

import com.WebApplicationProject.db.CalendarParticipantFacade;
import com.WebApplicationProject.model.CalendarParticipant;
import java.io.Serializable;
import javax.ejb.EJB;
import javax.inject.Named;
import javax.enterprise.context.SessionScoped;
import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;
import javax.faces.model.DataModel;

@Named("calendarParticipantController")
@SessionScoped
public class CalendarParticipantController implements Serializable {

    private CalendarParticipant current;
    private DataModel items = null;
    @EJB
    private com.WebApplicationProject.db.CalendarParticipantFacade ejbFacade;
    private int selectedItemIndex;

    public CalendarParticipantController() {
    }

    public CalendarParticipant getSelected() {
        if (current == null) {
            current = new CalendarParticipant();
            selectedItemIndex = -1;
        }
        return current;
    }

    private CalendarParticipantFacade getFacade() {
        return ejbFacade;
    }

    public CalendarParticipant getCalendarParticipant(java.lang.Long id) {
        return ejbFacade.find(id);
    }

    @FacesConverter(forClass = CalendarParticipant.class)
    public static class CalendarParticipantControllerConverter implements Converter {

        @Override
        public Object getAsObject(FacesContext facesContext, UIComponent component, String value) {
            if (value == null || value.length() == 0) {
                return null;
            }
            CalendarParticipantController controller = (CalendarParticipantController) facesContext.getApplication().getELResolver().
                    getValue(facesContext.getELContext(), null, "calendarParticipantController");
            return controller.getCalendarParticipant(getKey(value));
        }

        java.lang.Long getKey(String value) {
            java.lang.Long key;
            key = Long.valueOf(value);
            return key;
        }

        String getStringKey(java.lang.Long value) {
            StringBuilder sb = new StringBuilder();
            sb.append(value);
            return sb.toString();
        }

        @Override
        public String getAsString(FacesContext facesContext, UIComponent component, Object object) {
            if (object == null) {
                return null;
            }
            if (object instanceof CalendarParticipant) {
                CalendarParticipant o = (CalendarParticipant) object;
                return getStringKey(o.getId());
            } else {
                throw new IllegalArgumentException("object " + object + " is of type " + object.getClass().getName() + "; expected type: " + CalendarParticipant.class.getName());
            }
        }

    }

}
