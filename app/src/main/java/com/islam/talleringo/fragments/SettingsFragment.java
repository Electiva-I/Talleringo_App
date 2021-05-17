package com.islam.talleringo.fragments;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.islam.talleringo.R;
import com.islam.talleringo.notifications.NotificationHandler;

import java.util.Calendar;

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

        Toast.makeText(getContext(), "asda", Toast.LENGTH_LONG).show();
        NotificationHandler handler = new NotificationHandler(getContext());
        Notification.Builder builder = handler.createNotificationBuilder("Prueba "+ counter, "Notificacion de prueba", true);
        ++counter;
            handler.getNotificationManager().notify(counter, builder.build());
            handler.publishNotificationSummaryGroup(true);


    }

}