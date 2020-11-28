package com.example.mobilehealthprototype;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import java.time.LocalDate;
import java.util.Objects;

@Entity(tableName = "patient_table")
public class PatientInfo implements Parcelable {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "autoID")
    public int autoid;

    @NonNull
    @ColumnInfo(name = "ID")
    public String id;

    @NonNull
    @ColumnInfo(name = "name")
    public String name;

    @ColumnInfo(name = "height")
    public float height;

    @ColumnInfo(name = "weight")
    public float weight;

    @ColumnInfo(name = "gender")
    public String gender;

    @ColumnInfo(name = "birth_year")
    public int birth_year;

    @ColumnInfo(name = "symptoms")
    public String symptoms;

    @ColumnInfo(name = "lab_test")
    public  String lab_test;

    @ColumnInfo(name = "diagnosis")
    public String diagnosis; //disease

    @ColumnInfo(name = "prescription")
    public String prescription;

    @ColumnInfo(name = "date")
    public String date;

    @Override
    public int describeContents() {
        return 0;
    }


    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(symptoms);
        parcel.writeString(lab_test);
        parcel.writeString(diagnosis);
        parcel.writeString(prescription);
        parcel.writeInt(birth_year);
        parcel.writeFloat(height);
        parcel.writeFloat(weight);
        parcel.writeString(gender);
    }

    public PatientInfo(){}

    public PatientInfo(Parcel source) {
        id = Objects.requireNonNull(source.readString());
        name = Objects.requireNonNull(source.readString());
        symptoms = source.readString();
        lab_test = source.readString();
        diagnosis = source.readString();
        prescription = source.readString();
        birth_year = source.readInt();
        height = source.readFloat();
        weight = source.readFloat();
        gender = source.readString();
    }
    public static final Creator<PatientInfo> CREATOR = new Creator<PatientInfo>() {
        @Override
        public PatientInfo createFromParcel(Parcel source) {
            return new PatientInfo(source);
        }

        @Override
        public PatientInfo[] newArray(int i) {
            return new PatientInfo[0];
        }
    };
}
