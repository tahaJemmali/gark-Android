package com.example.gark.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.gark.AddChallengeActivity;
import com.example.gark.R;
import com.example.gark.Utils.CallBackInterface;
import com.example.gark.adapters.ChallengeAdapterListe;
import com.example.gark.entites.Challenge;
import com.example.gark.entites.ChallengeType;
import com.example.gark.repositories.ChallengeRepository;
import com.example.gark.repositories.IRepository;


import java.util.ArrayList;

public class ChallengeFragment extends Fragment implements IRepository{
    View view;
    Context mContext;
    CallBackInterface callBackInterface;
    ProgressDialog dialogg;
    ArrayList<Challenge> challenges;
    ArrayList<Challenge> challengesAcademy;
    ArrayList<Challenge> challengesAmateur;
    ArrayList<Challenge> challengesStudents;
    ArrayList<Challenge> searchChallenges;
    //UI
    RecyclerView recycleViewChallenges;
    ChallengeAdapterListe challengeAdapter;
    ImageButton addChallenge;
    RadioButton buttonAcademy;
    RadioButton buttonAmateur;
    RadioButton buttonStudents;
    TextView search_bar_challenges;
    TextView challengNumber;


    private static final String FRAGMENT_NAME = "challenge";
    public ChallengeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_challenge, container, false);
        mContext = getContext();
        initUI();

        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                System.out.println("on back pressed from frag");
                if (callBackInterface!=null){
                    callBackInterface.popBack();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(this, callback);

        return view;
    }

    void initUI(){
        challengesAcademy = new ArrayList<Challenge> ();
        challengesAmateur = new ArrayList<Challenge> ();
        challengesStudents = new ArrayList<Challenge> ();
        searchChallenges = new ArrayList<Challenge> ();
        recycleViewChallenges=view.findViewById(R.id.recycleViewChallenges);
        addChallenge=view.findViewById(R.id.addChallenge);
        challengNumber=view.findViewById(R.id.challengNumber);
/*        if(MainActivity.getCurrentLoggedInUser().getRole().equals("player"))
            addChallenge.setVisibility(View.INVISIBLE);*/

        addChallenge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupForm();
            }
        });

         buttonAcademy=view.findViewById(R.id.buttonAcademy);
        buttonAcademy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                challengesAcademy.clear();
                search_bar_challenges.setText("");
                for(Challenge row : challenges){
                    if(row.getType().equals(ChallengeType.Academy))
                        challengesAcademy.add(row);

                }
                initUIRecycleViewerChallenges(challengesAcademy);
            }
        });
         buttonAmateur=view.findViewById(R.id.buttonAmateur);
         buttonAmateur.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 challengesAmateur.clear();
                search_bar_challenges.setText("");
                 for(Challenge row : challenges){
                     if(row.getType().equals(ChallengeType.Amateur))
                         challengesAmateur.add(row);

                 }
                 initUIRecycleViewerChallenges(challengesAmateur);
             }
         });
         buttonStudents=view.findViewById(R.id.buttonStudents);
         buttonStudents.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 challengesStudents.clear();
                 search_bar_challenges.setText("");
                 for(Challenge row : challenges){
                     if(row.getType().equals(ChallengeType.Students))
                         challengesStudents.add(row);

                 }
                 initUIRecycleViewerChallenges(challengesStudents);
             }
         });

         search_bar_challenges=view.findViewById(R.id.search_bar_challenges);
        search_bar_challenges.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                find(charSequence);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        reloadData();
    }


    void popupForm(){
        Intent intent =new Intent(mContext, AddChallengeActivity.class);
        startActivity(intent);
    }


    void find(CharSequence charSequence){
        if(!charSequence.toString().isEmpty()){
            buttonAcademy.setChecked(false);
            buttonStudents.setChecked(false);
            buttonAmateur.setChecked(false);
        }

        searchChallenges.clear();
            for (Challenge row:challenges){
                if (row.getName().contains(charSequence)){
                    searchChallenges.add(row);
                }
            }
            initUIRecycleViewerChallenges(searchChallenges);
    }

    void reloadData(){
        dialogg = ProgressDialog.show(mContext, "","Loading" , true);
        challenges=new  ArrayList<Challenge>();
        ChallengeRepository.getInstance().setiRepository(this);
        ChallengeRepository.getInstance().getAll(mContext,null);
    }

    @Override
    public void showLoadingButton() {
        dialogg.show();
    }

    @Override
    public void doAction() {
        /////challenges
        challenges= ChallengeRepository.getInstance().getList();
        initUIRecycleViewerChallenges(challenges);
    }

    @Override
    public void dismissLoadingButton() {
    dialogg.dismiss();
    }
    public void setCallBackInterface(CallBackInterface callBackInterface) {
        this.callBackInterface = callBackInterface;
    }
    private void initUIRecycleViewerChallenges(ArrayList<Challenge> challenges) {
        challengNumber.setText(challenges.size()+" challenges");
        recycleViewChallenges.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        challengeAdapter = new ChallengeAdapterListe(mContext, challenges);
        recycleViewChallenges.setAdapter(challengeAdapter);
    }
}