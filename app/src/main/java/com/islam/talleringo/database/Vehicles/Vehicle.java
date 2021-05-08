package com.islam.talleringo.database.Vehicles;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Vehicle {
    @PrimaryKey(autoGenerate = true)
    public int ID;

    @ColumnInfo(name = "vehicle_model")
    public String Model;

    @ColumnInfo(name = "vehicle_brand")
    public String Brand;

    @ColumnInfo(name = "vehicle_year")
    public String Year;

    public Vehicle(String Model, String Brand, String Year){
        this.Brand = Brand;
        this.Year = Year;
        this.Model = Model;
    }

    @Override
    public String toString() {
        return Brand+" "+Model+" "+Year;
    }
}
