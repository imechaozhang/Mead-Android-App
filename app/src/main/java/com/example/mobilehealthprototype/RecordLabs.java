package com.example.mobilehealthprototype;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ajithvgiri.searchdialog.OnSearchItemSelected;
import com.ajithvgiri.searchdialog.SearchListItem;
import com.ajithvgiri.searchdialog.SearchableDialog;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;

public class RecordLabs extends AppCompatActivity {
    //important properties for the GUI to load in
    List<SearchListItem> allSymptoms = new ArrayList<>();
    List<SearchListItem> allDiseases = new ArrayList<>();
    List<SearchListItem> allLabs = new ArrayList<>();
    List<SearchListItem> allDrugs = new ArrayList<>();
    ArrayList<String> patientSymptoms = new ArrayList<>();
    ArrayList<String> patientLabs = new ArrayList<>();
    String diagnosis;
    ListView currentDiseaseListView;
    LabAdapter adp;
    SearchableDialog sd;
    ListView currentLabListView;

    public Hashtable<String, Integer> DisToIndex = new Hashtable<>();
    public Hashtable<Integer, String> IndexToDis = new Hashtable<>();
    public Hashtable<String, Integer> SympToIndex = new Hashtable<>();
    public Hashtable<Integer,String> IndexToSymp = new Hashtable<>();
    public Hashtable<String, Integer> LabToIndex = new Hashtable<>();
    public Hashtable<Integer,String> IndexToLab = new Hashtable<>();
    public Hashtable<String, Integer> DrugToIndex = new Hashtable<>();
    public Hashtable<Integer,String> IndexToDrug = new Hashtable<>();

