package com.example.mobilehealthprototype;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PatientListAdapter extends RecyclerView.Adapter<PatientListAdapter.PatientViewHolder> {

    class PatientViewHolder extends RecyclerView.ViewHolder {
        private final TextView patientItemView;

        private PatientViewHolder(View itemView) {
            super(itemView);
            patientItemView = itemView.findViewById(R.id.databaseView);
        }
    }

    private final LayoutInflater mInflater;
    private List<PatientInfo> patientInfo; // Cached copy of words

    PatientListAdapter(Context context) { mInflater = LayoutInflater.from(context); }

    @Override
    public PatientViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.activity_patient_query, parent, false);
        return new PatientViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PatientViewHolder holder, int position) {
        if (patientInfo != null) {
            PatientInfo current = patientInfo.get(position);
            holder.patientItemView.setText(current.id + current.name);
        } else {
            // Covers the case of data not being ready yet.
            holder.patientItemView.setText("Not found");
        }
    }

    void setvalue(List<PatientInfo> patients){
        patientInfo = patients;
        notifyDataSetChanged();
    }

    // getItemCount() is called many times, and when it is first called,
    // mWords has not been updated (means initially, it's null, and we can't return null).
    @Override
    public int getItemCount() {
        if (patientInfo != null)
            return patientInfo.size();
        else return 0;
    }
}
