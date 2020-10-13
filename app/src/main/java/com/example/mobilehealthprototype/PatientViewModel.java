package com.example.mobilehealthprototype;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;

public class PatientViewModel extends AndroidViewModel {

    private PatientInfoRepo repo;

    private List<PatientInfo> all_patients;

    public PatientViewModel (Application application) {
        super(application);
        repo = new PatientInfoRepo(application);
        all_patients = repo.getAllPatients();
    }

    List<PatientInfo> getAllPatients() { return all_patients; }

    public void insert(PatientInfo patient_info) { repo.insert(patient_info); }
}