package com.example.gark.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.gark.R;
import com.example.gark.adapters.AddMatchAdapter;
import com.example.gark.entites.Match;
import com.example.gark.entites.Team;

import java.util.Calendar;
import java.util.Date;

public class AddMatchDialog  extends DialogFragment {

    //widgets
    View view;
    DatePicker startDate;
    TimePicker timePicker;
    Button confirm;
    //Var
    Date date;
    private static final String TAG = "AddMatchDialog";
    public interface OnInputSelected{
        void sendInput();
    }
    public OnInputSelected mOnInputSelected;
    @Override
    public void onStart()
    {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null)
        {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.WRAP_CONTENT;
            dialog.getWindow().setLayout(width, height);
        }
    }
    void setMatch(){
        AddMatchAdapter.selectedMatch.setStart_date(date);
    }

    public Date getDateFromDatePicker(DatePicker datePicker){
        int day = datePicker.getDayOfMonth();
        int month = datePicker.getMonth();
        int year =  datePicker.getYear();

        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        return calendar.getTime();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnInputSelected = (OnInputSelected) getContext();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException : " + e.getMessage() );
        }
    }
    Boolean validator(){
        date = getDateFromDatePicker(startDate);
        date.setHours(timePicker.getCurrentHour());
        date.setMinutes(timePicker.getMinute());
        if(date.before(new Date())){
            Toast.makeText(getContext(),"You can't give a match a date before current date !",Toast.LENGTH_LONG).show();
            return  false;
        }
        return true;
    }
    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_team_dialog, container, false);
        initUI();
        if (getDialog() != null && getDialog().getWindow() != null) {
            ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
            InsetDrawable inset = new InsetDrawable(back, 20);
            getDialog().getWindow().setBackgroundDrawable(inset);
        }
        return view;
    }
    void initUI() {
         startDate=view.findViewById(R.id.startDate);
        timePicker=view.findViewById(R.id.timePicker);
        confirm=view.findViewById(R.id.confirm);
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validator()){
                    setMatch();
                    Toast.makeText(getContext(),"Match date added successfully !",Toast.LENGTH_LONG).show();
                    mOnInputSelected.sendInput();
                    getDialog().dismiss();
                }

            }
        });
    }
}
