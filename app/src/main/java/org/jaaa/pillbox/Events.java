package org.jaaa.pillbox;

import java.util.ArrayList;
import java.util.Date;

/**
 * Event object to use in Medication and calendar subclasses
 */

public class Events implements Events_Interface{

    public static ArrayList<Events> events = new ArrayList<>();

    private String eventName, eventDescription;
    private Date eventDate;

    public Events(){
        //Empty constructor
        eventName = "";
        eventDescription = "";
        eventDate = new Date();
    }

    public Events (String a, Date b, String c) {
        eventName = a;
        eventDate = b;
        eventDescription = c;
    }

    public Events add() {
        return null;
    }

    public Events edit(){
        return null;
    }

    //Setters
    public void setEventName(String newName) {
        eventName = newName;
    }

    public void setEventDate(Date newDate) {
        eventDate = newDate;
    }

    public void setEventDescription(String newDescription) {
        eventDescription = newDescription;
    }

    //Getters
    public String getEventName (){
        return eventName;
    }

    public Date getEventDate () {
        return eventDate;
    }

    public String getEventDescription (){
        return eventDescription;
    }
}
