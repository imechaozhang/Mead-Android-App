package com.example.mobilehealthprototype;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface PatientInfoDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(PatientInfo patientInfo);

    @Query("SELECT * from patient_table ORDER BY ID ASC")
    List<PatientInfo> getAll();

    @Query("SELECT * from patient_table WHERE ID = id ORDER BY ID ASC")
    List<PatientInfo> getById(int id);

}
