package com.example.mobilehealthprototype;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ajithvgiri.searchdialog.SearchListItem;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;


public class ConfirmationScreen extends AppCompatActivity {
    private static final MediaType FORM_DATA_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    Intent passedIntent;
    String p_sex;
    String p_id;
    int p_age;
    float p_height, p_weight;
    PatientInfo patient;

    int diagnosed_disease_index;
    String diagnosed_disease;
    Float diagnosed_disease_prob;

    List<SearchListItem> allDiseases = new ArrayList<>();
    ArrayList<String> patientSymptoms;

    Hashtable<String, String> SympToUmls= new Hashtable<String, String>();
    Hashtable<String, String> UmlsToSymp= new Hashtable<String, String>();
    Hashtable<Integer, String> IndexToUmls_s = new Hashtable<Integer, String>();
    Hashtable<String, Integer> UmlsToIndex_s = new Hashtable<String, Integer>();
    Hashtable<String, Integer> UmlsToIndex_d = new Hashtable<String, Integer>();
    Hashtable<Integer, String> IndexToUmls_d = new Hashtable<Integer, String>();
    Hashtable<String, String> DisToUmls = new Hashtable<String, String>();
    Hashtable<String,String> UmlsToDis = new Hashtable<String,String>();



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirmation_screen);

        handlePassedIntent();
        String summary = constructConfirmationDetails();
        TextView text = findViewById(R.id.textView5);
        text.setText(summary);
        PatientViewModel patientViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(this.getApplication())).get(PatientViewModel.class);

        Button confirmation_button = (Button) findViewById(R.id.final_confirmation);
        confirmation_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                CommunicationHandler ch = new CommunicationHandler();

                ArrayList<Integer> tmp = new ArrayList<Integer>();

                for(int i = 0; i < patientSymptoms.size(); i++){
                    tmp.add(UmlsToIndex_s.get(SympToUmls.get(patientSymptoms.get(i))));
                    //new Integer(UmlsToIndex.get(SympToUmls.get(patientSymptoms.get(i))))
                }


                //String toSend = ch.generateRawMessage(p_id, p_sex, p_age, p_height, p_weight, tmp, diagnosed_disease_index);
                // sendMessage deactivated
                // sendMessage(getString(R.string.server_number),toSend); //Check if this is working later

                //save to database
                patient.symptoms = String.valueOf(patientSymptoms);
                patient.diagnosis = diagnosed_disease;
                patientViewModel.insert(patient);

                //send google form
                PostDataTask postDataTask = new PostDataTask();

                //execute asynctask
                postDataTask.execute(summary);

                //saveFile(toSend);
                //readFile();


                Intent intent = new Intent(ConfirmationScreen.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void readFile() {
        try {
            FileInputStream fileInputStream = openFileInput("cache_text");
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(fileInputStream));
            String line = bufferedReader.readLine();
            while (line != null) {
                System.out.println("file contents:");
                System.out.println(line);
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            fileInputStream.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void saveFile(String toSend) {
        try {
            FileOutputStream fileOutputStream = openFileOutput("cache_text", MODE_APPEND);
            PrintWriter writer = new PrintWriter(new OutputStreamWriter(fileOutputStream));
            writer.write("test" + toSend+"\n");
            writer.flush();
            writer.close();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public String constructConfirmationDetails(){
        String spid = p_id;
        String spage = Integer.toString(p_age);
        String spsex = (p_sex == "M") ? "M" : "F";
        String spheight = Float.toString(p_height);
        String spweight = Float.toString(p_weight);

        String sddprob = Float.toString(diagnosed_disease_prob);

        String fp = "Patient id:" + spid + "\n" + "Patient age:" + spage + "\n";
        String sp = fp + "Patient Symptoms::" + "\n";
        for(int i = 0; i < patientSymptoms.size(); i++){
            sp = sp + patientSymptoms.get(i) + "\n";
        }
        String tp = sp + "Doctor Diagnosis::" + "\n" + diagnosed_disease + "\n" + "with probability: " + sddprob;
        return tp;
    }

    public void handlePassedIntent(){
        passedIntent = getIntent();
        patient = passedIntent.getParcelableExtra("patient");
        p_sex = (String) passedIntent.getSerializableExtra("sex");
        p_id = passedIntent.getStringExtra("hid");
        p_age = passedIntent.getIntExtra("age", -1);
        p_height = passedIntent.getFloatExtra("height",-1);
        p_weight = passedIntent.getFloatExtra("weight",-1);
        patientSymptoms = passedIntent.getStringArrayListExtra("patient_symptoms");

        SympToUmls = new Hashtable<> ((HashMap<String,String>) passedIntent.getSerializableExtra("stu"));
        UmlsToSymp = new Hashtable<>((HashMap<String,String>) passedIntent.getSerializableExtra("uts"));
        IndexToUmls_s = new Hashtable<>((HashMap<Integer, String>) passedIntent.getSerializableExtra("itus"));
        UmlsToIndex_s = new Hashtable<>((HashMap<String, Integer>) passedIntent.getSerializableExtra("utis"));

        DisToUmls = new Hashtable<> ((HashMap<String,String>) passedIntent.getSerializableExtra("dtu"));
        UmlsToDis = new Hashtable<>((HashMap<String,String>) passedIntent.getSerializableExtra("utd"));
        IndexToUmls_d = new Hashtable<>((HashMap<Integer, String>) passedIntent.getSerializableExtra("itud"));
        UmlsToIndex_d = new Hashtable<>((HashMap<String, Integer>) passedIntent.getSerializableExtra("utid"));

        diagnosed_disease = passedIntent.getStringExtra("diagnosed_disease_name");
        diagnosed_disease_prob = passedIntent.getFloatExtra("likelihood_of_disease", -1f);
        diagnosed_disease_index = passedIntent.getIntExtra("diagnosed_disease_index", -1);
    }

    private void sendMessage(String phone_num, String msg){
        SmsManager sm = SmsManager.getDefault();
        sm.sendTextMessage(phone_num, null, msg, null, null);
    }

    private class PostDataTask extends AsyncTask<String, Void, Boolean> {

        @Override
        protected Boolean doInBackground(String... data) {
            String msg = data[0];
            Boolean result = true;
            // String url = "@strings/google_form_url";
            String url = "https://docs.google.com/forms/d/e/1FAIpQLSe3Vuyr0WWWlc2MIKAq_aw8uYeQCdqg2dV_4B7TgcP5Aw2wpg/formResponse";
            String record = "entry.519325928";
            String feedback = "entry.1481931525";
            String postBody = "";
            try {
                //all values must be URL encoded to make sure that special characters like & | ",etc.
                //do not cause problems
                postBody = record + "=" + URLEncoder.encode(msg, "UTF-8") +
                        "&" + feedback + "=" + URLEncoder.encode("empty", "UTF-8");
            } catch (UnsupportedEncodingException ex) {
                result = false;
            }
            try {
                //Create OkHttpClient for sending request
                OkHttpClient client = new OkHttpClient();
                //Create the request body with the help of Media Type
                RequestBody body = RequestBody.create(FORM_DATA_TYPE, postBody);
                Request request = new Request.Builder()
                        .url(url)
                        .post(body)
                        .build();
                //Send the request
                Response response = client.newCall(request).execute();
            } catch (IOException exception) {
                result = false;
            }
            System.out.println("===========");
            System.out.println("sending result");
            System.out.println(result);
            System.out.println(msg);
            System.out.println(postBody);
            System.out.println("===========");


            return result;
        }
    }


}
