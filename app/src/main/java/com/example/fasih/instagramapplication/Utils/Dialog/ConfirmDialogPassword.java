package com.example.fasih.instagramapplication.Utils.Dialog;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.example.fasih.instagramapplication.R;
import com.example.fasih.instagramapplication.Utils.Models.MessageEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Fasih on 10/24/18.
 */

public class ConfirmDialogPassword extends DialogFragment {
    private EditText confirmPasswordText;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_confirm_password, container, false);
        setupDialogWidgets(view);
        return view;
    }

    private void setupDialogWidgets(View view) {
        TextView confirmPassword = view.findViewById(R.id.dialog_confirm);
        TextView dialog_cancel = view.findViewById(R.id.dialog_cancel);
        confirmPasswordText = view.findViewById(R.id.confirm_password);
        //Listeners
        dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        confirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new MessageEvent(confirmPasswordText.getText().toString()));
                dismiss();
            }
        });
    }

}