    float[][] wm, lab_dis, drug_dis;
    Intent passedIntent;
    String p_sex;
    String p_id;
    int p_age;
    float p_height, p_weight;
    PatientInfo patient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_record_lab);
        handlePassedIntent();
        loadFiles("wmrec.csv", "labrec.csv", "drugrec.csv");
        setUpInterface();
    }

    public void handlePassedIntent(){
        passedIntent = getIntent();
        patient = passedIntent.getParcelableExtra("patient");
        p_sex = passedIntent.getStringExtra("sex");
        p_id = passedIntent.getStringExtra("hid");
        p_age = passedIntent.getIntExtra("age", -1);
        p_height = passedIntent.getFloatExtra("height",-1);
        p_weight = passedIntent.getFloatExtra("weight",-1);
        patientSymptoms = passedIntent.getStringArrayListExtra("patient_symptoms");
        DisToIndex = new Hashtable<> ((HashMap<String,Integer>) passedIntent.getSerializableExtra("DisToIndex"));
        IndexToDis = new Hashtable<> ((HashMap<Integer,String>) passedIntent.getSerializableExtra("IndexToDis"));
        SympToIndex = new Hashtable<> ((HashMap<String,Integer>) passedIntent.getSerializableExtra("SympToIndex"));
        IndexToSymp = new Hashtable<> ((HashMap<Integer,String>) passedIntent.getSerializableExtra("IndexToSymp"));
        LabToIndex = new Hashtable<> ((HashMap<String,Integer>) passedIntent.getSerializableExtra("LabToIndex"));
        IndexToLab = new Hashtable<> ((HashMap<Integer,String>) passedIntent.getSerializableExtra("IndexToLab"));
        DrugToIndex = new Hashtable<> ((HashMap<String,Integer>) passedIntent.getSerializableExtra("DrugToIndex"));
        IndexToDrug = new Hashtable<> ((HashMap<Integer,String>) passedIntent.getSerializableExtra("IndexToDrug"));
    }

    public void loadFiles(String WeightMatrix, String Lab, String Drug) { //ArrayList<String>
        //Load symptoms and diseases
        try {
            InputStreamReader is = new InputStreamReader(getAssets().open(WeightMatrix));
            BufferedReader reader = new BufferedReader(is);
            String nl;
            String[] temp;
            int index = 0;

            // first line for symptom list
            nl = reader.readLine();
            temp = nl.split(",");
            int ncols = temp.length - 1;
            for (int sympidx = 0; sympidx < ncols; sympidx++) {
                SympToIndex.put(temp[sympidx + 1], sympidx);
                IndexToSymp.put(sympidx, temp[sympidx + 1]);

                SearchListItem t = new SearchListItem(sympidx, temp[sympidx + 1]);
                allSymptoms.add(t);
            }

            while ((nl = reader.readLine()) != null) {
                temp = nl.split(",");
                DisToIndex.put(temp[0], index);
                IndexToDis.put(index, temp[0]);
                SearchListItem t = new SearchListItem(index, temp[0]);
                allDiseases.add(t);
                // UMLS codes are temporally abandoned
                //SympToUmls.put(temp[1], temp[0]);
                //UmlsToSymp.put(temp[0], temp[1]);
                index++;
                //SearchListItem t = new SearchListItem(0, temp[1]);
                //allSymptoms.add(t);
            }
            int nrows = index;
            reader.close();

            float[][] weight_matrix = new float[nrows][ncols];

            is = new InputStreamReader(getAssets().open(WeightMatrix));
            reader = new BufferedReader(is);
            nl = reader.readLine(); //skip the first line of the CSV

            for (int r = 0; r < nrows; r++) {
                nl = reader.readLine();
                temp = nl.split(",");
                for (int c = 0; c < ncols; c++) {
                    weight_matrix[r][c] = Float.parseFloat(temp[c + 1]);
                }
            }
            reader.close();
            wm = weight_matrix;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ERROR", "AN ERROR HAS OCCURRED IN LOADFILES(Symptoms)");
        }

        //load lab
        try {
            InputStreamReader is = new InputStreamReader(getAssets().open(Lab));
            BufferedReader reader = new BufferedReader(is);
            String nl;
            String[] temp;
            int index = 0;

            // first line for lab list
            nl = reader.readLine();
            temp = nl.split(",");
            int ncols = temp.length - 1;
            for (int idx = 0; idx < ncols; idx++) {
                LabToIndex.put(temp[idx + 1], idx);
                IndexToLab.put(idx, temp[idx + 1]);

                SearchListItem t = new SearchListItem(idx, temp[idx + 1]);
                allLabs.add(t);
            }

            while ((nl = reader.readLine()) != null) {
                index++;
            }
            int nrows = index;
            reader.close();

            float[][] lab_matrix = new float[nrows][ncols];

            is = new InputStreamReader(getAssets().open(Lab));
            reader = new BufferedReader(is);
            nl = reader.readLine(); //skip the first line of the CSV

            for (int r = 0; r < nrows; r++) {
                nl = reader.readLine();
                temp = nl.split(",", -1);
                for (int c = 0; c < ncols; c++) {
                    if (temp[c + 1].equals("")) {
                        lab_matrix[r][c] = 0;
                    } else {
                        lab_matrix[r][c] = Float.parseFloat(temp[c + 1]);
                    }

                }
            }
            reader.close();
            lab_dis = lab_matrix;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ERROR", "AN ERROR HAS OCCURRED IN LOADFILES (Lab)");
        }

        //load drug
        try {
            InputStreamReader is = new InputStreamReader(getAssets().open(Drug));
            BufferedReader reader = new BufferedReader(is);
            String nl;
            String[] temp;
            int index = 0;

            // first line for lab list
            nl = reader.readLine();
            temp = nl.split(",", -1);
            int ncols = temp.length - 1;
            for (int idx = 0; idx < ncols; idx++) {
                DrugToIndex.put(temp[idx + 1], idx);
                IndexToDrug.put(idx, temp[idx + 1]);

                SearchListItem t = new SearchListItem(idx, temp[idx + 1]);
                allDrugs.add(t);
            }

            while ((nl = reader.readLine()) != null) {
                index++;
            }
            int nrows = index;
            reader.close();

            float[][] drug_matrix = new float[nrows][ncols];

            is = new InputStreamReader(getAssets().open(Drug));
            reader = new BufferedReader(is);
            nl = reader.readLine(); //skip the first line of the CSV

            for (int r = 0; r < nrows; r++) {
                nl = reader.readLine();
                temp = nl.split(",", -1);
                for (int c = 0; c < ncols; c++) {
                    if (temp[c + 1].equals("")) {
                        drug_matrix[r][c] = 0;
                    } else {
                        drug_matrix[r][c] = Float.parseFloat(temp[c + 1]);
                    }

                }
            }
            reader.close();
            drug_dis = drug_matrix;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("ERROR", "AN ERROR HAS OCCURRED IN LOADFILES (Drug)");
        }
    }

    public void setUpInterface(){
        //Setting up the search view to look up symptoms
        sd = new SearchableDialog(RecordLabs.this, allLabs,"Disease Search");
        sd.setOnItemSelected(new OnSearchItemSelected(){
            public void onClick(int position, SearchListItem searchListItem) {
                String lab = searchListItem.getTitle();
                if (!patientLabs.contains(lab)) {
                    patientLabs.add(searchListItem.getTitle());
                    ((RecordSymptoms.SymptomAdapter) currentLabListView.getAdapter()).notifyDataSetChanged();
                }

                if (patientLabs.size() == 1) {
                    patient.lab_test = lab;
                }

                patient.lab_test += ',' + lab;
            }
        });

        Button addlab = findViewById(R.id.select_lab_button);
        CustomButton.changeButtonColor(this, addlab, R.color.colorPrimaryDark, 3, R.color.colorPrimaryDarkAccent);
        addlab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                sd.show();
            }
        });

        //Sets up the ListView for the patient's current symptoms

        currentLabListView = findViewById(R.id.recordlablist);
        adp = new RecordLabs.LabAdapter(this, patientLabs);
        currentLabListView.setAdapter(adp);

        Button skip = findViewById(R.id.record_lab_skip);
        CustomButton.changeButtonColor(this, skip, R.color.colorPrimary, 3, R.color.colorAccent);

        skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(RecordLabs.this, ConfirmationScreen.class);
                intent.putExtra("hid", p_id);
                intent.putExtra("sex", p_sex);
                intent.putExtra("age", p_age);
                intent.putExtra("height", p_height);
                intent.putExtra("weight", p_weight);
                intent.putExtra("patient_symptoms", patientSymptoms);
                intent.putExtra("DisToIndex", DisToIndex);
                intent.putExtra("SympToIndex", SympToIndex);
                intent.putExtra("LabToIndex", LabToIndex);
                intent.putExtra("DrugToIndex", DrugToIndex);
                intent.putExtra("IndexToDis", IndexToDis);
                intent.putExtra("IndexToSymp", IndexToSymp);
                intent.putExtra("IndexToLab", IndexToLab);
                intent.putExtra("IndexToDrug", IndexToDrug);
                intent.putExtra("patient", patient);
                intent.putExtra("diagnosed_disease_index", -1);
                intent.putExtra("likelihood_of_disease", 1);
                intent.putExtra("diagnosed_UMLS", "null");
                intent.putExtra("diagnosed_disease_name", "null");
                startActivity(intent);
            }
        });
    }

    public static class LabAdapter extends BaseAdapter implements ListAdapter {
        private ArrayList<String> list = new ArrayList<String>();
        private final Context context;

        public LabAdapter(Context context, ArrayList<String> list) {
            this.list = list;
            this.context = context;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int pos) {
            return list.get(pos);
        }

        @Override
        //necessary, but will never be used
        public long getItemId(int pos) {
            return 0; //just return 0 if your list items do not have an Id variable.
        }

        @Override
        public void notifyDataSetChanged(){
            super.notifyDataSetChanged();
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View view = convertView;
            if (view == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                view = inflater.inflate(R.layout.item_curr_symptom, null);
            }

            //Handle TextView and display string from your list
            TextView listItemText = view.findViewById(R.id.list_item_symptom);
            listItemText.setText(list.get(position));

            //Handle buttons and add onClickListeners
            Button deleteBtn = view.findViewById(R.id.delete_symptom_button);
            deleteBtn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    list.remove(position); //or some other task
                    notifyDataSetChanged();
                }
            });

            return view;
        }
    }

}
