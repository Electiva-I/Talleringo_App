package com.islam.talleringo.fragments;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.islam.talleringo.R;
import com.islam.talleringo.adapters.About_Adapter;
import com.islam.talleringo.models.Develops;

import java.util.ArrayList;
import java.util.List;

public class AboutFragment extends Fragment {
    private RecyclerView.LayoutManager layoutManager;

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
        RecyclerView recyclerViewAbout = view.findViewById(R.id.recyclerViewAbout);

        recyclerViewAbout.setLayoutManager(layoutManager);
        Develops develops01 = new Develops("Julio Vladimir Aviles Platero", "PlateroJulio", "https://firebasestorage.googleapis.com/v0/b/talleringo.appspot.com/o/ProfilePhotos%2Fphoto_julio.jpg?alt=media&token=a22392e6-6b35-43fc-8d9b-8376ececafa8");
        Develops develops02 = new Develops("Josue Emanuel Lopez Zamora", "JoshuaKnight98", "https://firebasestorage.googleapis.com/v0/b/talleringo.appspot.com/o/ProfilePhotos%2Fphoto_emanuel.jpg?alt=media&token=7ed1cff1-bfec-40fd-8919-5e232a5de60e");
        Develops develops03 = new Develops("Osmin Alexis Beltrán Jimenez", "Osmin9930", "https://firebasestorage.googleapis.com/v0/b/talleringo.appspot.com/o/ProfilePhotos%2Fdon_chamba.png?alt=media&token=fbfa20d5-eba8-448b-b8b3-4e30bd8b167e");
        Develops develops04 = new Develops("Joselyn Astrid Aguilar Reyes", "Joselyn166", "https://firebasestorage.googleapis.com/v0/b/talleringo.appspot.com/o/ProfilePhotos%2FMicrosoftTeams-image.png?alt=media&token=867827aa-e0ab-4f58-8e1b-aa49ae45a128");
        Develops develops05 = new Develops("Jhosep Isaac Islam Chachagua", "JhosepIslam", "https://firebasestorage.googleapis.com/v0/b/talleringo.appspot.com/o/ProfilePhotos%2Fphoto_jhosep.jpg?alt=media&token=9d63bc4c-e118-48ee-addd-4a8caaf1935a");
        List<Develops> developsList = new ArrayList<>();
        developsList.add(develops01);
        developsList.add(develops02);
        developsList.add(develops03);
        developsList.add(develops04);
        developsList.add(develops05);

        About_Adapter about_adapter = new About_Adapter(developsList, R.layout.card_view_about, (user, position) -> {
            Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse("https://github.com/" + user));
            startActivity(i);
        });

        recyclerViewAbout.setAdapter(about_adapter);
        recyclerViewAbout.setLayoutManager(layoutManager);
    }
}