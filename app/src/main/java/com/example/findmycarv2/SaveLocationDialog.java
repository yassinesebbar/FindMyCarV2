package com.example.findmycarv2;

import androidx.appcompat.app.AppCompatDialogFragment;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import java.io.File;
import java.io.IOException;
import java.sql.Blob;
import java.text.SimpleDateFormat;
import java.util.Date;

import static android.app.Activity.RESULT_OK;
import static android.os.Environment.getExternalStoragePublicDirectory;

public class SaveLocationDialog extends AppCompatDialogFragment {

    private TextView street;
    private View view;
    private SaveLocationDialogListener listener;
    private ImageView imageView;
    private String pathTofile = "";


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        view = inflater.inflate(R.layout.savelocation_dialog, null);
        street = view.findViewById(R.id.textview_street);
        street.setText(getArguments().getString("street"));
        takeImageSettings();

        builder.setView(view);
        builder.setTitle("Save your location");
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.saveLocation(pathTofile);
            }
        });

        builder.setNeutralButton("Take Photo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {


            }
        });


        Dialog dialog = builder.create();


        dialog.setOnShowListener(new DialogInterface.OnShowListener() {

            @Override
            public void onShow(DialogInterface dialog) {

                Button button = ((AlertDialog) dialog).getButton(AlertDialog.BUTTON_NEUTRAL);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        dispatchPictureTakeAction();
                    }
                });
            }
        });

        return dialog;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            listener = (SaveLocationDialogListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString() + "must implement saveLocationDialogInterface");
        }
    }

    public interface SaveLocationDialogListener{
        void saveLocation(String pathTofile);
    }

    public void takeImageSettings(){
        if(Build.VERSION.SDK_INT >= 23){
            requestPermissions(new String[] {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, 2);
        }

        imageView = view.findViewById(R.id.imageView);
    }


    private void dispatchPictureTakeAction() {

        Intent takePic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if(takePic.resolveActivity(getActivity().getPackageManager()) != null){
            File photoFile = null;
            photoFile = createPhotoFile();

            if(photoFile != null){
                pathTofile = photoFile.getAbsolutePath();
                Uri photoUri = FileProvider.getUriForFile(this.getContext(), "com.example.findmycarv2.fileprovider", photoFile);
                takePic.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePic, 1);
            }
        }
    }

    private File createPhotoFile() {

        File image = null;
        String name = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File storageDir = getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        try {
            image = File.createTempFile(name, "jpg", storageDir);
        } catch (IOException e) {
            Log.d("myLog", "Excep " + e.toString());
        }

        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(resultCode == RESULT_OK){
            if(requestCode == 1){

                Bitmap bitmap = BitmapFactory.decodeFile(pathTofile);
                imageView.setImageBitmap(bitmap);

            }

        }
    }

}
