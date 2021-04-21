package com.example.gark.repositories;

import android.icu.text.SimpleDateFormat;
import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Date;

public interface IRepository {
    String baseURL = "http://192.168.209.1:3000";
  //public static String baseURL = "https://gark-backend.herokuapp.com";
    void showLoadingButton();
    void doAction();
    void dismissLoadingButton();

}