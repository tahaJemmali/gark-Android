package com.example.gark.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.InsetDrawable;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.example.gark.R;
import com.example.gark.entites.Challenge;
import com.example.gark.entites.Match;
import com.example.gark.entites.MatchAction;
import com.example.gark.entites.MatchActionType;
import com.example.gark.entites.MatchType;
import com.example.gark.entites.Nationality;
import com.example.gark.entites.Skills;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddMatchActionDialog  extends DialogFragment {

    //widgets
    View view;
    Button confirm;
    Spinner team;
    Spinner player;
    Spinner action_type;
    EditText minutes;
    CircleImageView topPlayerImage;
    ImageView ActionTypeImage;
    //Var
    ArrayList<Skills> playersList;
    Match match;
    Challenge challenge;
    Date date;
    MatchAction matchAction;
    int minutesVal=0;
    private static final String TAG = "AddMatchActionDialog";
    public interface OnInputSelected{
        void sendInput(MatchAction matchAction);
    }
    public OnInputSelected mOnInputSelected;

    public AddMatchActionDialog(Match match, Challenge challenge, Date date) {
        this.match = match;
        this.challenge = challenge;
        this.date = date;
    }

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


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            mOnInputSelected = (OnInputSelected) getActivity();
        }catch (ClassCastException e){
            Log.e(TAG, "onAttach: ClassCastException : " + e.getMessage() );
        }
    }
        boolean validator(){
            if(minutes.getText().toString().isEmpty()){
                Toast.makeText(getContext(),"Please specify when this action happend in the game !",Toast.LENGTH_LONG).show();
                return false;
            }
            try {
                minutesVal=Integer.parseInt(minutes.getText().toString());
            }catch (Exception e){
                Toast.makeText(getContext(),"Please put a valid number in time field !",Toast.LENGTH_LONG).show();
                return false;
            }
            return true;
        }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.add_match_action_dialog, container, false);
        initUI();
        if (getDialog() != null && getDialog().getWindow() != null) {
            ColorDrawable back = new ColorDrawable(Color.TRANSPARENT);
            InsetDrawable inset = new InsetDrawable(back, 20);
            getDialog().getWindow().setBackgroundDrawable(inset);
        }
        return view;
    }
    void initUI() {
         team=view.findViewById(R.id.team);
         player=view.findViewById(R.id.player);
         action_type=view.findViewById(R.id.action_type);
         minutes=view.findViewById(R.id.minutes);
        topPlayerImage=view.findViewById(R.id.topPlayerImage);
        confirm=view.findViewById(R.id.confirm);
        ActionTypeImage=view.findViewById(R.id.ActionTypeImage);
         playersList=new ArrayList<Skills>();

        //Team spinner
        ArrayList<String> teams = new ArrayList<String>();
        teams.add(match.getTeam1().getName());
        teams.add(match.getTeam2().getName());
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, teams);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        team.setAdapter(adapter);
        team.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                playersList.clear();
                if(i==0){
                    playersList.add(match.getTeam1().getCapitaine());
                    playersList.addAll(match.getTeam1().getTitulares());
                    playersList.addAll(match.getTeam1().getSubstitutes());
                }else {
                    playersList.add(match.getTeam2().getCapitaine());
                    playersList.addAll(match.getTeam2().getTitulares());
                    playersList.addAll(match.getTeam2().getSubstitutes());
                }
                setPlayerSpinner(playersList);
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //player spinner
        if(team.getSelectedItem().equals(match.getTeam1().getName())){
            playersList.add(match.getTeam1().getCapitaine());
            playersList.addAll(match.getTeam1().getTitulares());
            playersList.addAll(match.getTeam1().getSubstitutes());
        }else {
            playersList.add(match.getTeam1().getCapitaine());
            playersList.addAll(match.getTeam1().getTitulares());
            playersList.addAll(match.getTeam1().getSubstitutes());
        }
        setPlayerSpinner(playersList);

        player.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (playersList.get(i).getPlayer().getPhoto().equals("Not mentioned")){
                   topPlayerImage.setImageResource(R.drawable.ic_default_user);
                }else {
                    Bitmap bitmap = getBitmapFromString(playersList.get(i).getPlayer().getPhoto());
                    topPlayerImage.setImageBitmap(bitmap); 
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });

        //action type spinner
        ArrayList<String> action_typeArray = new ArrayList<String>();
        action_typeArray.add("Goal");
        action_typeArray.add("Yellow Card");
        action_typeArray.add("Red Card");
        ArrayAdapter<String> adapt = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, action_typeArray);
        adapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        action_type.setAdapter(adapt);
        action_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            switch (i){
                case 0:
                    ActionTypeImage.setImageResource(R.drawable.goal);
                    break;
                case 1:
                    ActionTypeImage.setImageResource(R.drawable.yellow_card);
                    break;
                case 2:
                    ActionTypeImage.setImageResource(R.drawable.red_card);
                    break;
            }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validator()){
                    matchAction=new MatchAction();
                    matchAction.setMatch(match);
                    matchAction.setChallenge(challenge);
                    //Set player
                    matchAction.setPlayer(playersList.get(player.getSelectedItemPosition()));
                    //Set action date
                    Calendar date = dateToCalendar(match.getStart_date());
                    long timeInSecs = date.getTimeInMillis();
                    Date afterAddingMins = new Date(timeInSecs + (minutesVal * 60 * 1000));
                    matchAction.setDate(afterAddingMins);
                    //Set Team
                    if(team.getSelectedItemPosition()==0){
                        matchAction.setTeam(match.getTeam1());
                    }else {
                        matchAction.setTeam(match.getTeam2());
                    }

                    //Set Type
                    switch (action_type.getSelectedItemPosition()){
                        case 0:
                            matchAction.setType(MatchActionType.goal);
                            break;
                        case 1:
                            matchAction.setType(MatchActionType.yellowCard);
                            break;
                        case 2:
                            matchAction.setType(MatchActionType.redCard);
                            break;
                    }

                    Toast.makeText(getContext(),"Match Action added successfully !",Toast.LENGTH_LONG).show();
                    mOnInputSelected.sendInput(matchAction);
                    getDialog().dismiss();
                }
            }
        });
    }
    private Calendar dateToCalendar(Date date) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        return calendar;

    }
    void setPlayerSpinner(ArrayList<Skills> playersList){
        ArrayList<String> players = new ArrayList<String>();
        players.clear();
        for (Skills row : playersList){
            players.add(row.getPlayer().getFirstName()+" "+row.getPlayer().getLastName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_item, players);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        player.setAdapter(adapter);
        if (playersList.get(player.getSelectedItemPosition()).getPlayer().getPhoto().equals("Not mentioned")){
            topPlayerImage.setImageResource(R.drawable.ic_default_user);
        }else {
            Bitmap bitmap = getBitmapFromString(playersList.get(player.getSelectedItemPosition()).getPlayer().getPhoto());
            topPlayerImage.setImageBitmap(bitmap);
        }
    }
    private Bitmap getBitmapFromString(String image) {

        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }
}
