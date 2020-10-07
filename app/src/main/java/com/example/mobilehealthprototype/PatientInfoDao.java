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

    @Query("SELECT * from patient_table WHERE ID LIKE :id ORDER BY ID ASC")
    List<PatientInfo> getById(String id);

    @Query("SELECT * from patient_table WHERE name LIKE :patient_name ORDER BY ID ASC")
    List<PatientInfo> getByName(String patient_name);


}
