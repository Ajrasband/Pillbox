package org.jaaa.pillbox;

import android.util.Log;

import java.util.ArrayList;

/**
 *Implements specific medications.
 */

public class Medications extends Events implements Events_Interface {
    public static ArrayList<ArrayList<Medications>> medications = new ArrayList<>();

    static
    {
        medications.add(new ArrayList<Medications>());
        medications.add(new ArrayList<Medications>());
        medications.add(new ArrayList<Medications>());
        medications.add(new ArrayList<Medications>());
        medications.add(new ArrayList<Medications>());
        medications.add(new ArrayList<Medications>());
        medications.add(new ArrayList<Medications>());
    }

    private String medName;
    private double dosage;
    private String dosageType;

    public Medications(){
        super();
        //All other input added
    }
    public Medications(String a, double b, String d){
        super();
        medName = a;
        dosage = b;
        dosageType = d;
    }


    public Events add(String a, double b) {
        try{
            //TODO ("Medication Name: \n\t");
            medName = a;
            //TODO ("Dosage Ammount: ")
            dosage = b;
            return new Medications(medName, dosage, null);
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
            return new Medications(medName, dosage, null);
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

    public String getDosageType()
    {
        return dosageType;
    }
}
