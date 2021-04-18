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
import com.example.gark.entites.Ressource;
import com.example.gark.entites.RessourceType;
import com.example.gark.entites.Skills;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RessourceRepository  {
    private IRepository iRepository;
    private static RessourceRepository instance;
    public static ArrayList<Ressource> ressources;
    public static Ressource ressource;

    public static RessourceRepository getInstance() {
        if (instance == null)
            instance = new RessourceRepository();
        return instance;
    }

    //GET FROM FILE
    public void getAll(Context mContext, RessourceType ressourceType) {
         iRepository.showLoadingButton();
         String url="";
        if (ressourceType.equals(RessourceType.player))
           url= IRepository.baseURL + "/getAllPlayers";
        else url = IRepository.baseURL + "/getAllTeams";

        String finalUrl = url;
        JsonObjectRequest request = new  JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            ressources=new ArrayList<Ressource>();
                            JSONArray jsonArray = response.getJSONArray("array");

                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonTag = jsonArray.getJSONObject(i);
                                ressources.add( convertJsonToObject(jsonTag));
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
                Log.e("TAG", "onResponse: "+ finalUrl);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyInstance.getInstance(mContext).addToRequestQueue(request);
    }

    public void findByName(Context mContext, String fname, String lname, RessourceType ressourceType) {

    }

    public void findById(Context mContext, String id, RessourceType ressourceType) {

    }


    //CRUD
    public void add(Context mcontext, Ressource ressource, ProgressDialog dialog) {

    }


    public void delete(Context mcontext, String id, ProgressDialog dialog) {

    }


    public void update(Context mcontext, Ressource ressource, String id) {

    }


    public Ressource convertJsonToObject(JSONObject object) {
        Ressource tmp = new Ressource();
        try {
            if (object.has("id"))
                tmp.setId(object.getString("id"));
            if (object.has("firstName")) {
                tmp.setFirstName(object.getString("firstName"));
                tmp.setLastName(object.getString("lastName"));
                tmp.setType(RessourceType.player);
            } else {
                tmp.setName(object.getString("name"));
                tmp.setType(RessourceType.club);
            }
            if (object.has("image"))
                tmp.setImage(object.getString("image"));

            return tmp;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }


    public JSONObject convertObjectToJson(JSONObject object, Ressource ressource) {
        try {
            object.put("id", ressource.getId());
            object.put("image", ressource.getImage());
            if (ressource.getType().equals(RessourceType.player)) {
                object.put("firstName", ressource.getFirstName());
                object.put("lastName", ressource.getLastName());
            } else {
                object.put("name", ressource.getName());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return object;
    }


    public ArrayList<Ressource> getList() {
        if (ressources == null)
            ressources = new ArrayList<Ressource>();
        return ressources;
    }


    public void setiRepository(IRepository iRepository) {
        this.iRepository = iRepository;
    }


    public Ressource getElement() {
        if (ressource == null)
            ressource = new Ressource();
        return ressource;
    }
}
