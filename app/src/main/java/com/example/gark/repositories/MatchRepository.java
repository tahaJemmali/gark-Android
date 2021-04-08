package com.example.gark.repositories;

import android.app.ProgressDialog;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.gark.MainActivity;
import com.example.gark.Utils.VolleyInstance;
import com.example.gark.entites.Challenge;
import com.example.gark.entites.ChallengeState;
import com.example.gark.entites.ChallengeType;
import com.example.gark.entites.Match;
import com.example.gark.entites.MatchAction;
import com.example.gark.entites.MatchType;
import com.example.gark.entites.Team;
import com.example.gark.entites.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MatchRepository {
    private IRepository iRepository;
    private static MatchRepository instance;
    public static ArrayList<Match> matches;
    public static  Match match;
    public static int generatorMatches=0;
    public static int generator=0;

    public static MatchRepository getInstance() {
        if (instance==null){
            instance = new MatchRepository();
        }
        return instance;
    }


    public void setiRepository(IRepository iRepository) {
        this.iRepository = iRepository;
    }



    public void generateMatch(Context mcontext, Match match, Challenge challenge) {

        final String url = iRepository.baseURL + "/add_match";
        JSONObject object = new JSONObject();
        convertObjectToJson(object,match);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("TAG", "add match message: "+response.getString("message"));
                            String id= response.getString("message");
                            generatorMatches++;
                            ChallengeRepository.getInstance().addMatchToChallenge(mcontext,challenge.getId(),id);
                            iRepository.doAction();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "fail: "+error);
            }
        });
        VolleyInstance.getInstance(mcontext).addToRequestQueue(request);
    }

    public void add(Context mcontext, Match match, ProgressDialog dialog) {

    }


    public void delete(Context mcontext, String id, ProgressDialog dialog) {

    }

    public void update(Context mcontext, Match match, String id,Challenge challenge) {
        iRepository.showLoadingButton();
        final String url=iRepository.baseURL  + "/update_match/"+id;
        this.match=match;
        JSONObject object = new JSONObject();
        convertObjectToJson(object,match);
        JsonObjectRequest request = new  JsonObjectRequest(Request.Method.PUT, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                    generator++;
                    iRepository.doAction();
                    iRepository.dismissLoadingButton();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("TAG", "onResponse: "+url);
            }
        });
        VolleyInstance.getInstance(mcontext).addToRequestQueue(request);
    }


    public void getAll(Context mContext, ProgressDialog dialogg) {

    }


    public void findById(Context mContext, String id) {

    }


    public Match convertJsonToObject(JSONObject object) {
        try {
            Match tmp=  new Match();
            if(object.has("start_date"))
            tmp.setStart_date(getDate(object.getString("start_date")));
            if(object.has("team1"))
            tmp.setTeam1( new Team(object.getString("team1")));
            if(object.has("team2"))
            tmp.setTeam2( new Team(object.getString("team2")));
            if(object.has("end_date"))
            tmp.setEnd_date(getDate(object.getString("end_date")));
            if(object.has("type"))
                tmp.setType(MatchType.valueOf(object.getString("type")));
            if(object.has("state"))
                tmp.setState(ChallengeState.valueOf(object.getString("state")));

            //Goals
            List<MatchAction> goals = new ArrayList<MatchAction>();

            if (object.has("goals")){
                JSONArray jsonArrayGoals = object.getJSONArray("goals");
                for (int i = 0; i < jsonArrayGoals.length(); i++) {
                    goals.add(new MatchAction( jsonArrayGoals.getString(i)));
                }
                tmp.setGoals(goals);
            }
            //redCards
            List<MatchAction> redCards = new ArrayList<MatchAction>();

            if (object.has("redCards")){
                JSONArray jsonArrayRedCards = object.getJSONArray("redCards");
                for (int i = 0; i < jsonArrayRedCards.length(); i++) {
                    redCards.add(new MatchAction( jsonArrayRedCards.getString(i)));
                }
                tmp.setRedCards(redCards);
            }
            //yellowCard
            List<MatchAction> yellowCards = new ArrayList<MatchAction>();

            if (object.has("yellowCards")){
                JSONArray jsonArrayYellowCards = object.getJSONArray("yellowCards");
                for (int i = 0; i < jsonArrayYellowCards.length(); i++) {
                    yellowCards.add(new MatchAction( jsonArrayYellowCards.getString(i)));
                }
                tmp.setYellowCards(yellowCards);
            }

            if (object.has("winner")){
                if(!object.get("winner").equals("null"))
                    tmp.setWinner(TeamRepository.getInstance().convertJsonToObject((JSONObject) object.get("winner")));
            }
            tmp.setId(object.getString("_id"));
            return tmp;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  null;
    }


    public JSONObject convertObjectToJson(JSONObject object, Match match) {
        JSONArray goals = new JSONArray();
        JSONArray yellowCards = new JSONArray();
        JSONArray redCards = new JSONArray();

        if (match.getGoals()!=null) {
            for (MatchAction row : match.getGoals()) {
                goals.put(row.getId());
            }
        }
            if (match.getYellowCards()!=null) {
                for (MatchAction row : match.getYellowCards()) {
                    yellowCards.put(row.getId());
                }
            }
                if (match.getRedCards()!=null) {
                    for (MatchAction row : match.getRedCards()) {
                        redCards.put(row.getId());
                    }
                }

        try {
            object.put("start_date", match.getStart_date());
            object.put("end_date", match.getEnd_date());
            if(match.getTeam1()!=null)
            object.put("team1", match.getTeam1().getId());
            if(match.getTeam2()!=null)
            object.put("team2", match.getTeam2().getId());
            object.put("goals", goals);
            object.put("yellowCards", yellowCards);
            object.put("redCards", redCards);
            if(match.getWinner()!=null)
            object.put("winner", match.getWinner().getId());
            object.put("state", match.getState().toString());
            object.put("type", match.getType().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }


    public ArrayList<Match> getList() {
        return matches;
    }




    public Match convertJsonToObjectDeepPopulate(JSONObject jsonTag) {
        return null;
    }


    public Match getElement() {
        return match;
    }
    public Date getDate(String key){
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'" );
            date = format.parse(key);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
