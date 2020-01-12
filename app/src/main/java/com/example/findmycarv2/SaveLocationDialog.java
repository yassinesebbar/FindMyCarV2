package com.example.findmycarv2;

import androidx.appcompat.app.AppCompatDialogFragment;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.sql.Blob;

public class SaveLocationDialog extends AppCompatDialogFragment {

    private TextView street;
    private View view;
    private SaveLocationDialogListener listener;
    private Blob image;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.savelocation_dialog, null);

        street = view.findViewById(R.id.textview_street);
        street.setText(getArguments().getString("street"));

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
                listener.saveLocation();
            }
        });

        builder.setNeutralButton("Take Photo", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
//                listener.saveLocation();
                dialog.dismiss();

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
        void saveLocation();
    }




}
