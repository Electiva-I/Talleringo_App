package com.islam.talleringo.database.Maintenances;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;
import java.util.UUID;


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

    @ColumnInfo(name = "maintenance_schedule_uuid")
    public String uuid;

    @ColumnInfo(name = "maintenance_schedule_notify")
    public Boolean notify;

    @ColumnInfo(name = "maintenance_schedule_hour")
    public String hour;

    public Maintenance(int Vehicle_Id, String Detail, String Creation_Date, String Schedule_Date, Boolean notify, String hour){
        this.Creation_Date = Creation_Date;
        this.Schedule_Date = Schedule_Date;
        this.Detail = Detail.substring(0, 1).toUpperCase() + Detail.substring(1);
        this.Vehicle_Id = Vehicle_Id;
        this.uuid = UUID.randomUUID().toString();
        this.notify = notify;
        this.hour = hour;
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
