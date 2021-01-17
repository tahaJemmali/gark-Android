package com.example.gark.repositories;

public interface IRepository {
  //  public static String baseURL = "http://192.168.209.1:3000";
  public static String baseURL = "https://gark-backend.herokuapp.com";
    void showLoadingButton();
    void doAction();
    void dismissLoadingButton();
}