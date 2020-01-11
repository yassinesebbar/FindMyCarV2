package com.example.findmycarv2;

import androidx.appcompat.app.AppCompatDialogFragment;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class SaveLocationDialog extends AppCompatDialogFragment {

    private TextView street;
    private View view;
    private SaveLocationDialogListener listener;

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

        return builder.create();

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
