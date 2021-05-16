package com.islam.talleringo.database.Maintenances;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface MaintenanceDAO {
        @Query("Select count(*) from maintenance")
        int countMaintenance();

        @Query("Select * from maintenance")
        List<Maintenance> getAll();

        @Query("Select * from maintenance where id in (:id)")
        Maintenance getMaintenance(int id);

        @Query("delete from maintenance where id in (:id)")
        void deleteId(int id);

        @Query("Select * from maintenance where id = (SELECT MAX(id) FROM maintenance)")
        Maintenance getLastMaintenance();

        @Query("delete from maintenance where maintenance_vehicle_id in (:id)")
        void deleteIdByVehicle(int[] id);

        @Insert
        void insertAll(Maintenance... maintenance);

        @Delete
        void delete(Maintenance maintenance);

        @Query("delete from maintenance")
        void deleteAll();

        @Update
        void  update(Maintenance maintenance);
}
