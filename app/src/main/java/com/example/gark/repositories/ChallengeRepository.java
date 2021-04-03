package com.example.gark.repositories;
import android.app.ProgressDialog;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.gark.Utils.VolleyInstance;
import com.example.gark.entites.Challenge;
import com.example.gark.entites.ChallengeState;
import com.example.gark.entites.ChallengeType;
import com.example.gark.entites.Match;
import com.example.gark.entites.Skills;
import com.example.gark.entites.Team;
import com.example.gark.entites.Terrain;
import com.example.gark.entites.User;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChallengeRepository {
    private IRepository iRepository;
    private static ChallengeRepository instance;
    public static  ArrayList<Challenge> challenges;
    public static  Challenge challenge;

    public static ChallengeRepository getInstance() {
        if (instance==null){
            instance = new ChallengeRepository();
        }
        return instance;
    }


    public void add(Context mcontext, Challenge challenge, ProgressDialog dialog) {
        iRepository.showLoadingButton();
        final String url = iRepository.baseURL + "/add_challenge";
        Log.e("TAG", "add: "+challenge );
        JSONObject object = new JSONObject();
        convertObjectToJson(object,challenge);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("TAG", "add team message: "+response.getString("message"));
                            iRepository.doAction();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally {
                            iRepository.dismissLoadingButton();
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

    public void delete(Context mcontext, String id, ProgressDialog dialog) {

    }

    public void update(Context mcontext, Challenge challenge, String id) {
        iRepository.showLoadingButton();
        final String url=iRepository.baseURL  + "/update_challenge/"+id;
        this.challenge=challenge;
        JSONObject object = new JSONObject();
        convertObjectToJson(object,challenge);
        JsonObjectRequest request = new  JsonObjectRequest(Request.Method.PUT, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.e("TAG", "done: "+response.getString("message"));
                    iRepository.doAction();
                    iRepository.dismissLoadingButton();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
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
        iRepository.showLoadingButton();
        JsonObjectRequest request = new  JsonObjectRequest(Request.Method.GET, IRepository.baseURL + "/all_challenges", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            challenges=new ArrayList<Challenge>();
                            String message = response.getString("message");
                            JSONArray jsonArray = response.getJSONArray("challenges");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonTag = jsonArray.getJSONObject(i);
                                challenges.add( convertJsonToObject(jsonTag));

                            }
                            iRepository.doAction();
                            iRepository.dismissLoadingButton();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("TAG", "onResponse: "+IRepository.baseURL + "/all_challenges");
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyInstance.getInstance(mContext).addToRequestQueue(request);
    }

    public void findByName(Context mContext, String challengeType) {
        iRepository.showLoadingButton();
        JsonObjectRequest request = new  JsonObjectRequest(Request.Method.GET, IRepository.baseURL + "/findByName/"+challengeType, null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("TAG", "onResponse: "+response.toString() );
                        challenge=convertJsonToObjectDeepPopulate(response);
                        iRepository.doAction();
                        iRepository.dismissLoadingButton();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("TAG", "onResponse: "+IRepository.baseURL +  "/findByName/"+challengeType);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyInstance.getInstance(mContext).addToRequestQueue(request);
    }

    public void findById(Context mContext, String id) {

    }


    public Challenge convertJsonToObject(JSONObject object) {
        try {
            //TEAMS
            List<Team> teams = new ArrayList<Team>();

            if (object.has("teams")){
                JSONArray jsonArrayTeams = object.getJSONArray("teams");
                for (int i = 0; i < jsonArrayTeams.length(); i++) {
                    teams.add(new Team( jsonArrayTeams.getString(i)));
                }
            }
            //MATCHES
            List<Match> matches = new ArrayList<Match>();

            if (object.has("matches")){
                JSONArray jsonArrayTeams = object.getJSONArray("matches");
                for (int i = 0; i < jsonArrayTeams.length(); i++) {
                    //matches.add(new Match( jsonArrayTeams.getString(i)));
                }
            }

            Challenge tmp=  new Challenge(object.getString("name"),
                    getDate(object.getString("start_date")),
                    getDate(object.getString("end_date")),
                    getDate(object.getString("date_created")),
                    object.getInt("maxNumberOfTeams"),
                    teams,
                    matches,
                    object.getInt("prize"),
                    object.getString("location"),
                    object.getString("description"),
                    object.getString("image"),
                    new User(object.getString("creator")),
                            ChallengeType.valueOf(object.getString("type")),
                                    ChallengeState.valueOf(object.getString("state")));

            Log.e("TAG", "convertJsonToObject: "+tmp );
            if (object.has("winner")){
                if(!object.get("winner").equals("null"))
                tmp.setWinner(TeamRepository.getInstance().convertJsonToObject((JSONObject) object.get("winner")));
            }
            if (object.has("terrain")){
              //  tmp.setTerrain(TeamRepository.getInstance().convertJsonToObject((JSONObject) object.get("terrain")));
            }
            tmp.setId(object.getString("_id"));
            return tmp;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  null;
    }
    public void removeTeamFromChallenge( Context context,String challengeId,String teamId){
        iRepository.showLoadingButton();
        String url = IRepository.baseURL + "/remove_team_challenge"+"/"+challengeId+"/"+teamId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        iRepository.doAction();
                        Toast.makeText(context,"Your team have been removed sucessfully from the challenge !",Toast.LENGTH_LONG).show();
                        iRepository.dismissLoadingButton();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        VolleyInstance.getInstance(context).addToRequestQueue(request);
    }

    public void addTeamToChallenge( Context context,String challengeId,String teamId){
        iRepository.showLoadingButton();
        String url = IRepository.baseURL + "/add_team_challenge"+"/"+challengeId+"/"+teamId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                            iRepository.doAction();
                        Toast.makeText(context,"Your team have been added sucessfully to the challenge !",Toast.LENGTH_LONG).show();
                            iRepository.dismissLoadingButton();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        VolleyInstance.getInstance(context).addToRequestQueue(request);
    }


    public JSONObject convertObjectToJson(JSONObject object, Challenge challenge) {
        JSONArray jsonArrayTeams = new JSONArray();
        JSONArray jsonArrayMatches = new JSONArray();
        if (challenge.getTeams()!=null){
            for (Team row:challenge.getTeams()){
                jsonArrayTeams.put(row.getId());
                Log.e("TAG", "convertObjectToJson: "+row.getId() );
            }
        }
        if (challenge.getMatches()!=null) {
            for (Match row : challenge.getMatches()) {
                jsonArrayMatches.put(row.getId());
            }
        }

        try {
            object.put("name", challenge.getName());
            object.put("image", challenge.getImage());
            if(challenge.getWinner()!=null){
                object.put("winner", challenge.getWinner().getId());
            }
            object.put("teams", jsonArrayMatches);
            object.put("matches", jsonArrayMatches);
            object.put("type", challenge.getType().toString());
            object.put("state", challenge.getState().toString());
            object.put("description",challenge.getDescription());
            object.put("start_date", challenge.getStart_date());
            object.put("end_date", challenge.getEnd_date());
            object.put("date_created", challenge.getDate_created());
            object.put("creator",challenge.getCreator().getId());
            object.put("prize",challenge.getPrize());
            if(challenge.getTerrain()!=null){
            object.put("terrain",challenge.getTerrain().getId());
            }
            object.put("location",challenge.getLocation());
            object.put("maxNumberOfTeams",challenge.getMaxNumberOfTeams());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }


    public ArrayList<Challenge> getList() {
        return challenges;
    }


    public void setiRepository(IRepository iRepository) {
        this.iRepository = iRepository;
    }

    public Challenge convertJsonToObjectDeepPopulate(JSONObject object) {
        try {
            List<Team> teams = new ArrayList<Team>();

            if (object.has("teams")){
                JSONArray jsonArrayTeams = object.getJSONArray("teams");
                for (int i = 0; i < jsonArrayTeams.length(); i++) {
                    teams.add(new TeamRepository().convertJsonToObject((JSONObject) jsonArrayTeams.get(i)));
                }
            }
            //MATCHES
            List<Match> matches = new ArrayList<Match>();

            if (object.has("matches")){
                JSONArray jsonArrayTeams = object.getJSONArray("matches");
                for (int i = 0; i < jsonArrayTeams.length(); i++) {
                    //matches.add(new Match( jsonArrayTeams.getString(i)));
                }
            }

            Log.e("TAG", "convertJsonToObject: "+ object.getString("name"));
            Challenge tmp= new Challenge(object.getString("name"),
                    getDate(object.getString("start_date")),
                    getDate(object.getString("end_date")),
                    getDate(object.getString("date_created")),
                    object.getInt("maxNumberOfTeams"),
                    teams,
                    matches,
                    object.getInt("prize"),
                    object.getString("location"),
                    object.getString("description"),
                    object.getString("image"),
                    new User(object.getString("creator")),

                    ChallengeType.valueOf(object.getString("type")),
                    ChallengeState.valueOf(object.getString("state")));
            if (object.has("winner")){
                if(!object.get("winner").equals("null"))
                tmp.setWinner(TeamRepository.getInstance().convertJsonToObject((JSONObject) object.get("winner")));
            }
            if (object.has("terrain")){
                //  tmp.setTerrain(TeamRepository.getInstance().convertJsonToObject((JSONObject) object.get("terrain")));
            }
            tmp.setId(object.getString("_id"));
            return tmp;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return  null;
    }


    public Challenge getElement() {
        return challenge;
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
