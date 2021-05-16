package com.islam.talleringo.database.LiveData;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.islam.talleringo.database.Maintenances.Maintenance;
import com.islam.talleringo.database.Record.Record;
import com.islam.talleringo.database.Vehicles.Vehicle;

public class DataViewModel extends ViewModel {
    private MutableLiveData<Vehicle> currentVehicles;
    private MutableLiveData<Maintenance> currentMaintenance;
    private MutableLiveData<Record> currentRecord;
    //update data
    private MutableLiveData<Record> updatedRecord;
    private MutableLiveData<Maintenance> updatedMaintenance;
    private MutableLiveData<Vehicle> updatedVehicle;
    //deleted data
    private MutableLiveData<Maintenance> deletedMaintenance;
    public MutableLiveData<Vehicle> getNewVehicle() {
        if (currentVehicles == null) {
            currentVehicles = new MutableLiveData<>();
        }
        return currentVehicles;
    }
    public MutableLiveData<Maintenance> getNewMaintenance() {
        if (currentMaintenance == null) {
            currentMaintenance = new MutableLiveData<>();
        }
        return currentMaintenance;
    }

    public MutableLiveData<Record> getNewRecord() {
        if (currentRecord == null) {
            currentRecord = new MutableLiveData<>();
        }
        return currentRecord;
    }

    public MutableLiveData<Record> getUpdatedRecord() {
        if (updatedRecord == null) {
            updatedRecord = new MutableLiveData<>();
        }
        return updatedRecord;
    }


    public MutableLiveData<Maintenance> getUpdatedMaintenance() {
        if (updatedMaintenance == null) {
            updatedMaintenance = new MutableLiveData<>();
        }
        return updatedMaintenance;
    }

    public MutableLiveData<Maintenance> getDeletedMaintenance() {
        if (deletedMaintenance == null) {
            deletedMaintenance = new MutableLiveData<>();
        }
        return deletedMaintenance;
    }

    public MutableLiveData<Vehicle> getUpdatedVehicle() {
        if (updatedVehicle == null) {
            updatedVehicle = new MutableLiveData<>();
        }
        return updatedVehicle;
    }
}
