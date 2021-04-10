package com.example.mobilehealthprototype;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;


public class Feedback extends AppCompatActivity {
    private static final MediaType FORM_DATA_TYPE = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");

    public void warnError(int input_id, int header_id) {
        TextView header = findViewById(header_id);
        String orig = header.getText().toString();
        String mod_orig = (orig.contains("*")) ? orig : orig + "*";
        header.setText(mod_orig);
        header.setTextColor(getResources().getColor(R.color.errorColor));
//        if(input_id > 0){
//            EditText input = findViewById(input_id);
//            input.setBackgroundColor(getResources().getColor(R.color.transparentRed));
//        }
    }

    public void removeError(int input_id, int header_id) {
        TextView header = findViewById(header_id);
        String orig = header.getText().toString();
        String mod_orig = (orig.contains("*")) ? orig.substring(0, orig.length() - 1) : orig;
        header.setText(mod_orig);
        header.setTextColor(getResources().getColor(R.color.black));
    }

    public String checkValue(int id1, int id2, boolean required) {
        String parsed = ((TextView) findViewById(id1)).getText().toString();
        if (parsed == null || parsed.trim().equals("")) {
            if (required) {
                warnError(id1, id2);
            }
            return "-1";
        } else {
            removeError(id1, id2);
        }
        return parsed;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);


        Button confirmation_button = findViewById(R.id.submit_feedback_button);
        confirmation_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String summary = checkValue(R.id.feedback_input, R.id.feedback_header, true);
                System.out.println("===========");
                System.out.println("summary\n");
                System.out.println(summary);
                System.out.println("===========");
                //send google form
                PostDataTask postDataTask = new PostDataTask();
                postDataTask.execute(summary);


                Intent intent = new Intent(Feedback.this, MainActivity.class);
                startActivity(intent);
            }
        });
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
                postBody = record + "=" + URLEncoder.encode("empty", "UTF-8") +
                        "&" + feedback + "=" + URLEncoder.encode(msg, "UTF-8");
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


            return result;
        }
    }


}
