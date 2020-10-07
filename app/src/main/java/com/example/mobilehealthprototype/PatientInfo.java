package com.example.mobilehealthprototype;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "patient_table")
public class PatientInfo {

    @PrimaryKey
    @NonNull
    @ColumnInfo(name = "autoID")
    public int autoid;

    @NonNull
    @ColumnInfo(name = "ID")
    public String id;

    @NonNull
    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "symptoms")
    public String symptoms; //symptom1, symptom2...

    @NonNull
    @ColumnInfo(name = "diagnosis")
    public String diagnosis; //disease

    @ColumnInfo(name = "prescription")
    public String prescription;

    @ColumnInfo(name = "date")
    public String date;

    //public patientInfo(String name) {this.name = name;}

    //public String getWord(){return this.name;}
}
