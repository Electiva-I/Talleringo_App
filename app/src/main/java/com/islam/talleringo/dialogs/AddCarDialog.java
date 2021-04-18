package com.islam.talleringo.dialogs;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.DialogFragment;

import com.islam.talleringo.R;
import com.islam.talleringo.activities.MainActivity;
import com.islam.talleringo.utils.utils;
import com.bumptech.glide.Glide;

import java.io.File;

public class AddCarDialog extends DialogFragment {
    private ImageButton btnImg;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_add_car,container, true);
        btnImg  = view.findViewById(R.id.btnImg);
        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        return dialog;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        btnImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent pictureIntent = new Intent("android.media.action.IMAGE_CAPTURE");


                if (CheckPermission(Manifest.permission.CAMERA)){

                    startActivityForResult(pictureIntent, utils.CAMERA_REQUEST_CODE);
                } else {
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, utils.CAMERA_REQUEST_CODE);
                }
            }
        });
    }

    private  boolean CheckPermission(String permission){
        int result = getActivity().checkCallingOrSelfPermission(permission);
        return  result == PackageManager.PERMISSION_GRANTED;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case  utils.CAMERA_REQUEST_CODE:
                String result = data.toUri(0);

                break;
        }
    }


}
