package com.tringuyen.example.hrmanagerandroid;

import android.util.Log;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Employee implements IDisplayable, Serializable {
    private String name;
    private int age;
    private String DOB;
    private String country;
    public Vehicle vehicle_owned;

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }
    public void setAge(int age) {
        if (age > 0 ) this.age = age;
        else Log.d("Employee","Invalid Age");
    }

    public String getDOB() { return DOB; }
    public void setDOB(String DOB) {
        Date date = null;
        try {
            date = new SimpleDateFormat("yyyy-MM-dd").parse(DOB);
        } catch (Exception ex) {
            Log.d("Employee","Invalid Age");
        }
        if (date!=null) this.DOB = DOB;
    }

    public String getCountry() { return country; }
    public void setCountry(String country) { country = country; }

    public Employee() {
        this.name = "";
        this.age = 0;
        this.DOB = "";
        this.country = "";
        this.vehicle_owned = null;
    }

    public Employee(String pName, int pAge, String pDOB, String pCountry, Vehicle pV) {
        this.name = pName;
        this.age = pAge;
        this.DOB = pDOB;
        this.country = pCountry;
        this.vehicle_owned = pV;
    }

    public Employee(String pName, int pAge, String pDOB, String pCountry, String  pPlate, String pMake) {
        this.name = pName;
        this.age = pAge;
        this.DOB = pDOB;
        this.country = pCountry;
        this.vehicle_owned = new Vehicle(pPlate, pMake);
    }

    public int calcBirthYear(){
        Calendar calendar = Calendar.getInstance();
        int currentYear = calendar.get(Calendar.YEAR);
        return (currentYear - this.age);
    }

    public int calcEaringings(){
        return 1000;
    }

    @Override
    public void displayData() {
        Log.d("Employee","Name: "+this.name);
        Log.d("Employee","Age: "+this.age);
        Log.d("Employee","DOB: "+this.DOB);
        Log.d("Employee","Country: "+this.country);
        if (vehicle_owned != null) {
            vehicle_owned.displayData();
        } else {
            Log.d("Employee","No vehicle registed");
        }
    }
}

