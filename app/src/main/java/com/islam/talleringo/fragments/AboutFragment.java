package com.islam.talleringo.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.islam.talleringo.R;
import com.islam.talleringo.adapters.About_Adapter;
import com.islam.talleringo.adapters.Vehicles_Adapter;
import com.islam.talleringo.database.Vehicles.Vehicle;
import com.islam.talleringo.models.Develops;

import java.util.ArrayList;
import java.util.List;

public class AboutFragment extends Fragment {
    private RecyclerView recyclerViewAbout;
    private About_Adapter about_adapter;
    private RecyclerView.LayoutManager layoutManager;
    private List<Develops> developsList;

    public AboutFragment() {
        // Required empty public constructor
    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);
        layoutManager = new LinearLayoutManager(getActivity());
        initAbout(view);
        return view;
    }

    private void initAbout(View view) {
        recyclerViewAbout = view.findViewById(R.id.recyclerViewAbout);

        recyclerViewAbout.setLayoutManager(layoutManager);
        Develops platero = new Develops("Julio Vladimir Aviles Platero", "PlateroJulio", "https://firebasestorage.googleapis.com/v0/b/talleringo.appspot.com/o/ProfilePhotos%2Fphoto_julio.jpg?alt=media&token=a22392e6-6b35-43fc-8d9b-8376ececafa8");
        Develops emanuel = new Develops("Josue Emanuel Lopez Zamora", "JoshuaKnight98", "https://firebasestorage.googleapis.com/v0/b/talleringo.appspot.com/o/ProfilePhotos%2Fphoto_emanuel.jpg?alt=media&token=7ed1cff1-bfec-40fd-8919-5e232a5de60e");
        Develops osmin = new Develops("Osmin Alexis Beltrán Jimenez", "Osmin9930", "https://firebasestorage.googleapis.com/v0/b/talleringo.appspot.com/o/ProfilePhotos%2Fdon_chamba.png?alt=media&token=fbfa20d5-eba8-448b-b8b3-4e30bd8b167e");
        Develops joselyn = new Develops("Joselyn Astrid Aguilar Reyes", "Joselyn166", "https://firebasestorage.googleapis.com/v0/b/talleringo.appspot.com/o/ProfilePhotos%2FMicrosoftTeams-image.png?alt=media&token=867827aa-e0ab-4f58-8e1b-aa49ae45a128");
        Develops yo = new Develops("Jhosep Isaac Islam Chachagua", "JhosepIslam", "https://firebasestorage.googleapis.com/v0/b/talleringo.appspot.com/o/ProfilePhotos%2Fphoto_jhosep.jpg?alt=media&token=9d63bc4c-e118-48ee-addd-4a8caaf1935a");
        developsList = new ArrayList<>();
        developsList.add(platero);
        developsList.add(emanuel);
        developsList.add(osmin);
        developsList.add(joselyn);
        developsList.add(yo);

        about_adapter = new About_Adapter(developsList, R.layout.card_view_about, new About_Adapter.OnItemClickListener() {
            @Override
            public void OnItemClick(String user, int position) {
                Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/"+user));
                startActivity(i);
            }
        });

        recyclerViewAbout.setAdapter(about_adapter);
        recyclerViewAbout.setLayoutManager(layoutManager);
    }
}