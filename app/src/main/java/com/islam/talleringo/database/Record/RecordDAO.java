package com.islam.talleringo.database.Record;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import com.islam.talleringo.database.Maintenances.Maintenance;

import java.util.List;

@Dao
public interface RecordDAO {
    @Query("select count(*) from record")
    int countHistory();

    @Query("Select * from record")
    List<Record> getAll();

    @Query("Select * from record where id in (:id)")
    List<Record> getMaintenance(int[] id);

    @Query("delete from record where id in (:id)")
    void deleteId(int[] id);

    @Query("delete from record where record_vehicle_id in (:id)")
    void deleteIdByVehicle(int[] id);

    @Insert
    void insertAll(Record... records);

    @Delete
    void delete(Record record);

    @Query("delete from record")
    void deleteAll();

}
