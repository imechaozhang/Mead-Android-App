package com.example.mobilehealthprototype;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PatientInfoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(PatientInfo patientInfo);

    @Query("SELECT * from patient_table ORDER BY ID ASC")
    List<PatientInfo> getAll();

    @Query("SELECT * from patient_table WHERE ID LIKE :id ORDER BY ID ASC")
    LiveData<List<PatientInfo>> getById(String id);

    @Query("SELECT * from patient_table WHERE name LIKE :patientName ORDER BY ID ASC")
    LiveData<List<PatientInfo>> getByName(String patientName);

    @Query("DELETE FROM patient_table")
    void deleteAll();

}
