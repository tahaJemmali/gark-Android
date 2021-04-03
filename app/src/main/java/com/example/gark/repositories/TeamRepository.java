package com.example.gark.repositories;

import android.app.ProgressDialog;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.gark.Utils.VolleyInstance;
import com.example.gark.entites.Categorie;
import com.example.gark.entites.Nationality;
import com.example.gark.entites.Role;
import com.example.gark.entites.Skills;
import com.example.gark.entites.Team;
import com.example.gark.entites.User;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class TeamRepository implements CRUDRepository<Team> {
    private IRepository iRepository;
    private static TeamRepository instance;
    public static  ArrayList<Team> teams;
    public static  Team team;

    public static TeamRepository getInstance() {
        if (instance==null)
            instance = new TeamRepository();
        return instance;
    }


    @Override
    public void add(Context mcontext, Team team, ProgressDialog dialog) {
        iRepository.showLoadingButton();
        final String url = iRepository.baseURL + "/add_team";
        JSONObject object = new JSONObject();
        convertObjectToJson(object,team);
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

    @Override
    public void delete(Context mcontext, String id, ProgressDialog dialog) {

    }

    @Override
    public void update(Context mcontext, Team team, String id) {

    }

    @Override
    public void getAll(Context mContext, ProgressDialog dialogg) {
        iRepository.showLoadingButton();
        JsonObjectRequest request = new  JsonObjectRequest(Request.Method.GET, IRepository.baseURL + "/all_teams", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            teams=new ArrayList<Team>();
                            String message = response.getString("message");
                            JSONArray jsonArray = response.getJSONArray("teams");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonTag = jsonArray.getJSONObject(i);
                                teams.add( convertJsonToObject(jsonTag));

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
                Log.e("TAG", "onResponse: "+IRepository.baseURL + "/all_teams");
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
        JsonObjectRequest request = new  JsonObjectRequest(Request.Method.GET, IRepository.baseURL + "/findByIdTeam/"+id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                            team = convertJsonToObjectDeepPopulate(response);
                            iRepository.doAction();
                            iRepository.dismissLoadingButton();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("TAG", "onResponse: "+IRepository.baseURL + "/findByIdTeam"+id);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyInstance.getInstance(mContext).addToRequestQueue(request);
    }

    @Override
    public Team convertJsonToObject(JSONObject jsonTag) {
        try {
            List<Skills> titulares = new ArrayList<>();
            List<Skills> substitutes = new ArrayList<>();
            JSONArray jsonArrayTitulares = jsonTag.getJSONArray("titulares");
            for (int i = 0; i < jsonArrayTitulares.length(); i++) {
                titulares.add(new Skills(jsonArrayTitulares.getString(i)));
            }
            JSONArray jsonArraySubstitutes = jsonTag.getJSONArray("substitutes");
            for (int i = 0; i < jsonArraySubstitutes.length(); i++) {
                titulares.add(new Skills(jsonArrayTitulares.getString(i)));
            }
            Date date = this.getDate(jsonTag.getString("date_created"));
            return new Team(jsonTag.getString("_id"),
                    jsonTag.getString("name"),
                    jsonTag.getString("image"),
                   new Skills(jsonTag.getString("capitaine")),
                    titulares,
                    substitutes,
                    jsonTag.getInt("victories"),
                    jsonTag.getInt("defeats"),
                    jsonTag.getInt("points"),
                    jsonTag.getInt("rating"),
                    (Categorie.valueOf(jsonTag.getString("categorie"))),
                    (Nationality.valueOf(jsonTag.getString("nationality"))),jsonTag.getString("description"),
                    date,
                    jsonTag.getInt("draws"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Team convertJsonToObjectDeepPopulate(JSONObject jsonTag) {
        try {
            List<Skills> titulares = new ArrayList<>();
            List<Skills> substitutes = new ArrayList<>();
            JSONArray jsonArrayTitulares = jsonTag.getJSONArray("titulares");
            for (int i = 0; i < jsonArrayTitulares.length(); i++) {
                JSONObject titulaire = jsonArrayTitulares.getJSONObject(i);
                titulares.add(SkillsRepository.getInstance().convertJsonToObject(titulaire));
            }
            JSONArray jsonArraySubstitutes = jsonTag.getJSONArray("substitutes");
            for (int i = 0; i < jsonArraySubstitutes.length(); i++) {
                JSONObject substitute = jsonArrayTitulares.getJSONObject(i);
                substitutes.add(SkillsRepository.getInstance().convertJsonToObject(substitute));
            }
            Date date = this.getDate(jsonTag.getString("date_created"));
            return new Team(jsonTag.getString("_id"),
                    jsonTag.getString("name"),
                    jsonTag.getString("image"),
                    SkillsRepository.getInstance().convertJsonToObject((JSONObject)jsonTag.get("capitaine")),
                    titulares,
                    substitutes,
                    jsonTag.getInt("victories"),
                    jsonTag.getInt("defeats"),
                    jsonTag.getInt("points"),
                    jsonTag.getInt("rating"),
                    (Categorie.valueOf(jsonTag.getString("categorie"))),
                    (Nationality.valueOf(jsonTag.getString("nationality"))),
                    jsonTag.getString("description"),date,
                    jsonTag.getInt("draws"));
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public Team getElement() {
        if (team==null)
            team = new Team();
        return team;
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

    @Override
    public JSONObject convertObjectToJson(JSONObject object,Team team) {
        JSONArray jsonArrayTitulares = new JSONArray();
        JSONArray jsonArraySubstitutes = new JSONArray();
        JSONObject objectTMP = new JSONObject();
        if (team.getTitulares()!=null){
            for (Skills row:team.getTitulares()){
                jsonArrayTitulares.put(row.getId());
            }
        }
        if (team.getSubstitutes()!=null) {
            for (Skills row : team.getSubstitutes()) {
                jsonArraySubstitutes.put(row.getId());
            }
        }
        try {
            object.put("name", team.getName());
            object.put("image", team.getImage());
            object.put("categorie", team.getCategorie().toString());
            object.put("capitaine", team.getCapitaine().getId());
            object.put("titulares", jsonArrayTitulares);
            object.put("substitutes", jsonArraySubstitutes);
            object.put("victories",0);
            object.put("defeats", 0);
            object.put("points", 0);
            object.put("rating", 0);
            object.put("nationality", team.getNationality().toString());
            object.put("description",team.getDescription());
            object.put("draws",0);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    @Override
    public ArrayList<Team> getList() {
        if (teams==null)
            teams = new ArrayList<Team>();
        return teams;
    }

    @Override
    public void setiRepository(IRepository iRepository) {
        this.iRepository = iRepository;
    }
}
