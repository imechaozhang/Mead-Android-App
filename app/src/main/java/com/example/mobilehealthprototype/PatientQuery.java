package com.example.mobilehealthprototype;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;

import com.ajithvgiri.searchdialog.OnSearchItemSelected;
import com.ajithvgiri.searchdialog.SearchListItem;
import com.ajithvgiri.searchdialog.SearchableDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class PatientQuery extends AppCompatActivity {

    SearchableDialog sd;
    List<PatientInfo> patientList;
    List<SearchListItem> allPatients = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_query);

        // Get a new or existing ViewModel from the ViewModelProvider.
        PatientViewModel patientViewModel = new ViewModelProvider(this).get(PatientViewModel.class);
        patientList = patientViewModel.getAllPatients();
        for (PatientInfo patientInfo:patientList){
            SearchListItem t = new SearchListItem(0, patientInfo.id + ":" + patientInfo.name);
            allPatients.add(t);
        }

        setUpInterface();

        //FloatingActionButton fab = findViewById(R.id.fab);
        //fab.setOnClickListener(new View.OnClickListener() {
        //    @Override
        //    public void onClick(View view) {
        //        Intent intent = new Intent(MainActivity.this, NewWordActivity.class);
        //        startActivityForResult(intent, NEW_WORD_ACTIVITY_REQUEST_CODE);
        //    }
        //});

    }

    public void setUpInterface(){
        //Setting up the search view to look up symptoms
        sd = new SearchableDialog(PatientQuery.this, allPatients,"Patient Search");
        sd.setOnItemSelected(new OnSearchItemSelected(){
            public void onClick(int position, SearchListItem searchListItem){
                String newSmp = searchListItem.getTitle();
                String id = newSmp.split(";")[0];
                PatientInfo patient= patientList.get(0);

                for (PatientInfo patientInfo:patientList){
                    if (patientInfo.id == id){
                        patient = patientInfo;
                        break;
                    }
                }

                Intent intent = new Intent(PatientQuery.this, Orientation.class);
                intent.putExtra("patient", patient);
                startActivity(intent);
            }
        });

        Button findPatient = findViewById(R.id.find_patient_button);
        CustomButton.changeButtonColor(this, findPatient, R.color.colorPrimaryDark,3, R.color.colorPrimaryDarkAccent);
        findPatient.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                sd.show();
            }
        });

    }
}
