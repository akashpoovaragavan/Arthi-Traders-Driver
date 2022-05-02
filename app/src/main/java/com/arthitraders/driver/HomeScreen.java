package com.arthitraders.driver;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;

public class HomeScreen extends AppCompatActivity {
    ChipNavigationBar chip_bottom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);
        chip_bottom = findViewById(R.id.chip_bottom);
        chip_bottom.setItemEnabled(R.id.home_nav,true);
        chip_bottom.setItemSelected(R.id.home_nav,true);
        chip_bottom.requestFocus(R.id.home_nav);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new Fragment_Home()).commit();
        chip_bottom.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int id) {
                Fragment selectedfrag = null;
                switch (id) {
                    case R.id.home_nav:
                        selectedfrag = new Fragment_Home();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedfrag).commit();
                        break;
                    case R.id.profile_nav:
                        selectedfrag = new Fragment_Profile();
                        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, selectedfrag).commit();
                        break;
                    case R.id.logout_nav:
                       // showlogout();
                        logout();
                }
            }
        });
    }

    private void showlogout() {
        AlertDialog.Builder builder=new AlertDialog.Builder(this);
        builder.setMessage("Are you want to logout?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        startActivity(new Intent(HomeScreen.this,LoginScreen.class));
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
        AlertDialog alertDialog=builder.create();
        alertDialog.show();
    }

    private void logout()
    {

        AlertDialog.Builder builders = new AlertDialog.Builder(HomeScreen.this);
        builders.setTitle("Logout");
        builders.setMessage("Are you sure you want to logout?");
        builders.setPositiveButton("Yes", (dialog, which) -> {
            Helper.sharedpreferencesdriver = getSharedPreferences(Helper.MyPREFERENCES, Context.MODE_PRIVATE);
            SharedPreferences.Editor ed = Helper.sharedpreferencesdriver.edit();
            ed.putBoolean("FIRSTTIME_LOGIN", false);
            ed.clear();
            ed.apply();
            startActivity(new Intent(getApplicationContext(), LoginScreen.class));
            finish();
        });
        builders.setNegativeButton("No", (dialog, which) -> {
            dialog.cancel();
        });
        builders.show();
    }

    @Override
    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

}