package com.islam.talleringo.models;

public class VehiclesModels {
    private String Brand, Name, Year;
    private int Id ;

    public VehiclesModels(String brand, String name, String year, int id) {
        Brand = brand;
        Name = name;
        Year = year;
        Id = id;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }
}
