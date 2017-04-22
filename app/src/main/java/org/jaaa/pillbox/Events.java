package org.jaaa.pillbox;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * Event object to use in Medication and calendar subclasses
 */

public class Events implements Events_Interface{

    public static HashMap<String, ArrayList<Events>> events = new HashMap<>();

    private String eventName, eventDescription;
    private String time;

    public Events(){
        //Empty constructor
        eventName = "";
        eventDescription = "";
        time = "";
    }

    public Events (String a, String b, String c) {
        eventName = a;
        time = b;
        eventDescription = c;
    }

    public Events add() {
        return null;
    }

    public Events edit(){
        return null;
    }


    //Getters
    public String getEventName (){
        return eventName;
    }

    public String getTime () {
        return time;
    }

    public String getEventDescription (){
        return eventDescription;
    }
}
