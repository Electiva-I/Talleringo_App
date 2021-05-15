package com.islam.talleringo.database.Vehicles;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.ArrayList;
import java.util.List;

@Dao
public interface VehicleDAO {
    @Query("Select count(*) from vehicle")
    int countVehicles();

    @Query("Select * from vehicle")
    List<Vehicle> getAll();

    @Query("Select * from vehicle where id in (:id)")
    List<Vehicle> getVehicle(int id);

    @Query("delete from vehicle where id in (:id)")
    void deleteId(int[] id);


    @Insert
    void insertAll(Vehicle... vehicles);

    @Delete
    void delete(Vehicle vehicle);

    @Query("delete from vehicle")
    void deleteAll();
}
