package com.nesposi3.capstoneapp.ui.main;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.gson.Gson;
import com.nesposi3.capstoneapp.R;
import com.nesposi3.capstoneapp.data.model.GameState;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements RefreshListener {
    protected String userName;
    protected String hash;
    protected String gameID;
    protected GameState gameState;
    private final String TAG = "MainActivity";
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_game, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onAttachFragment(Fragment fragment) {
        if(fragment instanceof RefreshableFragment){
            RefreshableFragment fragment1 = (RefreshableFragment) fragment;
            fragment1.setListener(this);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()== R.id.refresh){
            TabLayout tabs = findViewById(R.id.tabs);
            int selected = tabs.getSelectedTabPosition();
            new GetGameInfoTask(selected).execute(userName,hash,gameID);
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent currIntent = getIntent();
        userName = currIntent.getStringExtra("name");
        hash = currIntent.getStringExtra("hash");
        gameID = currIntent.getStringExtra("gameID");
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Game: " + gameID);
        new GetGameInfoTask(0).execute(userName,hash,gameID);
    }
    private void setUpTabs(int index){
        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);
        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);
        tabs.getTabAt(index).select();
    }

    @Override
    public void onRefresh(int i) {
        new GetGameInfoTask(i).execute(userName,hash,gameID);
    }

    private class GetGameInfoTask extends AsyncTask<String, GameState, GameState> {
        private int tabIndex;
        public GetGameInfoTask(int i){
            tabIndex = i;
        }
        @Override
        protected GameState doInBackground(String... strings) {
            String name = strings[0];
            String hash = strings[1];
            String gameID = strings[2];
            try {
                URL apiUrl = new URL(getString(R.string.server_url) + "game/" + gameID + "/getGameStatus/" + name + "-" + hash);
                HttpURLConnection con = (HttpURLConnection) apiUrl.openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(5000);
                con.setReadTimeout(5000);
                int code = con.getResponseCode();
                con.connect();
                InputStream stream = con.getInputStream();
                BufferedReader reader = new BufferedReader(new InputStreamReader(stream));
                String line = "";
                StringBuilder builder = new StringBuilder();
                while ((line = reader.readLine()) != null) {
                    builder.append(line);
                }
                String json = builder.toString();
                Gson gson = new Gson();
                // Error code
                if (code > 399 && code < 500) {
                    return null;
                } else {
                    GameState gameState = gson.fromJson(json, GameState.class);
                    return gameState;
                }
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(GameState gameState) {
            MainActivity.this.gameState = gameState;
            setUpTabs(tabIndex);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        TabLayout tabs = findViewById(R.id.tabs);
        int i = tabs.getSelectedTabPosition();
        new GetGameInfoTask(i).execute(userName,hash,gameID);
        Log.d(TAG, "onConfigurationChanged: ");
        super.onConfigurationChanged(newConfig);
    }
}