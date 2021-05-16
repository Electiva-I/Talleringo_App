package com.islam.talleringo.database.Record;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class Record {
    @PrimaryKey(autoGenerate = true)
    public int ID;

    @ColumnInfo(name = "record_detail")
    public String Detail;

    @ColumnInfo(name = "record_vehicle_id")
    public int Vehicle_Id;

    @ColumnInfo(name = "record_date")
    public String Creation_Date;

    @ColumnInfo(name = "record_cost")
    public float Cost;

    public Record(int Vehicle_Id, String Detail, String Creation_Date, float Cost){
        this.Creation_Date = Creation_Date;
        this.Detail = Detail;
        this.Vehicle_Id = Vehicle_Id;
        this.Cost = Cost;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Record record = (Record) o;
        return ID == record.ID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }
}
