package com.example.gark.fragments;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.databinding.ObservableArrayList;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.gark.MainActivity;
import com.example.gark.R;
import com.example.gark.StoryActivity;
import com.example.gark.Utils.CallBackInterface;
import com.example.gark.adapters.ChallengeAdapter;
import com.example.gark.adapters.PostAdapter;
import com.example.gark.adapters.TeamsAdapter;
import com.example.gark.adapters.TopPlayersAdapter;
import com.example.gark.chat.Chat;
import com.example.gark.chat.ChatActivity;
import com.example.gark.chat.Message;
import com.example.gark.entites.Challenge;
import com.example.gark.entites.Post;
import com.example.gark.entites.Skills;
import com.example.gark.entites.Team;
import com.example.gark.entites.User;
import com.example.gark.repositories.ChallengeRepository;
import com.example.gark.repositories.ChatRepository;
import com.example.gark.repositories.IRepository;
import com.example.gark.repositories.PostRepository;
import com.example.gark.repositories.SkillsRepository;
import com.example.gark.repositories.TeamRepository;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AcceuilFragment#} factory method to
 * create an instance of mContext fragment.
 */
public class AcceuilFragment extends Fragment implements IRepository {
    View view;
    Context mContext;
    CallBackInterface callBackInterface;
    TextView showAllLikedPosts;
    //UI
    RecyclerView recycleViewTopPlayers;
    RecyclerView recycleViewTeams;
    RecyclerView recycleViewPosts;
    RecyclerView recycleViewChallenges;
    ProgressDialog dialogg;
    SwipeRefreshLayout swipe_container;

    //VAR
    public static ArrayList<Skills> players;
    public static ArrayList<Team> teams;
    public static ArrayList<Challenge> challenges;
    public static ArrayList<Post> posts;
    public static ArrayList<Post> topTen;
    public static ArrayList<Chat> chats;
    private static final String COLLECTION_NAME = "messages";
    private final String YOUR_CHANNEL_ID="1";
    private final String YOUR_CHANNEL_NAME="MESSAGE";
    private final String YOUR_NOTIFICATION_CHANNEL_DESCRIPTION="LAST MESSAGES";
    //Adapters
    TopPlayersAdapter topPlayersAdapter;
    TeamsAdapter teamsAdapter;
    PostAdapter postAdapter;
    ChallengeAdapter challengeAdapter;
    int generating = 0;

