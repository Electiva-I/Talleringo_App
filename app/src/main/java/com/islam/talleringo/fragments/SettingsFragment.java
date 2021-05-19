package com.islam.talleringo.fragments;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.work.Data;
import androidx.work.WorkManager;

import com.islam.talleringo.R;

import java.util.Calendar;
import java.util.Date;
import java.util.UUID;

public class SettingsFragment extends Fragment {

    TextView hora;
    Button save;
    int hour, minute;

    public SettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_settings, container, false);
        hora = view.findViewById(R.id.hora_text);
        save = view.findViewById(R.id.btn_save);

        save.setOnClickListener(view12 -> sendTestNotification());

        hora.setOnClickListener(view1 -> {
            TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), (timePicker, i, i1) -> {
                hour = i;
                minute = i1;
                Calendar calendar = Calendar.getInstance();
                calendar.set(0, 0, 0, hour, minute);
                hora.setText(DateFormat.format("hh:mm aa",calendar));
            }, 12, 0, false
            );
            timePickerDialog.updateTime(hour, minute);
            timePickerDialog.show();
        });

        return view;
    }
    int counter = 0;
    private void sendTestNotification(){

        Log.d("alertTIme", " "+new Date("05/17/2021  08:30:00"));
        Log.d("alertTIme", "SYSTEM "+System.currentTimeMillis()+" "+ new Date());


        WorkManager.getInstance(getContext()).cancelAllWork();
        //WorkManager.getInstance(getContext()).cancelAllWorkByTag("tag");
    }

    public String getUID (){
        return  UUID.randomUUID().toString();
    }

    private Data setData(String ti, String de, int id){
        return new Data.Builder()
                .putString("titulo", ti)
                .putString("detalle", de)
                .putInt("id", id).build();
    }
}