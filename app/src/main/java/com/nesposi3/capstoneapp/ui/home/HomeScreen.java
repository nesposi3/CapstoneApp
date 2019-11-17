package com.nesposi3.capstoneapp.ui.home;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.gson.Gson;
import com.nesposi3.capstoneapp.ui.gameScreen.MainActivity;
import com.nesposi3.capstoneapp.R;
import com.nesposi3.capstoneapp.data.StaticUtils;
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

import android.text.Editable;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class HomeScreen extends AppCompatActivity implements LogoutDialogListener {
    private final String TAG = "HomeScreen";
    private String name;
    private String hash;
    private LinearLayout layout;
    private SwipeRefreshLayout swipeRefreshLayout;

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
        name = i.getStringExtra("name");
        hash = i.getStringExtra("hash");
        final CoordinatorLayout topLayout = findViewById(R.id.coordinatorLayout);
        final PopupWindow popupWindow = popupSetup();
        setContentView(R.layout.activity_home_screen);
        Toolbar toolbar = findViewById(R.id.toolbar);
        swipeRefreshLayout = findViewById(R.id.swipeRefresh);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
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
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                layout.removeAllViews();
                new GetGamesTask(name,hash).execute(name,hash);

            }
        });
    }
    private PopupWindow popupSetup(){
        //Popup to create/join game
        final PopupWindow popupWindow = new PopupWindow(this);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setAnimationStyle(R.style.Animation);
        popupWindow.setElevation(75);
        LinearLayout.LayoutParams popupParams =(
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT)
        );
        LinearLayout popupLayout = new LinearLayout(this);
        popupLayout.setOrientation(LinearLayout.VERTICAL);
        popupLayout.setPadding(15,15,15,15);
        CardView popupCard = new CardView(this);
        CardView.LayoutParams cardParams =(
                new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT,CardView.LayoutParams.WRAP_CONTENT)
        );
        popupCard.setLayoutParams(cardParams);
        TextView popupMessage = new TextView(this);
        popupMessage.setText("Create a new game, or join someone else's!");
        popupMessage.setGravity(Gravity.CENTER_HORIZONTAL);
        popupMessage.setTextSize(15);
        popupLayout.addView(popupMessage);
        popupLayout.setLayoutParams(popupParams);
        popupCard.addView(popupLayout);
        final EditText idField = new EditText(this);
        idField.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length()>50 || s.length() == 0){
                    idField.setError("Invalid name length, must be between 1 and 50 Characters");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        popupLayout.addView(idField);
        popupWindow.setContentView(popupCard);
        popupWindow.setFocusable(true);

        Button join = new Button(this);
        join.setText("Join");
        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(idField.getText().toString().trim().length() > 0){
                    new JoinCreateTask().execute(name,hash,idField.getText().toString(),"join");
                }
            }
        });
        Button create = new Button(this);
        create.setText("Create");

        LinearLayout buttons = new LinearLayout(this);
        buttons.setOrientation(LinearLayout.HORIZONTAL);
        buttons.addView(join);
        buttons.addView(create);
        popupLayout.addView(buttons);
        return popupWindow;

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

    @Override
    public void onBackPressed() {
        logoutConfirm();
    }

    @Override
    protected void onResume() {
        super.onResume();
        new GetGamesTask(name,hash).execute(name,hash);
    }

    private class GetGamesTask extends AsyncTask<String,Boolean,GameState[]> {
        private final String hash;
        private final String userName;

        public GetGamesTask(String name, String hash) {
            this.userName = name;
            this.hash = hash;
        }

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
                    GameState[] gameStates = gson.fromJson(json, GameState[].class);
                    return gameStates;
                }
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(GameState[] gameStates) {
            if (gameStates == null) {
                return;
            }
            layout.removeAllViews();
            for (final GameState g : gameStates) {
                LinearLayout cardLinear = new LinearLayout(HomeScreen.this);
                LinearLayout.LayoutParams linearParams = (
                        new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
                );
                cardLinear.setOrientation(LinearLayout.VERTICAL);
                cardLinear.setLayoutParams(linearParams);
                CardView cardView = new CardView(HomeScreen.this);
                CardView.LayoutParams cardParams = (
                        new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT)
                );
                cardParams.setMargins(0, 20, 0, 20);
                cardView.setLayoutParams(cardParams);
                final TextView name = new TextView(HomeScreen.this);
                name.setPadding(10, 0, 0, 0);
                name.setTextSize(40);
                name.setText(g.getGameID());
                TextView timeLeft = new TextView(HomeScreen.this);
                timeLeft.setPadding(10, 0, 0, 0);
                timeLeft.setTextSize(20);
                if (g.getTicksLeft() == 0) {
                    timeLeft.setText(getString(R.string.game_finished));
                } else {
                    timeLeft.setText(StaticUtils.minutesToTimeLeft(g.getTicksLeft()));
                }
                cardLinear.addView(name);
                cardLinear.addView(timeLeft);
                cardView.addView(cardLinear);
                layout.addView(cardView);
                layout.invalidate();
                layout.refreshDrawableState();
                cardView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(HomeScreen.this, MainActivity.class);
                        i.putExtra("name", userName);
                        i.putExtra("hash", hash);
                        i.putExtra("gameID", g.getGameID());
                        HomeScreen.this.startActivity(i);
                    }
                });
            }
            swipeRefreshLayout.setRefreshing(false);
        }
    }
    private class JoinCreateTask extends AsyncTask<String,String,String> {


        @Override
        protected void onPreExecute() {
            swipeRefreshLayout.setRefreshing(true);
        }

        @Override
        protected String doInBackground(String... strings) {
            String name = strings[0];
            String hash = strings[1];
            String gameID = strings[2];
            String joinOrCreate = strings[3];
            try {
                URL apiUrl = new URL(getString(R.string.server_url) + "game/" + gameID + joinOrCreate + "/" + name + "-" + hash);
                HttpURLConnection con = (HttpURLConnection) apiUrl.openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(5000);
                con.setReadTimeout(5000);
                int code = con.getResponseCode();
                con.connect();
                // Error code
                if (code > 399 && code < 500) {
                    if(code == 409){
                        return "Game with id " + gameID + " already exists";
                    }else{
                        return "Error joining game " + gameID;
                    }
                } else {
                    return "Success";
                }
            } catch (Exception e) {
                return "Failure";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(HomeScreen.this,s, Toast.LENGTH_LONG).show();

        }
    }

}

