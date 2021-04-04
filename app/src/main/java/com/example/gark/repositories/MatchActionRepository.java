package com.example.gark.repositories;

import android.app.ProgressDialog;
import android.content.Context;

import com.example.gark.entites.MatchAction;
import com.example.gark.entites.MatchAction;

import org.json.JSONObject;

import java.util.ArrayList;

public class MatchActionRepository implements CRUDRepository<MatchAction> {
    private IRepository iRepository;
    private static MatchActionRepository instance;
    public static  ArrayList<MatchAction> matchActions;
    public static  MatchAction matchAction;

    public static MatchActionRepository getInstance() {
        if (instance == null) {
            instance = new MatchActionRepository();
        }
        return instance;
    }
    @Override
    public void add(Context mcontext, MatchAction matchAction, ProgressDialog dialog) {

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

    @Override
    public MatchAction convertJsonToObject(JSONObject object) {
        return null;
    }

    @Override
    public JSONObject convertObjectToJson(JSONObject object, MatchAction matchAction) {
        return null;
    }

    @Override
    public ArrayList<MatchAction> getList() {
        return matchActions;
    }

    @Override
    public void setiRepository(IRepository iRepository) {

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
