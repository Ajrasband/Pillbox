package org.jaaa.pillbox;

/**
 *
 */

public class Doctor{
    private String name;
    private String type;
    private String number;
  
    public Doctor(String name, String type, String number){
        super();
        this.name = name;
        this.number = number;
        this.type = type;

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
        return String.format("%s\n%s\n%s", name, type, numberFormat());
    }
    public String numberFormat(){

        if (number.length() == 10)
        {
            String p1 = number.substring(0, 3);
            String p2 = number.substring(3, 6);
            String p3 = number.substring(6, 10);
            return ("(" + p1 + ")" + p2 + "-" + p3);
        }
        else
        {
            return number;
        }
    }
}
