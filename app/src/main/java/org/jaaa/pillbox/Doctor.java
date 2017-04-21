package org.jaaa.pillbox;

/**
 *
 */

public class Doctor{
    private String name;
    private String type;
    private String number;

    public Doctor(){
        super();
        //TODO Get Doc name
        name = Input.stringIn();
        //TODO get doc type (occupation / specialty)
        type = Input.stringIn();
        //TODO get number to call
        number = Input.stringIn();
    }

    public String getName(){
        return name;
    }

    public String getNumber() {
        return number;
    }

    public String getType() {
        return type;
    }

    public String getInfo(){
        numberFormat();
        return String.format("%s \n%s", name, type, number);
    }
    public String numberFormat(){
        String p1 = number.substring(0, 3);
        String p2 = number.substring(3, 6);
        String p3 = number.substring(6, 10);
        number = ("(" + p1 + ")" + p2 + "-" + p3);
        return  number;
    }
}
