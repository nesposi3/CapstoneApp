package com.nesposi3.capstoneapp.ui.home;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.nesposi3.capstoneapp.R;
import com.nesposi3.capstoneapp.data.model.GameState;
import com.nesposi3.capstoneapp.ui.home.logoutConfirm.LogoutDialogFragment;
import com.nesposi3.capstoneapp.ui.home.logoutConfirm.LogoutDialogListener;
import com.nesposi3.capstoneapp.ui.login.LoginActivity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.text.InputType;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HomeScreen extends AppCompatActivity implements LogoutDialogListener {
    private final String TAG = "HomeScreen";
    private LinearLayout layout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private boolean popupOpen = false;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId()==R.id.logout){
            this.logoutConfirm();
        }
        return true;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent i = getIntent();
        final String name = i.getStringExtra("name");
        final String hash = i.getStringExtra("hash");
        final CoordinatorLayout topLayout = findViewById(R.id.coordinatorLayout);
        //Popup to create/join game
        final PopupWindow popupWindow = new PopupWindow(this);
        LinearLayout.LayoutParams popupParams =(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT)
        );
        LinearLayout popupLayout = new LinearLayout(this);
        TextView popupMessage = new TextView(this);
        popupMessage.setText("Create a new game, or join someone else's!");
        popupLayout.setLayoutParams(popupParams);
        popupLayout.addView(popupMessage);
        popupWindow.setContentView(popupLayout);
        popupWindow.setFocusable(true);

        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        EditText text = findViewById(R.id.nameField2);
        text.setText(getString(R.string.welcomeMessage));
        text.setInputType(InputType.TYPE_NULL);
        TextView userName = findViewById(R.id.userGamesTop);
        userName.setText(name + "'s Games");
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popupWindow.showAtLocation(swipeRefreshLayout, Gravity.CENTER,0,0);
            }
        });
        layout = findViewById(R.id.linearLayout);
        new GetGamesTask().execute(name,hash);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                layout.removeAllViews();
                new GetGamesTask().execute(name,hash);

            }
        });
    }
    private void logoutConfirm(){
        FragmentManager manager = getSupportFragmentManager();
        LogoutDialogFragment dialogFragment = new LogoutDialogFragment();
        dialogFragment.show(manager,TAG);
    }
    public void logout(){
        Intent i = new Intent(HomeScreen.this, LoginActivity.class);
        HomeScreen.this.startActivity(i);
        this.finish();
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        logout();
    }

    @Override
    public void onDialogNegativeClick(DialogFragment dialog) {

    }

    private class GetGamesTask extends AsyncTask<String,Boolean,GameState[]>{
        @Override
        protected void onPreExecute() {
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected GameState[] doInBackground(String... strings) {
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
                StringBuilder builder = new StringBuilder();
                while ((line=reader.readLine())!=null){
                    builder.append(line);
                }
                String json = builder.toString();
                Gson gson = new Gson();
                // Error code
                if(code > 399 && code < 500){
                    return null;
                }else{
                    GameState[] gameStates = gson.fromJson(json,GameState[].class);
                    return gameStates;
                }
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(GameState[] gameStates) {
            if(gameStates==null){
                return;
            }
            for(GameState g:gameStates){
                Log.d(TAG, "onPostExecute: " + g.getGameID());
                CardView card2 = new CardView(HomeScreen.this);
                CardView cardView = new CardView(HomeScreen.this);
                CardView.LayoutParams cardParams =(
                        new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT,CardView.LayoutParams.WRAP_CONTENT)
                );
                cardView.setLayoutParams(cardParams);
                card2.setLayoutParams(cardParams);
                TextView name = new TextView(HomeScreen.this);
                name.setText(g.getGameID());
                cardView.addView(name);
                layout.addView(cardView);
                layout.addView(card2);
                layout.invalidate();
                layout.refreshDrawableState();
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    }


}

