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
import android.view.View;
import android.widget.Button;

import com.ajithvgiri.searchdialog.OnSearchItemSelected;
import com.ajithvgiri.searchdialog.SearchListItem;
import com.ajithvgiri.searchdialog.SearchableDialog;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class PatientQuery extends AppCompatActivity {

    SearchableDialog sd;
    List<PatientInfo> patientList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_query);

        RecyclerView recyclerView;
        recyclerView = findViewById(R.id.recyclerview);
        final PatientListAdapter adapter = new PatientListAdapter(this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Get a new or existing ViewModel from the ViewModelProvider.
        PatientViewModel patientViewModel = new ViewModelProvider(this).get(PatientViewModel.class);
        patientList = patientViewModel.getAllPatients();

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
        sd = new SearchableDialog(ListSymptoms.this, allSymptoms,"Symptom Search");
        sd.setOnItemSelected(new OnSearchItemSelected(){
            public void onClick(int position, SearchListItem searchListItem){
                String newSmp = searchListItem.getTitle();
                if(!patientSymptoms.contains(newSmp)){
                    patientSymptoms.add(searchListItem.getTitle());
                    ((ListSymptoms.SymptomAdapter) currentSymptomListView.getAdapter()).notifyDataSetChanged();
                }
            }
        });

        Button addsymp = findViewById(R.id.add_symptom_button);
        CustomButton.changeButtonColor(this, addsymp, R.color.colorPrimaryDark,3, R.color.colorPrimaryDarkAccent);
        addsymp.setOnClickListener(new View.OnClickListener(){
            public void onClick(View view) {
                sd.show();
            }
        });

        //Sets up the ListView for the patient's current symptoms
        currentSymptomListView = findViewById(R.id.currentsymptomlist);
        adp = new ListSymptoms.SymptomAdapter(this, patientSymptoms);
        currentSymptomListView.setAdapter(adp);

        Button diagnose = findViewById(R.id.continue_diagnose_button);
        CustomButton.changeButtonColor(this, diagnose, R.color.colorPrimary,3, R.color.colorAccent);

        diagnose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListSymptoms.this, AdaptiveDiagnosis.class);
                intent.putExtra("mode", 1);
                intent.putExtra("hid", p_id);
                intent.putExtra("sex", p_sex);
                intent.putExtra("age", p_age);
                intent.putExtra("height", p_height);
                intent.putExtra("weight", p_weight);
                intent.putExtra("patient_symptoms", patientSymptoms);
                intent.putExtra("stu", SympToUmls);
                intent.putExtra("uts", UmlsToSymp);
                intent.putExtra("dtu", DisToUmls);
                intent.putExtra("utd", UmlsToDis);
                intent.putExtra("itud", IndexToUmls_d);
                intent.putExtra("itus", IndexToUmls_s);
                intent.putExtra("utid", UmlsToIndex_d);
                intent.putExtra("utis", UmlsToIndex_s);
                intent.putExtra("ncols", ncols);
                intent.putExtra("nrows", nrows);
                startActivity(intent);
            }
        });
    }
}
