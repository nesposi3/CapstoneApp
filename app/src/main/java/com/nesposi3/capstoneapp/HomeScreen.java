package com.nesposi3.capstoneapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.nesposi3.capstoneapp.data.Result;
import com.nesposi3.capstoneapp.data.model.LoggedInUser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.security.auth.login.LoginException;

public class HomeScreen extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        String name = i.getStringExtra("name");
        String hash = i.getStringExtra("hash");
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        EditText text = findViewById(R.id.nameField2);
        text.setText("Welcome to Market Mayhem!" );
        text.setInputType(InputType.TYPE_NULL);
        TextView userName = findViewById(R.id.userGamesTop);
        userName.setText(name + "'s Games");
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        new GetGamesTask().execute(name,hash);
    }
    private class GetGamesTask extends AsyncTask<String,Boolean,Boolean>{

        @Override
        protected Boolean doInBackground(String... strings) {
            String name = strings[0];
            String hash = strings[1];
            try {
                URL apiUrl = new URL(getString(R.string.server_url) + "allGames/" + name + "-" + hash);
                HttpURLConnection con = (HttpURLConnection) apiUrl.openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(5000);
                con.setReadTimeout(5000);
                int code = con.getResponseCode();
                con.connect();
                InputStream stream =con.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String line = "";
                StringBuffer buffer = new StringBuffer();
                while ((line=reader.readLine())!=null){
                    buffer.append(line);
                }
                String json = buffer.toString();
                Log.d("HomeScreen", "doInBackground: " + json);
                // Error code
                if(code > 399 && code < 500){
                    return false;
                }else{

                    return true;
                }
            } catch (Exception e) {
                return false;
            }
        }
    }
}
