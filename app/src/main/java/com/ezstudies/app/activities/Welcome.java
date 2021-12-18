package com.ezstudies.app.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.ezstudies.app.Database;
import com.ezstudies.app.Login;
import com.ezstudies.app.R;
import com.ezstudies.app.WelcomeFragment;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;

public class Welcome extends FragmentActivity {
    public static final String USER_AGENT = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/95.0.4638.69 Safari/537.36";
    public static final String LOGIN_FORM_URL  = "https://services-web.u-cergy.fr/calendar/LdapLogin";
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private ViewPager2 viewPager;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPreferences = getSharedPreferences("prefs", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();

        Boolean firstTime = sharedPreferences.getBoolean("first_time", true);

        if(firstTime){
            viewPager = new ViewPager2(this);
            FragmentStateAdapter pagerAdapter = new ScreenSlidePagerAdapter(this);
            viewPager.setAdapter(pagerAdapter);
            setContentView(viewPager);
        }
        else {
            finish();
            startActivity(new Intent(this, Overview.class));
        }
    }

    public void login (View view) throws InterruptedException {
        EditText eName = findViewById(R.id.welcome_name);
        EditText ePassword = findViewById(R.id.welcome_password);
        String name = eName.getText().toString();
        String password = ePassword.getText().toString();

        Login login = new Login(name, password);
        login.start();
        login.join();

        if(login.isSuccess()) {

            editor.putString("name", name);
            editor.putString("password", password);
            editor.putBoolean("connected", true);
            editor.apply();

            Toast.makeText(this, getString(R.string.login_succes), Toast.LENGTH_SHORT).show();
        }
        else{
            String response = login.getResponseUrl();
            String text;
            if (response.equals("https://services-web.u-cergy.fr/calendar/LdapLogin/Logon")) {
                text = getString(R.string.login_fail_credentials);
            }
            else {
                text = getString(R.string.login_fail_network);
            }
            Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
        }
    }

    public void enter(View view){
        int mode = sharedPreferences.getInt("travel_mode", -1);
        Boolean condition1 = false;
        Boolean condition2 = false;
        int prep_time;
        switch (mode){
            case 0: //driving
            case 1: //walking
                String home_latitude = sharedPreferences.getString("home_latitude", null);
                String home_longitude = sharedPreferences.getString("home_longitude", null);
                String school_latitude = sharedPreferences.getString("school_latitude", null);
                String school_longitude = sharedPreferences.getString("school_longitude", null);
                prep_time = sharedPreferences.getInt("prep_time", -1);
                if(home_latitude != null && home_longitude != null && school_latitude != null && school_longitude != null && prep_time != -1) {
                    condition1 = true;
                }
                break;
            case 2: // transit
                prep_time = sharedPreferences.getInt("prep_time", -1);
                int travel_time = sharedPreferences.getInt("travel_time", -1);
                if (prep_time != -1 && travel_time != -1){
                    condition1 = true;
                }
                break;
            case -1: //no mode
                break;
        }
        int import_mode = sharedPreferences.getInt("import_mode", -1);
        switch (import_mode){
            case 0: //celcat
                Boolean connected = sharedPreferences.getBoolean("connected", false);
                if(connected){
                    condition2 = true;
                }
                else {
                    condition2 = false;
                }
                break;
            case 1: //ics
                condition2 = true;
                break;
            case -1:
                break;
        }
        Log.d("conditions", condition1 + " " + condition2);

        Boolean conditions = condition1 && condition2;
        if(conditions){
            editor.putBoolean("first_time", false);
            editor.apply();
            finish();
            startActivity(new Intent(this, Overview.class));
        }
        else{
            Toast.makeText(this, R.string.finish_setup, Toast.LENGTH_SHORT).show();
        }
    }

    private class ScreenSlidePagerAdapter extends FragmentStateAdapter {
        public ScreenSlidePagerAdapter(FragmentActivity fragmentActivity) {
            super(fragmentActivity);
        }

        @Override
        public Fragment createFragment(int position) {
            Fragment page = null;
            switch (position){
                case 0:
                    page =  new WelcomeFragment(1);
                    break;
                case 1:
                    page =  new WelcomeFragment(2);
                    break;
                case 2:
                    page =  new WelcomeFragment(3);
                    break;
                default:
                    break;
            }
            return page;
        }

        @Override
        public int getItemCount() {
            return 3;
        }
    }
}
