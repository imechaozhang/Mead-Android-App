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
import java.util.Hashtable;
import java.util.List;

//Review https://github.com/ajithvgiri/search-dialog - for Searchable Dialogue documentation
public class ListSymptoms extends AppCompatActivity {
    //important properties for the GUI to load in
    List<SearchListItem> allSymptoms = new ArrayList<>();
    ArrayList<String> patientSymptoms = new ArrayList<>();
    ListView currentSymptomListView;
    SymptomAdapter adp;
    SearchableDialog sd;
/*  // old version of hashmaps
    Hashtable<String, String> SympToUmls= new Hashtable<String, String>();
    Hashtable<String, String> UmlsToSymp= new Hashtable<String, String>();
    Hashtable<Integer, String> IndexToUmls_s = new Hashtable<>();
    Hashtable<String, Integer> UmlsToIndex_s = new Hashtable<>();
    Hashtable<String, Integer> UmlsToIndex_d = new Hashtable<>();
    Hashtable<Integer, String> IndexToUmls_d = new Hashtable<>();
    Hashtable<String, String> DisToUmls = new Hashtable<>();
    Hashtable<String,String> UmlsToDis = new Hashtable<>();

 */

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
        setContentView(R.layout.activity_list_symptoms);
        handlePassedIntent();
        //wm = loadFiles("SymptomList.csv", "DiseaseList.csv", "Dis_Sym_30.csv");
        loadFiles("wmrec.csv", "labrec.csv", "drugrec.csv");
        setUpInterface();
    }

    public void handlePassedIntent(){
        passedIntent = getIntent();
        patient = passedIntent.getParcelableExtra("patient");
        //p_sex = (Sex) passedIntent.getSerializableExtra("sex");
        //p_id = passedIntent.getIntExtra("hid", -1);
        //p_age = passedIntent.getIntExtra("age", -1);
        //p_height = passedIntent.getFloatExtra("height",-1);
        //p_weight = passedIntent.getFloatExtra("weight",-1);
        p_age = patient.birth_year;
        p_height = patient.height;
        p_id = patient.id;
        p_weight = patient.weight;
        p_sex = patient.gender;

    }

    //Loads up all the symptoms from the file into our activity
    public void loadFiles(String WeightMatrix, String Lab, String Drug){ //ArrayList<String>
        //Load symptoms and diseases
        try{
            InputStreamReader is = new InputStreamReader(getAssets().open(WeightMatrix));
            BufferedReader reader = new BufferedReader(is);
            String nl;
            String[] temp;
            int index = 0;

            // first line for symptom list
            nl = reader.readLine();
            temp = nl.split(",");
            int ncols = temp.length-1;
            for(int sympidx=0; sympidx<ncols; sympidx++){
                SympToIndex.put(temp[sympidx+1], sympidx);
                IndexToSymp.put(sympidx, temp[sympidx+1]);

                SearchListItem t = new SearchListItem(sympidx, temp[sympidx+1]);
                allSymptoms.add(t);
            }

            while((nl = reader.readLine()) != null){
                temp = nl.split(",");
                DisToIndex.put(temp[0], index);
                IndexToDis.put(index, temp[0]);
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

            for(int r = 0; r < nrows; r++){
                nl = reader.readLine();
                temp = nl.split(",");
                for(int c = 0; c < ncols; c++){
                    weight_matrix[r][c] = Float.parseFloat(temp[c + 1]);
                }
            }
            reader.close();
            wm = weight_matrix;
        }catch (IOException e){
            e.printStackTrace();
            Log.e("ERROR", "AN ERROR HAS OCCURRED IN LOADFILES(Symptoms)");
        }

        //load lab
        try{
            InputStreamReader is = new InputStreamReader(getAssets().open(Lab));
            BufferedReader reader = new BufferedReader(is);
            String nl;
            String[] temp;
            int index = 0;

            // first line for lab list
            nl = reader.readLine();
            temp = nl.split(",");
            int ncols = temp.length-1;
            for(int idx=0; idx<ncols; idx++){
                LabToIndex.put(temp[idx+1], idx);
                IndexToLab.put(idx, temp[idx+1]);
            }

            while((nl = reader.readLine()) != null){
                index++;
            }
            int nrows = index;
            reader.close();

            float[][] lab_matrix = new float[nrows][ncols];

            is = new InputStreamReader(getAssets().open(Lab));
            reader = new BufferedReader(is);
            nl = reader.readLine(); //skip the first line of the CSV

            for(int r = 0; r < nrows; r++){
                nl = reader.readLine();
                temp = nl.split(",");
                for(int c = 0; c < ncols; c++){
                    System.out.println("===========debugging========");
                    System.out.println(temp.length);
                    System.out.println(c);
                    System.out.println("===========debugging========");
                    if (temp[c + 1].equals("")){
                        lab_matrix[r][c] = 0;
                    }
                    else{
                        lab_matrix[r][c] = Float.parseFloat(temp[c + 1]);
                    }

                }
            }
            reader.close();
            lab_dis = lab_matrix;
        }catch (IOException e){
            e.printStackTrace();
            Log.e("ERROR", "AN ERROR HAS OCCURRED IN LOADFILES (Lab)");
        }

        //load drug
        try{
            InputStreamReader is = new InputStreamReader(getAssets().open(Drug));
            BufferedReader reader = new BufferedReader(is);
            String nl;
            String[] temp;
            int index = 0;

            // first line for lab list
            nl = reader.readLine();
            temp = nl.split(",");
            int ncols = temp.length-1;
            for(int idx=0; idx<ncols; idx++){
                DrugToIndex.put(temp[idx+1], idx);
                IndexToDrug.put(idx, temp[idx+1]);
            }

            while((nl = reader.readLine()) != null){
                index++;
            }
            int nrows = index;
            reader.close();

            float[][] drug_matrix = new float[nrows][ncols];

            is = new InputStreamReader(getAssets().open(Drug));
            reader = new BufferedReader(is);
            nl = reader.readLine(); //skip the first line of the CSV

            for(int r = 0; r < nrows; r++){
                nl = reader.readLine();
                temp = nl.split(",");
                for(int c = 0; c < ncols; c++){
                    if (temp[c + 1].equals("")){
                        drug_matrix[r][c] = 0;
                    }
                    else{
                        drug_matrix[r][c] = Float.parseFloat(temp[c + 1]);
                    }

                }
            }
            reader.close();
            drug_dis = drug_matrix;
        }catch (IOException e){
            e.printStackTrace();
            Log.e("ERROR", "AN ERROR HAS OCCURRED IN LOADFILES (Drug)");
        }



        /*
        //Load and return WeightMatrix
        try{
            String nl;
            String[] temp = null;
            float[][] weight_matrix = new float[nrows][ncols];

            InputStreamReader is = new InputStreamReader(getAssets().open(WeightMatrix));
            BufferedReader reader = new BufferedReader(is);
            nl = reader.readLine(); //skip the first line of the CSV

            for(int r = 0; r < nrows; r++){
                nl = reader.readLine();
                temp = nl.split(",");
                for(int c = 0; c < ncols; c++){
                    weight_matrix[r][c] = Float.parseFloat(temp[c + 1]);
                }
            }
            reader.close();
            return weight_matrix;
        } catch (IOException e){
            Log.d("TESTING", "AN ERROR HAS OCCURRED IN LOADFILES");
            Log.d("TESTING", e.toString());
            return null;
        }


        //Load DiseaseList
        try{
            InputStreamReader is = new InputStreamReader(getAssets().open(DiseaseList));
            BufferedReader reader = new BufferedReader(is);
            String nl;
            String[] temp;
            nl = reader.readLine(); //skips the heading in the csv
            int index = 0;
            while((nl = reader.readLine()) != null){
                temp = nl.split(",");
                DisToUmls.put(temp[1], temp[0]);
                UmlsToDis.put(temp[0], temp[1]);
                index++;
                SearchListItem t = new SearchListItem(0, temp[1]);
                allSymptoms.add(t);
            }
            nrows=index;
            reader.close();
        }catch (IOException e){
            e.printStackTrace();
            Log.e("ERROR", "AN ERROR HAS OCCURRED IN LOADDISEASES");
        }
        //Load WeightMatrix, and indexing diseases and symptoms
        String nl;
        String[] temp = null;
        float[][] weight_matrix = new float[nrows][ncols];

        try{
            InputStreamReader is = new InputStreamReader(getAssets().open(WeightMatrix));
            BufferedReader reader = new BufferedReader(is);
            nl = reader.readLine(); //skip the first line of the CSV

            //Index symptoms
            temp = nl.substring(1,nl.length()).split(",");
            for(int c = 0; c < ncols; c++){
                UmlsToIndex_s.put(temp[c], c);
                IndexToUmls_s.put(c, temp[c]);
            }

            for(int r = 0; r < nrows; r++){
                nl = reader.readLine();
                temp = nl.split(",");
                UmlsToIndex_d.put(temp[0], r);
                IndexToUmls_d.put(r, temp[0]);
                for(int c = 0; c < ncols; c++){
                    weight_matrix[r][c] = Float.parseFloat(temp[c + 1]);
                }
            }
            reader.close();
            return weight_matrix;
        } catch (IOException e){
            Log.d("TESTING", "AN ERROR HAS OCCURRED IN LOAD WM");
            Log.d("TESTING", e.toString());
            return null;
        }

         */
    }

    public void setUpInterface(){
        //Setting up the search view to look up symptoms
        sd = new SearchableDialog(ListSymptoms.this, allSymptoms,"Symptom Search");
        sd.setOnItemSelected(new OnSearchItemSelected(){
            public void onClick(int position, SearchListItem searchListItem){
                String newSmp = searchListItem.getTitle();
                if(!patientSymptoms.contains(newSmp)){
                    patientSymptoms.add(searchListItem.getTitle());
                    ((SymptomAdapter) currentSymptomListView.getAdapter()).notifyDataSetChanged();
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
        adp = new SymptomAdapter(this, patientSymptoms);
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
                intent.putExtra("DisToIndex", DisToIndex);
                intent.putExtra("SympToIndex", SympToIndex);
                intent.putExtra("LabToIndex", LabToIndex);
                intent.putExtra("DrugToIndex", DrugToIndex);
                intent.putExtra("IndexToDis", IndexToDis);
                intent.putExtra("IndexToSymp", IndexToSymp);
                intent.putExtra("IndexToLab", IndexToLab);
                intent.putExtra("IndexToDrug", IndexToDrug);
                intent.putExtra("patient", patient);
                //intent.putExtra("stu", SympToUmls);
                //intent.putExtra("uts", UmlsToSymp);
                //intent.putExtra("dtu", DisToUmls);
                //intent.putExtra("utd", UmlsToDis);
                //intent.putExtra("itud", IndexToUmls_d);
                //intent.putExtra("itus", IndexToUmls_s);
                //intent.putExtra("utid", UmlsToIndex_d);
                //intent.putExtra("utis", UmlsToIndex_s);
                //intent.putExtra("ncols", ncols);
                //intent.putExtra("nrows", nrows);
                startActivity(intent);
            }
        });
    }

    public class SymptomAdapter extends BaseAdapter implements ListAdapter {
        private ArrayList<String> list = new ArrayList<String>();
        private Context context;

        public SymptomAdapter(Context context, ArrayList<String> list) {
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
            TextView listItemText = (TextView)view.findViewById(R.id.list_item_symptom);
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
