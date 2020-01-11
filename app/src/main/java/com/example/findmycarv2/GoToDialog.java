package com.example.findmycarv2;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

public class GoToDialog extends AppCompatDialogFragment {

    private TextView street;
    private TextView storedDateLocation;
    private View view;

    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        view = inflater.inflate(R.layout.goto_dialog, null);

        street = view.findViewById(R.id.textview_street);
        storedDateLocation = view.findViewById(R.id.textview_storedDateTime);

        street.setText(getArguments().getString("street"));
        storedDateLocation.setText(getArguments().getString("dateTime"));

        builder.setView(view);
        builder.setTitle("Go to your car");
        builder.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        builder.setPositiveButton("Go", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        return builder.create();
    }
}
