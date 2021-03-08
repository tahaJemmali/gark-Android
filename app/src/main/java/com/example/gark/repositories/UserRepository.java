package com.example.gark.repositories;


import android.content.Context;
import android.icu.text.SimpleDateFormat;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.RequiresApi;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.gark.MainActivity;
import com.example.gark.ResetPasswordActivity;
import com.example.gark.Utils.VolleyInstance;
import com.example.gark.VerifyCodeActivity;
import com.example.gark.entites.Nationality;
import com.example.gark.entites.Role;
import com.example.gark.entites.Skills;
import com.example.gark.entites.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class UserRepository {

    private static UserRepository instance;

    private String baseURL = IRepository.baseURL;
    public User user;
    private IRepository iRepository;

    public static UserRepository getInstance() {
        if (instance==null)
            instance = new UserRepository();
        return instance;
    }

    public User getUser() {
        return user;
    }

    public void getUserById(Context mContext, String id){
        iRepository.showLoadingButton();
        JsonObjectRequest request = new  JsonObjectRequest(Request.Method.GET, IRepository.baseURL + "/findByIduser/"+id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.e("TAG", "onResponse: "+response );
                        user = convertJsonToObject(response);
                        iRepository.doAction();
                        iRepository.dismissLoadingButton();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Log.e("TAG", "onResponse: "+IRepository.baseURL + "/findByIduser"+id);
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyInstance.getInstance(mContext).addToRequestQueue(request);
    }
    public void setiRepository(IRepository iRepository){
        this.iRepository = iRepository;
    }

    public void login(final String email,String password,final Context context){

        String url = baseURL+"/login";

        JSONObject object = new JSONObject();
        try {
            object.put("email",email);
            object.put("password",password);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            if (message.equals("Login success")){
                                loadUserFromJson(email,context);
                            }else{
                                Toast.makeText(context,message,Toast.LENGTH_LONG).show();
                                iRepository.dismissLoadingButton();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                Toast.makeText(context,"ERROR",Toast.LENGTH_LONG).show();
                iRepository.dismissLoadingButton();
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(500000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        VolleyInstance.getInstance(context).addToRequestQueue(request);

    }

    private void loadUserFromJson(String email, Context context) {
        String url = baseURL+"/getUser"+"/"+email;
        iRepository.showLoadingButton();
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");

                            JSONObject jsonObject = response.getJSONObject("user");

                            User user = new User();
                            user.setId(jsonObject.getString("_id"));
                            user.setFirstName(jsonObject.getString("firstName"));
                            user.setLastName(jsonObject.getString("lastName"));
                            user.setEmail(jsonObject.getString("email"));
                            user.setAddress(jsonObject.getString("address"));
                            user.setPhone(jsonObject.getString("phone"));
                            user.setPhoto(jsonObject.getString("photo"));
                            user.setActivation(jsonObject.getBoolean("activation"));
                            user.setCompletedInformation(jsonObject.getBoolean("completedInformation"));

                            user.setSign_up_date(getDate(jsonObject.getString("sign_up_date")));
                            if (jsonObject.has("birth_date")){
                                user.setBirth_date(getDate(jsonObject.getString("birth_date")));
                            }
                            user.setSigned_up_with(jsonObject.getString("signed_up_with"));

                            MainActivity.setCurrentLoggedInUser(user);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally{

                            iRepository.dismissLoadingButton();
                            iRepository.doAction();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        VolleyInstance.getInstance(context).addToRequestQueue(request);
    }

    public void register(User u, final Context context){

        String url = baseURL+"/register";

        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("firstName",u.getFirstName());
            object.put("lastName",u.getLastName());
            object.put("email",u.getEmail());
            object.put("password",u.getPassword());
            object.put("address",u.getAddress());
            object.put("phone",u.getPhone());

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            u.setId(message);
                            Toast.makeText(context,"Registration success",Toast.LENGTH_SHORT).show();
                            iRepository.doAction();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        VolleyInstance.getInstance(context).addToRequestQueue(request);
    }

    public void updateEmailUser(String old_email, String new_email, String password, final Context context ){
        String url = baseURL+"/updateUserEmail";

        JSONObject object = new JSONObject();
        try {
            //input your API parameters
            object.put("oldEmail",old_email);
            object.put("newEmail",new_email);
            object.put("password",password);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            switch (message){
                                case "Wrong password":
                                    Toast.makeText(context,message+"",Toast.LENGTH_SHORT).show();
                                    break;
                                case "Email already exists":
                                    Toast.makeText(context,message+"",Toast.LENGTH_SHORT).show();
                                    break;
                                case "Email updated":
                                    Toast.makeText(context,message,Toast.LENGTH_SHORT).show();
                                    //MainActivity.getCurrentLoggedInUser().setEmail(CustomSettingsObjectEditText.emailText);
                                    iRepository.doAction();
                                    break;
                                default:
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally{

                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        VolleyInstance.getInstance(context).addToRequestQueue(request);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
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

    public void passwordRecovery(String email,final Context context) {
        String url = baseURL+"/passwordRecovery";

        JSONObject object = new JSONObject();
        try {
            object.put("email",email);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");

                            switch (message){
                                case "verification code":
                                    VerifyCodeActivity.code = response.getString("code");
                                    ResetPasswordActivity.email = response.getString("email");
                                    iRepository.doAction();
                                    break;
                                case "Email does not exist":
                                    iRepository.showLoadingButton();
                                    Toast.makeText(context,"Email does not exist",Toast.LENGTH_SHORT).show();
                                    break;
                                case "Please login with your facebook account":
                                    iRepository.showLoadingButton();
                                    Toast.makeText(context,"Please login with your facebook account",Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    break;
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally{
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        VolleyInstance.getInstance(context).addToRequestQueue(request);
    }

    public void resetPassword(User user, final Context context) {
        String url = baseURL+"/passwordReset";

        JSONObject object = new JSONObject();
        try {
            object.put("email",user.getEmail());
            object.put("password",user.getPassword());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");

                            switch (message){
                                case "Password has been reset":
                                    Toast.makeText(context,"Your password has been reset",Toast.LENGTH_SHORT).show();
                                    iRepository.doAction();
                                    break;
                                case "Error":
                                    Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show();
                                    break;
                                default:
                                    break;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally{
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        VolleyInstance.getInstance(context).addToRequestQueue(request);
    }

    public void updateUser(User user,final Context context) {
        String url = baseURL+"/updateUser";
        iRepository.showLoadingButton();
        JSONObject object = new JSONObject();
        try {
            //if (user.getBirth_date()!=null) object.put("birth_date",user.getBirth_date());
            //if (user.getFirstName()!=null) object.put("firstName",user.getFirstName());
            //if (user.getLastName()!=null) object.put("lastName",user.getLastName());
            if (user.getEmail()!=null) object.put("user_email",user.getEmail());
            //if (user.getAddress()!=null) object.put("address",user.getAddress());
            if (user.getPhoto()!=null) object.put("photo",user.getPhoto());
            object.put("completedInformation",true);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.PUT, url, object,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            String message = response.getString("message");
                            switch (message){
                                case "Done":
                                    break;
                                default:
                                    break;
                            }
                            iRepository.doAction();
                            iRepository.dismissLoadingButton();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }finally{
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        VolleyInstance.getInstance(context).addToRequestQueue(request);
    }

    public User convertJsonToObject(JSONObject jsonObject) {
        try {
            User user = new User();
            user.setId(jsonObject.getString("_id"));
            user.setFirstName(jsonObject.getString("firstName"));
            user.setLastName(jsonObject.getString("lastName"));
            user.setEmail(jsonObject.getString("email"));
            user.setAddress(jsonObject.getString("address"));
            user.setPhone(jsonObject.getString("phone"));
            user.setPhoto(jsonObject.getString("photo"));

            user.setSign_up_date(getDate(jsonObject.getString("sign_up_date")));
            if (jsonObject.has("birth_date")){
                user.setBirth_date(getDate(jsonObject.getString("birth_date")));
            }
            user.setSigned_up_with(jsonObject.getString("signed_up_with"));
            return user;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
