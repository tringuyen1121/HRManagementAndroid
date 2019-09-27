package com.tringuyen.example.hrmanagerandroid;

import android.util.Log;

public class PartTime extends Employee {

    public int hoursWorked;
    public int rate;

    public PartTime(){
        super();
        hoursWorked = 0;
        rate = 0;
    }

    public PartTime(String name, int age, String DOB, String country, int hoursWorked, int rate, Vehicle pPV){
        super(name, age, DOB, country, pPV);
        this.hoursWorked = hoursWorked;
        this.rate = rate;
    }

    public PartTime(String name, int age, String DOB, String country, int hoursWorked, int rate, String pPPlate, String pPMake){
        super(name, age, DOB, country, pPPlate, pPMake);
        this.hoursWorked = hoursWorked;
        this.rate = rate;
    }

    @Override
    public int calcEaringings() {
        return (hoursWorked * rate);
    }

    @Override
    public void displayData() {
        super.displayData();
        Log.d("PartTime","HoursWorked: " + Integer.toString(hoursWorked));
        Log.d("PartTime","Rate: " + Integer.toString(rate));
    }

}
