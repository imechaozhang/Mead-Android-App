package com.example.mobilehealthprototype;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PatientViewModel extends AndroidViewModel {

    private PatientInfoRepo repo;

    private LiveData<List<PatientInfo>> all_patients;

    public PatientViewModel (Application application) {
        super(application);
        repo = new PatientInfoRepo(application);
        all_patients = repo.getAllPatients();
    }

    LiveData<List<PatientInfo>> getAllPatients() { return all_patients; }

    public void insert(PatientInfo patient_info) { repo.insert(patient_info); }
}