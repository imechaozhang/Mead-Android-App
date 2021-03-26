package com.example.mobilehealthprototype;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_CODE = 1;
    boolean diagnose_enable = true;
    boolean outbreak_enabled = false;
    boolean query_enabled = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //askPermissions();
        setUpInterface();
    }

    public void askPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_DENIED) {
                String[] permissions = {Manifest.permission.SEND_SMS};
                requestPermissions(permissions, PERMISSION_REQUEST_CODE);
            }
        }
    }

    public void checkPermissions(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            diagnose_enable = checkSelfPermission(Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED;
        }
    }

    public void setUpInterface(){
        Button diagnose = findViewById(R.id.diagnosisButton);
        diagnose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //checkPermissions();
                // diagnose_enable = true;
                //AlertDialog.Builder bd = buildWarning(R.string.permissions_warning_title,
                //                                   R.string.permissions_warning_message, R.string.button_close);
                // if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                //    if (checkSelfPermission(Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                //        bd.show();
                //    }
                // }
                startActivity(new Intent(MainActivity.this, PatientInfoActivity.class));
            }
        });


        Button query_pinfo = findViewById(R.id.queryButton);
        if(!query_enabled){
            query_pinfo.setEnabled(query_enabled);
            CustomButton.changeButtonColor(this, query_pinfo, R.color.disabled_gray, 3, R.color.disabled_gray_accent);
        } else {
            query_pinfo.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this, PatientQuery.class);
                    startActivity(i);
                }
            });
        }

        Button feedback = findViewById(R.id.feedback);
        feedback.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, Feedback.class);
                startActivity(i);
            }
        });

 /*        //TODO Actually configure this "OUTBREAK" button & activities
        Button outbreak = findViewById(R.id.outbreakButton);
        if(!outbreak_enabled){
            //TODO: delete line below once we're ready to create an outbreak tracking activity
            outbreak.setEnabled(outbreak_enabled);
            CustomButton.changeButtonColor(this, outbreak, R.color.disabled_gray, 3, R.color.disabled_gray_accent);
        }else{
            outbreak.setOnClickListener(new View.OnClickListener(){
                public void onClick(View view) {
                    Intent i = new Intent(MainActivity.this, OutbreakScreens.class);
                    startActivity(i);
                }
            });
        }
*/
    }

    public AlertDialog.Builder buildWarning(int title_id, int message_id, int pb){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setTitle(title_id);
        builder.setMessage(message_id);
        builder.setPositiveButton(pb, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                //do nothing
            }
        });
        return builder;
    }
}
