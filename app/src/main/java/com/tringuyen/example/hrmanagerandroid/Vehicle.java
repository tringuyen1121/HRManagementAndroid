package com.tringuyen.example.hrmanagerandroid;

import android.util.Log;

public class Vehicle implements IDisplayable {

    public String plateNumber;
    public String make;

    Vehicle() {
        this.plateNumber = "";
        this.make = "";
    }

    Vehicle(String pPlate, String pMake) {
        this.plateNumber = pPlate;
        this.make = pMake;
    }

    @Override
    public void displayData() {
        Log.d("Vehicle","Vichile Make: "+this.make);
        Log.d("Vehicle","Vichile Plate: "+this.plateNumber);
    }

}

