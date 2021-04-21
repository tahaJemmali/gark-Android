package com.example.gark.repositories;

import android.app.ProgressDialog;
import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Build;

import androidx.annotation.RequiresApi;

import com.example.gark.entites.Team;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;

public interface CRUDRepository<T> {
    void add(final Context mcontext, T t, final ProgressDialog dialog);
    void delete(final Context mcontext, String id, final ProgressDialog dialog);
    void update(final Context mcontext, T t, String id);
    void getAll(final Context mContext, final ProgressDialog dialogg);
    void findById(final Context mContext, String id);
    T convertJsonToObject(JSONObject object);
    JSONObject convertObjectToJson(JSONObject object, T t);
    ArrayList<T> getList();
    void setiRepository(IRepository iRepository);
    T convertJsonToObjectDeepPopulate(JSONObject jsonTag);
    T getElement();
    @RequiresApi(api = Build.VERSION_CODES.N)
    static Date getDate(String key){
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
