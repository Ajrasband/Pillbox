package org.jaaa.pillbox;

import java.util.ArrayList;
/**
 * ArrayList of doctors
 */

public class Doc_List {

    public static ArrayList <Doctor> docList = new ArrayList<Doctor>();

    public static void addDoc(){
        Doctor doc = new Doctor();
        docList.add(0, doc);
    }
    public static Doctor getDoc(){
        int i;
        //TODO get doctor object they want to find
        Doctor useDoc = docList.get(i);
        useDoc.getInfo();
    }

}
