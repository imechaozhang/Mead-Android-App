package com.example.mobilehealthprototype;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class PatientInfoRepo {
    private PatientInfoDao patient_dao;
    private List<PatientInfo> patient_list;


    PatientInfoRepo(Application application) {
        PatientDatabase db = PatientDatabase.getDatabase(application);
        patient_dao = db.PatientInfoDao();
        patient_list = patient_dao.getAll();
    }

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    List<PatientInfo> getAllPatients() {
        return patient_list;
    }

    // You must call this on a non-UI thread or your app will throw an exception. Room ensures
    // that you're not doing any long running operations on the main thread, blocking the UI.
    void insert(PatientInfo patientInfo) {
        PatientDatabase.databaseWriteExecutor.execute(() -> {
            patient_dao.insert(patientInfo);
        });
    }
}
