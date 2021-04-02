package com.example.gark.repositories;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.gark.entites.Match;

import org.json.JSONObject;

import java.util.ArrayList;

public class MatchRepository implements CRUDRepository<Match> {
    private IRepository iRepository;
    private static MatchRepository instance;
    public static ArrayList<Match> matches;
    public static  Match match;

    public static MatchRepository getInstance() {
        if (instance==null){
            instance = new MatchRepository();
        }
        return instance;
    }

    @Override
    public void setiRepository(IRepository iRepository) {
        this.iRepository = iRepository;
    }


    @Override
    public void add(Context mcontext, Match match, ProgressDialog dialog) {

    }

    @Override
    public void delete(Context mcontext, String id, ProgressDialog dialog) {

    }

    @Override
    public void update(Context mcontext, Match match, String id) {

    }

    @Override
    public void getAll(Context mContext, ProgressDialog dialogg) {

    }

    @Override
    public void findById(Context mContext, String id) {

    }

    @Override
    public Match convertJsonToObject(JSONObject object) {
        return null;
    }

    @Override
    public JSONObject convertObjectToJson(JSONObject object, Match match) {
        return null;
    }

    @Override
    public ArrayList<Match> getList() {
        return matches;
    }



    @Override
    public Match convertJsonToObjectDeepPopulate(JSONObject jsonTag) {
        return null;
    }

    @Override
    public Match getElement() {
        return match;
    }
}
