package com.example.mobilehealthprototype;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;

public class Orientation extends AppCompatActivity{
    PatientInfo patient;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.orientation);
        handlePassedIntent();
        setUpInterface();
    }

    private void handlePassedIntent() {
        Intent passedIntent = getIntent();
        //patient = (PatientInfo) passedIntent.getSerializableExtra("patient");
        patient = passedIntent.getParcelableExtra("patient");
        System.out.println("=============testing parced patient birthyear");
        System.out.println(patient);
        System.out.println(patient.autoid);
        System.out.println(patient.birth_year);
        System.out.println("=============testing parced patient birthyear");

    }

    private void setUpInterface() {
        Button adaptiveButton = (Button) findViewById(R.id.adaptiveButton);
        adaptiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Orientation.this, ListSymptoms.class);
                intent.putExtra("patient", patient);
                startActivity(intent);
            }
        });

        Button recordingButton = (Button) findViewById(R.id.recordingButton);
        recordingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Orientation.this, RecordSymptoms.class);
                intent.putExtra("patient", patient);
                startActivity(intent);
            }
        });
    }

}
