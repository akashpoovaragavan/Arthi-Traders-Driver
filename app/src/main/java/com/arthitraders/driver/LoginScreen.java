package com.arthitraders.driver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginScreen extends AppCompatActivity {
    AppCompatButton login;
    EditText username,password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        login=findViewById(R.id.login);
        username=findViewById(R.id.username);
        password=findViewById(R.id.password);

        Helper.sharedpreferencesdriver = getSharedPreferences(Helper.MyPREFERENCES, Context.MODE_PRIVATE);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user=username.getText().toString().trim();
                if (user.isEmpty()){
                    username.setError("Username required");
                    username.requestFocus();
                }else {
                    logincheck(user);
                }
            }
        });
    }

    public void logincheck(String usernames) {
        try {
            final String username = usernames;
            final String passwords = password.getText().toString().trim();

            Helper.loading(LoginScreen.this);

            StringRequest stringRequest = new StringRequest(Request.Method.POST, Helper.login_url,
                    response -> {
                        Helper.mProgressBarHandlerdriver.hide();
                        Log.e("logincheck", response);
                        try {
                            JSONObject job = new JSONObject(response);
                            JSONArray jarr = job.getJSONArray("data");
                            JSONObject objj = jarr.getJSONObject(0);
                            if(!objj.getString("message").equals("success")) {
                                Toast.makeText(LoginScreen.this, "Invalid Username or Password", Toast.LENGTH_SHORT).show();
                            }
                            else
                            {
                                String dol_vehicle_type        = objj.getString("vehicle_type");
                                String dol_vehicle_no         = objj.getString("vehicle_no");


                                SharedPreferences.Editor ed = Helper.sharedpreferencesdriver.edit();
                                ed.putString("dol_vehicle_type", dol_vehicle_type);
                                ed.putString("dol_vehicle_no", dol_vehicle_no);
                                ed.putBoolean("FIRSTTIME_LOGIN", true);
                                ed.commit();

                                startActivity(new Intent(LoginScreen.this,HomeScreen.class));
                                overridePendingTransition(R.anim.right_enter,R.anim.left_out);
                              //  Animatoo.animateSwipeLeft(LoginScreen.this);
                                finish();
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Helper.mProgressBarHandlerdriver.hide();
                        }

                    },
                    error -> {
                        try {
                            Toast.makeText(LoginScreen.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                            Helper.mProgressBarHandlerdriver.hide();
                            Log.e("err", error.getMessage());
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("vehicle_no", username);
                    params.put("password", passwords);
                    return params;
                }
            };
            VolleySingleton.getInstance(LoginScreen.this).addToRequestQueue(stringRequest);
        } catch (Exception e) {
            e.printStackTrace();
            Helper.mProgressBarHandlerdriver.hide();
        }
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }
}