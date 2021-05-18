package com.islam.talleringo.database;

import androidx.room.Database;
import androidx.room.RoomDatabase;

import com.islam.talleringo.database.Record.Record;
import com.islam.talleringo.database.Record.RecordDAO;
import com.islam.talleringo.database.Maintenances.Maintenance;
import com.islam.talleringo.database.Maintenances.MaintenanceDAO;
import com.islam.talleringo.database.Vehicles.Vehicle;
import com.islam.talleringo.database.Vehicles.VehicleDAO;

@Database(entities = {Vehicle.class, Record.class, Maintenance.class}, version = 1)
public abstract class AppDatabase  extends RoomDatabase {
    public abstract VehicleDAO vehicleDAO();
    public abstract MaintenanceDAO maintenanceDAO();
    public abstract RecordDAO recordDAO();
}
