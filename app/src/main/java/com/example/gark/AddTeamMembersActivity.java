package com.example.gark;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.gark.adapters.CommunityTopPlayerAdapter;
import com.example.gark.adapters.InviteTeamAdapter;
import com.example.gark.entites.Skills;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.SkillsRepository;

import java.util.ArrayList;

public class AddTeamMembersActivity extends AppCompatActivity implements IRepository {
//UI
    EditText search_bar_invitation;
RecyclerView inviteTeamMembers;
InviteTeamAdapter inviteTeamAdapter;
    ProgressDialog dialogg;
//Var
private ArrayList<Skills> players;
    private ArrayList<Skills> searchPlayers;
private ArrayList<Skills> invitedPlayers;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_team_members);
        initUI();
    }
    void initUI(){
        dialogg = ProgressDialog.show(this, "", "Loading Data ..Wait..", true);
        search_bar_invitation=findViewById(R.id.search_bar_invitation);
        inviteTeamMembers=findViewById(R.id.inviteTeamMembers);
        searchPlayers=new ArrayList<Skills>();
        SkillsRepository.getInstance().setiRepository(this);
        SkillsRepository.getInstance().getAll(this,null);
        search_bar_invitation.addTextChangedListener(new TextWatcher() {
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
    }
    void find(CharSequence charSequence){
        searchPlayers.clear();
        for (Skills row:players){
            if (row.getPlayer().getFirstName().contains(charSequence)||row.getPlayer().getLastName().contains(charSequence)){
                searchPlayers.add(row);
            }
        }
        inviteTeamAdapter = new InviteTeamAdapter(this, searchPlayers);
        inviteTeamMembers.setAdapter(inviteTeamAdapter);
    }
    private void initUIRecycleViewer() {
        inviteTeamMembers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        inviteTeamAdapter = new InviteTeamAdapter(this, players);
        inviteTeamMembers.setAdapter(inviteTeamAdapter);
    }

    public void invite(View view) {
        Intent returnIntent = new Intent();
        invitedPlayers= inviteTeamAdapter.returnSelectedMemembers();
        returnIntent.putExtra("result",invitedPlayers);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }

    @Override
    public void showLoadingButton() {
        dialogg.show();
    }

    @Override
    public void doAction() {
        players= SkillsRepository.getInstance().getList();
        for (Skills row:players){
            if (row.getPlayer().getId().equals(MainActivity.getCurrentLoggedInUser().getId())){
                Log.e("TAG", "doAction: "+"done" );
                players.remove(row);
            }
        }
        initUIRecycleViewer();
    }

    @Override
    public void dismissLoadingButton() {
    dialogg.dismiss();
    }
}