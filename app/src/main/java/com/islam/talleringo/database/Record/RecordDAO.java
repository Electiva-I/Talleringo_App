package com.islam.talleringo.database.Record;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.islam.talleringo.database.Maintenances.Maintenance;
import com.islam.talleringo.database.Vehicles.Vehicle;

import java.util.List;

@Dao
public interface RecordDAO {
    @Query("select count(*) from record")
    int countHistory();

    @Query("Select * from record")
    List<Record> getAll();

    @Query("Select * from record where id = :id")
    Record getRecord(int id);

    @Query("delete from record where id in (:id)")
    void deleteId(int[] id);

    @Query("delete from record where record_vehicle_id in (:id)")
    void deleteIdByVehicle(int[] id);

    @Query("Select * from record where id = (SELECT MAX(id) FROM record)")
    Record getLastRecord();

    @Insert
    void insertAll(Record... records);

    @Delete
    void delete(Record record);

    @Query("delete from record")
    void deleteAll();

    @Update
    void  update(Record record);

}
