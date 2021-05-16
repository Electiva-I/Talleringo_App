package com.islam.talleringo.database.Maintenances;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

import java.util.Date;
import java.util.Objects;


@Entity
public class Maintenance {
    @PrimaryKey(autoGenerate = true)
    public int ID;

    @ColumnInfo(name = "maintenance_detail")
    public String Detail;

    @ColumnInfo(name = "maintenance_vehicle_id")
    public int Vehicle_Id;

    @ColumnInfo(name = "maintenance_date")
    public String Creation_Date;

    @ColumnInfo(name = "maintenance_schedule_date")
    public String Schedule_Date;

    public Maintenance(int Vehicle_Id, String Detail, String Creation_Date, String Schedule_Date){
        this.Creation_Date = Creation_Date;
        this.Schedule_Date = Schedule_Date;
        this.Detail = Detail;
        this.Vehicle_Id = Vehicle_Id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Maintenance that = (Maintenance) o;
        return ID == that.ID;
    }

    @Override
    public int hashCode() {
        return Objects.hash(ID);
    }
}
