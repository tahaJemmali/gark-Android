package com.example.gark.repositories;

import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.gark.Utils.VolleyInstance;
import com.example.gark.entites.Nationality;
import com.example.gark.entites.Role;
import com.example.gark.entites.Skills;
import com.example.gark.entites.Team;
import com.example.gark.entites.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class SkillsRepository implements CRUDRepository<Skills> {
    private IRepository iRepository;
    private static SkillsRepository instance;
    public static  ArrayList<Skills> players;
    public static Skills player;

    public static SkillsRepository getInstance() {
        if (instance==null)
            instance = new SkillsRepository();
        return instance;
    }
    @Override
    public void add(Context mcontext, Skills skills, ProgressDialog dialog) {
        final String url = iRepository.baseURL + "/add_skills";
        Log.e("TAG", "add Skills: "+skills );
        JSONObject object = new JSONObject();
        convertObjectToJson(object,skills);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url,object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Log.e("TAG", "added skill id: "+response.getString("message"));
                            getAll(mcontext,dialog);
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

    @Override
    public void delete(Context mcontext, String id, ProgressDialog dialog) {

    }

    @Override
    public void update(Context mcontext, Skills skills, String id) {
        iRepository.showLoadingButton();
        final String url=iRepository.baseURL  + "/update_skills/"+id;
        this.player=skills;
        JSONObject object = new JSONObject();
        convertObjectToJson(object,player);
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

    public void addTeamToPlayer( Context context,String challengeId,String matchId){
        iRepository.showLoadingButton();
        String url = IRepository.baseURL + "/add_team_player"+"/"+challengeId+"/"+matchId;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                 //       Log.e("TAG", "onResponse: "+"Your match have been added sucessfully to the challenge !" );
                        iRepository.dismissLoadingButton();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyInstance.getInstance(context).addToRequestQueue(request);
    }

    @Override
    public void getAll(Context mContext, ProgressDialog dialogg) {
      //  iRepository.showLoadingButton();
        JsonObjectRequest request = new  JsonObjectRequest(Request.Method.GET, IRepository.baseURL + "/top_players", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                           players=new ArrayList<Skills>();
                            String message = response.getString("message");
                            JSONArray jsonArray = response.getJSONArray("topPlayers");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonTag = jsonArray.getJSONObject(i);
                                players.add( convertJsonToObject(jsonTag));
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
                Log.e("TAG", "onResponse: "+IRepository.baseURL + "/top_players");
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyInstance.getInstance(mContext).addToRequestQueue(request);
    }

    @Override
    public void findById(Context mContext,String id) {
        iRepository.showLoadingButton();
        JsonObjectRequest request = new  JsonObjectRequest(Request.Method.GET, IRepository.baseURL + "/findByIdskills/"+id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        player = convertJsonToObjectDeepPopulate(response);
                        iRepository.doAction();
                        iRepository.dismissLoadingButton();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("TAG", "onResponse: "+IRepository.baseURL + "/findByIdskills"+id);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyInstance.getInstance(mContext).addToRequestQueue(request);
    }

    public void findPlayerById(Context mContext,String id) {
        iRepository.showLoadingButton();
        JsonObjectRequest request = new  JsonObjectRequest(Request.Method.GET, IRepository.baseURL + "/findPlayerByIdskills/"+id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        player = convertJsonToObjectDeepPopulate(response);
                        iRepository.doAction();
                        iRepository.dismissLoadingButton();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("TAG", "onResponse: "+IRepository.baseURL + "/findByIdskills"+id);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyInstance.getInstance(mContext).addToRequestQueue(request);
    }

    @Override
    public Skills convertJsonToObject(JSONObject jsonTag) {
        try {
            List<Team> teams = new ArrayList<Team>();

            if (jsonTag.has("teams")){
                JSONArray jsonArrayTeams = jsonTag.getJSONArray("teams");
                for (int i = 0; i < jsonArrayTeams.length(); i++) {
                    teams.add(new Team( jsonArrayTeams.getString(i)));
                }
            }

            Skills skills= new Skills(jsonTag.getString("_id"),
                jsonTag.getInt("pace"),
                jsonTag.getInt("shooting"),
                jsonTag.getInt("passing"),
                jsonTag.getInt("dribbling"),
                jsonTag.getInt("defending"),
                jsonTag.getInt("physical"),
                jsonTag.getInt("score"),
                jsonTag.getInt("goals"),
                Role.valueOf(jsonTag.getString("role")),
                UserRepository.getInstance().convertJsonToObject((JSONObject)jsonTag.get("player")),
                (Nationality.valueOf(jsonTag.getString("nationality"))),jsonTag.getInt("rating")
                ,jsonTag.getString("description"),jsonTag.getInt("age"));
            skills.setTeams(teams);
        return skills;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public JSONObject convertObjectToJson(JSONObject object ,Skills skills) {
        try {
            JSONArray jsonArrayTeams = new JSONArray();
            if (skills.getTeams()!=null){
                for (Team row:skills.getTeams()){
                    jsonArrayTeams.put(row.getId());
                }
            }

            object.put("pace", skills.getPace());
            object.put("shooting", skills.getShooting());
            object.put("passing", skills.getPassing());
            object.put("dribbling", skills.getDribbling());
            object.put("defending", skills.getDefending());
            object.put("physical", skills.getPhysical());
            object.put("rating",skills.getRating());
            object.put("score", skills.getScore());
            object.put("goals", skills.getGoals());
            object.put("role", skills.getRole().toString());
            object.put("player", skills.getPlayer().getId());
            object.put("nationality", skills.getNationality().toString());
            object.put("teams",jsonArrayTeams);
            object.put("age",skills.getAge());
            object.put("description",skills.getDescription());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    @Override
    public ArrayList<Skills> getList() {
        if (players==null)
            players = new ArrayList<Skills>();
        return players;
    }

    @Override
    public void setiRepository(IRepository iRepository) {
        this.iRepository = iRepository;
    }

    @Override
    public Skills convertJsonToObjectDeepPopulate(JSONObject jsonTag) {
        try {
            List<Team> teams = new ArrayList<Team>();

            if (jsonTag.has("teams")){
                JSONArray jsonArrayTeams = jsonTag.getJSONArray("teams");
                for (int i = 0; i < jsonArrayTeams.length(); i++) {
                    teams.add(new TeamRepository().convertJsonToObject((JSONObject) jsonArrayTeams.get(i)));
                }
            }

            Skills skills= new Skills(jsonTag.getString("_id"),
                    jsonTag.getInt("pace"),
                    jsonTag.getInt("shooting"),
                    jsonTag.getInt("passing"),
                    jsonTag.getInt("dribbling"),
                    jsonTag.getInt("defending"),
                    jsonTag.getInt("physical"),
                    jsonTag.getInt("score"),
                    jsonTag.getInt("goals"),
                    Role.valueOf(jsonTag.getString("role")),
                    UserRepository.getInstance().convertJsonToObject((JSONObject)jsonTag.get("player")),
                    (Nationality.valueOf(jsonTag.getString("nationality"))),jsonTag.getInt("rating")
                    ,jsonTag.getString("description"),jsonTag.getInt("age"));

                skills.setTeams(teams);

            return skills;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Skills getElement() {
        if (player==null)
            player = new Skills();
        return player;
    }
}
