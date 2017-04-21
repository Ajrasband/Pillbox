package org.jaaa.pillbox;

import android.util.Log;

/**
 *Implements specific medications.
 */

public class Medications extends Events implements Events_Interface {
    private String medName;
    private double dosage;

    public Medications(){
        super();
        //All other input added
    }
    public Medications(String a, double b){
        super();
        medName = a;
        dosage = b;
    }

    public Events add(String a, double b) {
        try{
            //TODO ("Medication Name: \n\t");
            medName = a;
            //TODO ("Dosage Ammount: ")
            dosage = b;
            return new Medications(medName, dosage);
        }catch(Exception e){
            Log.d("Medications", "ERROR: add", e);
            return null;
        }
    }

    public Events edit(String a, double b) {
        try{
            //TODO ("Medication Name: ")
            medName = a;
            //TODO ("Dosage ammount: ")
            dosage = b;
            return new Medications(medName, dosage);
        }catch(Exception e){
            Log.d("Medications", "ERROR: edit", e);
            return null;
        }

    }

    public String getMedName(){
        return medName;
    }

    public double getDosage(){
        return dosage;
    }
}
