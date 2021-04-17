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
import com.example.gark.entites.Challenge;
import com.example.gark.entites.Match;
import com.example.gark.entites.MatchAction;
import com.example.gark.entites.MatchAction;
import com.example.gark.entites.MatchActionType;
import com.example.gark.entites.Skills;
import com.example.gark.entites.Team;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public class MatchActionRepository implements CRUDRepository<MatchAction> {
    private IRepository iRepository;
    private static MatchActionRepository instance;
    public static ArrayList<MatchAction> matchActions;
    public static MatchAction matchAction;
    public static String id;
    public static int generatedMatchAction = 0;

    public static MatchActionRepository getInstance() {
        if (instance == null) {
            instance = new MatchActionRepository();
        }
        return instance;
    }

    @Override
    public void add(Context mcontext, MatchAction matchAction, ProgressDialog dialog) {
        iRepository.showLoadingButton();
        final String url = iRepository.baseURL + "/add_matchAction";
        JSONObject object = new JSONObject();
        convertObjectToJson(object, matchAction);
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            id = response.getString("message");
                            iRepository.doAction();
                            generatedMatchAction++;
                        } catch (JSONException e) {
                            e.printStackTrace();
                        } finally {
                            iRepository.dismissLoadingButton();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", "fail: " + error);
            }
        });
        VolleyInstance.getInstance(mcontext).addToRequestQueue(request);
    }

    @Override
    public void delete(Context mcontext, String id, ProgressDialog dialog) {

    }

    @Override
    public void update(Context mcontext, MatchAction matchAction, String id) {

    }

    @Override
    public void getAll(Context mContext, ProgressDialog dialogg) {

    }

    @Override
    public void findById(Context mContext, String id) {

    }

    public void findBy(Context mContext, String type, String id) {
        iRepository.showLoadingButton();
        String url = "";
        switch (type) {
            case "challenge":
                url = iRepository.baseURL + "/groupByChallenge";
                break;
            case "match":
                url = iRepository.baseURL + "/groupByMatch";
                break;
            case "team":
                url = iRepository.baseURL + "/groupByTeam";
                break;
            case "player":
                url = iRepository.baseURL + "/groupByPlayer";
                break;
        }
        url += "/" + id;
        String finalUrl = url;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            matchActions = new ArrayList<MatchAction>();
                            String message = response.getString("message");
                            JSONArray jsonArray = response.getJSONArray("matchActions");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonTag = jsonArray.getJSONObject(i);
                                matchActions.add(convertJsonToObject(jsonTag));

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
                Log.e("TAG", "onResponse: " + finalUrl);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyInstance.getInstance(mContext).addToRequestQueue(request);

    }

    @Override
    public MatchAction convertJsonToObject(JSONObject object) {
        MatchAction tmp = new MatchAction();
        try {
            if (object.has("player"))
                tmp.setPlayer(new Skills(object.getString("player")));
            if (object.has("challenge"))
                tmp.setChallenge(new Challenge(object.getString("challenge")));
            if (object.has("match")){
                try {
                    tmp.setMatch(MatchRepository.getInstance().convertJsonToObject((JSONObject) object.get("match")));
                }catch (Exception e){
                    tmp.setMatch(new Match(object.getString("match")));
                }
            }

            if (object.has("team"))
                tmp.setTeam(new Team(object.getString("team")));
            if (object.has("date"))
                tmp.setDate(getDate(object.getString("date")));
            if (object.has("type"))
                tmp.setType(MatchActionType.valueOf(object.getString("type")));
            return tmp;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Date getDate(String key) {
        Date date = null;
        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
            date = format.parse(key);
        } catch (java.text.ParseException e) {
            e.printStackTrace();
        }
        return date;
    }

    @Override
    public JSONObject convertObjectToJson(JSONObject object, MatchAction matchAction) {
        try {
            object.put("player", matchAction.getPlayer().getId());
            object.put("challenge", matchAction.getChallenge().getId());
            object.put("match", matchAction.getMatch().getId());
            object.put("team", matchAction.getTeam().getId());
            object.put("date", matchAction.getDate());
            object.put("type", matchAction.getType().toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    @Override
    public ArrayList<MatchAction> getList() {
        return matchActions;
    }

    @Override
    public void setiRepository(IRepository iRepository) {
        this.iRepository = iRepository;
    }

    @Override
    public MatchAction convertJsonToObjectDeepPopulate(JSONObject jsonTag) {
        return null;
    }

    @Override
    public MatchAction getElement() {
        return matchAction;
    }
}
