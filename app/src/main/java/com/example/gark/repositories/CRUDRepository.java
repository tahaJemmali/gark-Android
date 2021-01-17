package com.example.gark.repositories;

import android.app.ProgressDialog;
import android.content.Context;

import org.json.JSONObject;

import java.util.ArrayList;

public interface CRUDRepository<T> {
    public void add(final Context mcontext, T t, final ProgressDialog dialog);
    public void delete(final Context mcontext, String id, final ProgressDialog dialog);
    public void update(final Context mcontext, T t, String id);
    public void getAll(final Context mContext, final ProgressDialog dialogg);
    public T convertJsonToObject(JSONObject object);
    public JSONObject convertObjectToJson(T t);
    public ArrayList<T> getList();
    public void setiRepository(IRepository iRepository);
}
