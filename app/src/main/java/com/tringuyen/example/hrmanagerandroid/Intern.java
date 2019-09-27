package com.tringuyen.example.hrmanagerandroid;

import android.util.Log;

public class Intern extends Employee {

    public String collegeName;

    public Intern(){
        super();
        collegeName = "";
    }

    public Intern(String name, int age, String DOB, String country, String collegeName, Vehicle pPV){
        super(name, age, DOB, country, pPV);
        this.collegeName = collegeName;
    }

    public Intern(String name, int age, String DOB, String country, String collegeName, String pPPlate, String pPMake){
        super(name, age, DOB, country, pPPlate, pPMake);
        this.collegeName = collegeName;
    }

    @Override
    public void displayData() {
        super.displayData();
        Log.d("Intern","School: " + collegeName);
    }
}