   //boolean
    boolean generated=false;
    private static final String FRAGMENT_NAME = "acceuil";
    public AcceuilFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view =  inflater.inflate(R.layout.fragment_acceuil, container, false);
        chats=new ObservableArrayList<Chat>();
        mContext = getContext();
        if(!generated) {
            initUI();
            generated = true;
        }else {
            initUIRecycleViewerTopPlayers();
            initUIRecycleViewerPosts();
            initUIRecycleViewChallenges();
            initUIRecycleViewerTopRatedTeams();
        }
        swipe_container=view.findViewById(R.id.swipe_container);
        showAllLikedPosts=view.findViewById(R.id.showAllLikedPosts);
        swipe_container.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                dialogg = ProgressDialog.show(mContext, "",getString(R.string.loading) , true);
                //generated = false;
                loadData();
                swipe_container.setRefreshing(false);
            }
        });

            showAllLikedPosts.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(posts.size()>0){
                    Intent intent = new Intent(getActivity(), StoryActivity.class);
                    intent.putExtra("sizeTen",topTen.size());
                    mContext.startActivity(intent);
                }else {
                    Toast.makeText(mContext, getString(R.string.no_story),Toast.LENGTH_SHORT).show();
                }
                }
            });


        // This callback will only be called when MyFragment is at least Started.
        OnBackPressedCallback callback = new OnBackPressedCallback(true ) {
            @Override
            public void handleOnBackPressed() {
                if (callBackInterface!=null){
                    callBackInterface.popBack();
                }
            }
        };
        requireActivity().getOnBackPressedDispatcher().addCallback(getViewLifecycleOwner(), callback);

        return view;
    }
    void initUI(){
            //\tournement
        dialogg = ProgressDialog.show(mContext, "",getString(R.string.loading), true);
        //posts
        posts=new  ArrayList<Post>();
        topTen=new  ArrayList<Post>();
        ///players
        players=new  ArrayList<Skills>();
        //teams
        teams=new  ArrayList<Team>();
        ///challenges
        challenges=new  ArrayList<Challenge>();
            getAllPlayers();
    }

     void loadData(){
            generating = 0;
            topTen.clear();
            getAllPlayers();
    }
    public void getAllChallenges(){

        ChallengeRepository.getInstance().setiRepository(this);
        ChallengeRepository.getInstance().getAll(mContext,null);
    }
    public void getAllPlayers(){

        SkillsRepository.getInstance().setiRepository(this);
        SkillsRepository.getInstance().getAll(mContext,null);
    }
    public void getAllTeams(){

        TeamRepository.getInstance().setiRepository(this);
        TeamRepository.getInstance().getAll(mContext,null);
    }
    public void getAllPosts(){

        PostRepository.getInstance().setiRepository(this);
        PostRepository.getInstance().getAll(mContext,null);
    }
    private void initUIRecycleViewChallenges() {
        recycleViewChallenges=view.findViewById(R.id.recycleViewChallenges);

        recycleViewChallenges.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        challengeAdapter = new ChallengeAdapter(mContext, challenges);
        recycleViewChallenges.setAdapter(challengeAdapter);
    }

    private void initUIRecycleViewerPosts() {
        recycleViewPosts=view.findViewById(R.id.recycleViewPosts);

        recycleViewPosts.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        postAdapter = new PostAdapter(mContext, topTen);
        recycleViewPosts.setAdapter(postAdapter);
    }

    private void initUIRecycleViewerTopPlayers() {
        recycleViewTopPlayers=view.findViewById(R.id.recycleViewTopPlayers);

        recycleViewTopPlayers.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        topPlayersAdapter = new TopPlayersAdapter(mContext, players);
        recycleViewTopPlayers.setAdapter(topPlayersAdapter);
    }
    private void initUIRecycleViewerTopRatedTeams() {
        recycleViewTeams=view.findViewById(R.id.recycleViewTeams);
        recycleViewTeams.setLayoutManager(new LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false));
        teamsAdapter = new TeamsAdapter(mContext, teams);
        recycleViewTeams.setAdapter(teamsAdapter);
    }

    @Override
    public void showLoadingButton() {
      //  dialogg.show();
    }

    @Override
    public void doAction() {
        switch (generating){
            case 0:
                //////skills
                players=SkillsRepository.getInstance().getList();
                if(players.contains(MainActivity.currentPlayerSkills))
                    players.remove(MainActivity.currentPlayerSkills);
                initUIRecycleViewerTopPlayers();
                generating++;
                getAllPosts();

                break;
            case 1:
                /////posts
                posts= PostRepository.getInstance().getList();
                generating++;
                if (topTen.isEmpty()){
                    int i=0;
                    for (Post row:posts){
                        if(i<5){
                            topTen.add(row);
                            i++;
                        }else {
                            break;
                        }
                    }
                }
                initUIRecycleViewerPosts();
                getAllChallenges();
                break;
            case 2:
                /////challenges
                challenges= ChallengeRepository.getInstance().getList();
                initUIRecycleViewChallenges();
                generating++;
                getAllTeams();
                break;
            case 3:
                /////teams
                teams= TeamRepository.getInstance().getList();
                initUIRecycleViewerTopRatedTeams();
                generating++;
                ChatRepository.getInstance().setiRepository(this);
                ChatRepository.getInstance().getAll(mContext,null);
                break;
            case 4:
                ///Chats
                chats.addAll(ChatRepository.getInstance().getList());
                if(!chats.isEmpty()){
                    for (Chat row:chats) {
                        listenDataChangeMessageRecived(row);
                    }
                }
                dialogg.dismiss();
                break;
        }
    }


    public void listenDataChangeMessageRecived(Chat chat) {
        final CollectionReference docRef = ChatRepository.myFireBaseDB.document(chat.getId()).collection(COLLECTION_NAME);
        docRef.orderBy("dateCreated", Query.Direction.DESCENDING).addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.e("TAG", "Listen failed.", e);
                    return;
                }
                ArrayList<Message> single = (ArrayList<Message>) value.toObjects(Message.class);
                if(chat.getMessages().size()!=single.size()){
                    Log.e("TAG," ,"ACCEUIL FRAGMENT DATA CHANGE: " );
                    chat.getMessages().clear();
                    chat.getMessages().addAll(single);
                    messageNotficiation( single.get(0), chat);
                }
            }
        });
    }

    @Override
    public void dismissLoadingButton() {
        dialogg.dismiss();
    }
    void messageNotficiation(Message message,Chat chat){
        NotificationManager mNotificationManager =
                (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(YOUR_CHANNEL_ID,
                    YOUR_CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_DEFAULT);
            channel.setDescription(YOUR_NOTIFICATION_CHANNEL_DESCRIPTION);
            mNotificationManager.createNotificationChannel(channel);
        }

        User sender = chat.getUser1().getId().equals(message.getreciverId()) ? chat.getUser2() : chat.getUser1();

        NotificationCompat.Builder notification =  new NotificationCompat.Builder(mContext, "CHANNEL_ID")
                .setSmallIcon(R.drawable.ic_baseline_message_24)
                .setContentTitle(sender.getFirstName()+" "+sender.getLastName())
                .setContentText(message.getMessage())
                .setLargeIcon(getBitmapFromString(sender.getPhoto()))
                .setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(message.getMessage()))
                .setAutoCancel(true);
        Intent intent = new Intent(mContext, ChatActivity.class);
        intent.putExtra("chatId",AcceuilFragment.chats.indexOf(chat));
        PendingIntent pi = PendingIntent.getActivity(mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        notification.setContentIntent(pi);
        mNotificationManager.notify(0, notification.build());
    }


    public static Bitmap getBitmapFromString(String image) {

        byte[] bytes = Base64.decode(image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    }

    public void setCallBackInterface(CallBackInterface callBackInterface) {
        this.callBackInterface = callBackInterface;
    }
}