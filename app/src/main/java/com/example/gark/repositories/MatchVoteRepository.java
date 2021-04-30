package com.example.gark.repositories;

import android.app.ProgressDialog;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.gark.R;
import com.example.gark.Utils.VolleyInstance;
import com.example.gark.entites.Challenge;
import com.example.gark.entites.Match;
import com.example.gark.entites.MatchVote;
import com.example.gark.entites.Nationality;
import com.example.gark.entites.Role;
import com.example.gark.entites.Skills;
import com.example.gark.entites.Team;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MatchVoteRepository implements CRUDRepository<MatchVote> {
    private IRepository iRepository;
    private static MatchVoteRepository instance;
    public static  ArrayList<MatchVote> matchVotes;
    public static  MatchVote matchVote;

    public static MatchVoteRepository getInstance() {
        if (instance==null){
            instance = new MatchVoteRepository();
        }
        return instance;
    }
    @Override
    public void add(Context mContext, MatchVote matchVote, ProgressDialog dialog) {
        iRepository.showLoadingButton();
        final String url = iRepository.baseURL + "/add_matchVote";
        Log.e("TAG", "add: "+matchVote );
        JSONObject object = new JSONObject();
        convertObjectToJson(object,matchVote);
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
                error.printStackTrace();
                Toast.makeText(mContext,mContext.getString(R.string.connection_problem),Toast.LENGTH_LONG).show();
                iRepository.dismissLoadingButton();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyInstance.getInstance(mContext).addToRequestQueue(request);
    }

    public void findVotesByVoter(Context mContext, String voteid) {
        iRepository.showLoadingButton();
        JsonObjectRequest request = new  JsonObjectRequest(Request.Method.GET, IRepository.baseURL + "/findVoteByVoter/"+voteid, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        matchVotes=new ArrayList<MatchVote>();
                        try {
                            String message = response.getString("message");
                            JSONArray jsonArray = response.getJSONArray("matchVotes");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonTag = jsonArray.getJSONObject(i);
                                matchVotes.add( convertJsonToObject(jsonTag));
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
                Toast.makeText(mContext,mContext.getString(R.string.connection_problem),Toast.LENGTH_LONG).show();
                iRepository.dismissLoadingButton();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(DefaultRetryPolicy.DEFAULT_TIMEOUT_MS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyInstance.getInstance(mContext).addToRequestQueue(request);
    }

    @Override
    public void delete(Context mContext, String id, ProgressDialog dialog) {

    }

    @Override
    public void update(Context mContext, MatchVote matchVote, String id) {

    }

    @Override
    public void getAll(Context mContext, ProgressDialog dialogg) {

    }

    @Override
    public void findById(Context mContext, String id) {

    }

    @Override
    public MatchVote convertJsonToObject(JSONObject jsonTag) {
        try {
                    MatchVote matchVote= new MatchVote(
                    new Skills(jsonTag.getString("voter")),
                    new Skills(jsonTag.getString("votedOn")),
                    jsonTag.getInt("pace"),
                    jsonTag.getInt("shooting"),
                    jsonTag.getInt("passing"),
                    jsonTag.getInt("dribbling"),
                    jsonTag.getInt("defending"),
                    jsonTag.getInt("physical"),
                    jsonTag.getInt("rating"),
                    getDate(jsonTag.getString("vote_date")));
           matchVote.setId( jsonTag.getString("_id"));
            return matchVote;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
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
    public JSONObject convertObjectToJson(JSONObject object, MatchVote matchVote) {

        try {
            object.put("voter", matchVote.getVoter().getId());
            object.put("votedOn", matchVote.getVotedOn().getId());
            object.put("pace", matchVote.getPace());
            object.put("shooting", matchVote.getShooting());
            object.put("passing", matchVote.getPassing());
            object.put("dribbling", matchVote.getDribbling());
            object.put("defending", matchVote.getDefending());
            object.put("physical", matchVote.getPhysical());
            object.put("rating", matchVote.getRating());
            object.put("vote_date", matchVote.getVote_date());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }

    @Override
    public ArrayList<MatchVote> getList() {
        return matchVotes;
    }

    @Override
    public void setiRepository(IRepository iRepository) {
        this.iRepository = iRepository;
    }

    @Override
    public MatchVote convertJsonToObjectDeepPopulate(JSONObject jsonTag) {
        return null;
    }

    @Override
    public MatchVote getElement() {
        return matchVote;
    }
}
