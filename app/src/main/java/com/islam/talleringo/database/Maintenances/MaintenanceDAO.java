package com.islam.talleringo.database.Maintenances;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.islam.talleringo.database.Vehicles.Vehicle;

import java.util.List;

@Dao
public interface MaintenanceDAO {

        @Query("Select * from maintenance")
        List<Maintenance> getAll();

        @Query("Select * from maintenance where id in (:id)")
        List<Maintenance> getMaintenance(int[] id);

        @Query("delete from maintenance where id in (:id)")
        void deleteId(int[] id);

        @Insert
        void insertAll(Maintenance... maintenance);

        @Delete
        void delete(Maintenance maintenance);

        @Query("delete from maintenance")
        void deleteAll();
}
