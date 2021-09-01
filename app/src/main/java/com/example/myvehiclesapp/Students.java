package com.example.myvehiclesapp;

public class Students {
    String Name;
    String CarNumber;

    public Students() {
    }

    public Students(String name, String carNumber) {
        Name = name;
        CarNumber = carNumber;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCarnum() {
        return CarNumber;
    }

    public void setCarnum(String carNumber) {
        CarNumber = carNumber;
    }
}
