package com.tringuyen.example.hrmanagerandroid;

import android.util.Log;

public class FullTime extends Employee {

    public int salary;
    public int bonus;

    public FullTime(){
        super();
        salary = 0;
        bonus = 0;
    }

    public FullTime(String name, int age, String DOB, String country, int salary, int bonus, Vehicle pPV){
        super(name, age, DOB, country, pPV);
        this.salary = salary;
        this.bonus = bonus;
    }

    public FullTime(String name, int age, String DOB, String country, int salary, int bonus, String pPPlate, String pPMake){
        super(name, age, DOB, country, pPPlate, pPMake);
        this.salary = salary;
        this.bonus = bonus;
    }

    @Override
    public int calcEaringings() {
        return (salary + bonus);
    }

    @Override
    public void displayData() {
        super.displayData();
        Log.d("FullTime","Salary: " + Integer.toString(salary));
        Log.d("FullTime","Bonus: " + Integer.toString(bonus));
    }
}
