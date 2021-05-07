package com.example.gark.dialog;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.gark.MainActivity;
import com.example.gark.R;
import com.example.gark.ResetPasswordActivity;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.UserRepository;

public class UpdateDialog extends DialogFragment implements IRepository {

    private static final String TAG = "updateDialog";

    //widgets
    EditText desET;
    TextView titET;
    TextView mActionOk, mActionCancel;
    ProgressDialog dialogg;
    //VAR
    String title;
    String description;

    public UpdateDialog(String title, String description) {
        this.title = title;
        this.description = description;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.update_dialog, container, false);
        mActionOk = view.findViewById(R.id.action_ok);
        mActionCancel = view.findViewById(R.id.action_cancel);
        desET = view.findViewById(R.id.description);
        desET.setText(description);
        titET = view.findViewById(R.id.title);
        titET.setText(title);
        UserRepository.getInstance().setiRepository(this);
        mActionCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        mActionOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String input = desET.getText().toString();
                if (!input.equals("")) {
                    dialogg = ProgressDialog.show(getContext(), "",getString(R.string.loading), true);
                     String name =getString(R.string.name);
                     String home_town =getString(R.string.home_town);
                     String mobile =getString(R.string.mobile);
                  if(title.equals(name)){
                      String tmp[] = input.split(",",2);
                      MainActivity.getCurrentLoggedInUser().setLastName(tmp[0].toLowerCase());
                      MainActivity.getCurrentLoggedInUser().setFirstName(tmp[1]);
                  }else if(title.equals(home_town)){
                      MainActivity.getCurrentLoggedInUser().setAddress(input);
                  }else {
                      MainActivity.getCurrentLoggedInUser().setPhone(input);
                  }
                  UserRepository.getInstance().updateUser(MainActivity.getCurrentLoggedInUser(),getContext());
                }


            }
        });

        return view;
    }


    @Override
    public void showLoadingButton() {

    }

    @Override
    public void doAction() {
        Toast.makeText(getContext(), getString(R.string.user_update),Toast.LENGTH_SHORT).show();
        getDialog().dismiss();
    }

    @Override
    public void dismissLoadingButton() {
        dialogg.dismiss();
    }
}
