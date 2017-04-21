package org.jaaa.pillbox;

import java.util.TimeZone;
import java.util.Locale;
/**
 * Uses the data in the arraylist to create reminders of the medications
 */

public class Calendar extends Events implements Events_Interface {
    private String eventName;
    private TimeZone timeZone;
    private Locale locale;

    private java.util.Calendar date;



    public Calendar(){
        super();
    }

    public Events add(){
        //TODO ("Add event name: ");

        //eventName = Input.stringIn();
        //TODO ("Add event time: ")
        return null;

    }

}
