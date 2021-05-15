package com.islam.talleringo.database.LiveData;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.islam.talleringo.database.Maintenances.Maintenance;
import com.islam.talleringo.database.Vehicles.Vehicle;

import java.util.List;

public class DataViewModel extends ViewModel {
    private MutableLiveData<Vehicle> currentVehicles;
    private MutableLiveData<Maintenance> currentMaintenance;

    public MutableLiveData<Vehicle> getNewVehicle() {
        if (currentVehicles == null) {
            currentVehicles = new MutableLiveData<Vehicle>();
        }
        return currentVehicles;
    }
    public MutableLiveData<Maintenance> getNewMaintenance() {
        if (currentMaintenance == null) {
            currentMaintenance = new MutableLiveData<Maintenance>();
        }
        return currentMaintenance;
    }
}
