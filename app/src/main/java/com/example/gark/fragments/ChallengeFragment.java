package com.example.gark.fragments;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.gark.MainActivity;
import com.example.gark.R;
import com.example.gark.Utils.CallBackInterface;
import com.example.gark.adapters.ChallengeAdapter;
import com.example.gark.adapters.ChallengeAdapterListe;
import com.example.gark.adapters.CommunityTopPlayerAdapter;
import com.example.gark.repositories.IRepository;

public class ChallengeFragment extends Fragment implements IRepository {
    View view;
    Context mContext;
    CallBackInterface callBackInterface;
    ProgressDialog dialogg;
    //UI
    RecyclerView recycleViewChallenges;
    ChallengeAdapterListe challengeAdapter;

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
        recycleViewChallenges=view.findViewById(R.id.recycleViewChallenges);
        initUIRecycleViewerChallenges();
    }

    @Override
    public void showLoadingButton() {

    }

    @Override
    public void doAction() {

    }

    @Override
    public void dismissLoadingButton() {

    }
    public void setCallBackInterface(CallBackInterface callBackInterface) {
        this.callBackInterface = callBackInterface;
    }
    private void initUIRecycleViewerChallenges() {
        recycleViewChallenges.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.VERTICAL, false));
        challengeAdapter = new ChallengeAdapterListe(mContext, AcceuilFragment.challenges);
        recycleViewChallenges.setAdapter(challengeAdapter);
    }
}