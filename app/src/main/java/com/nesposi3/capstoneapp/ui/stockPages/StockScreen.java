package com.nesposi3.capstoneapp.ui.stockPages;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;


import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.DefaultValueFormatter;
import com.github.mikephil.charting.utils.EntryXComparator;
import com.google.gson.Gson;
import com.nesposi3.capstoneapp.R;
import com.nesposi3.capstoneapp.data.model.GameState;
import com.nesposi3.capstoneapp.data.model.Player;
import com.nesposi3.capstoneapp.data.model.Stock;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class StockScreen extends AppCompatActivity {
    private GameState gameState;
    private String name;
    private String hash;
    private int numOwned;
    private int totalCash;
    private int price;
    private String stockName;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_stock_screen, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stock_screen);
        Intent i = getIntent();
        String stockName = i.getStringExtra("stockName");
        Toolbar toolbar = findViewById(R.id.toolbar3);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle(stockName);
        int numOwned = Integer.parseInt(i.getStringExtra("numOwned"));
        String uName = i.getStringExtra("userName");
        String hash = i.getStringExtra("hash");
        String gameID = i.getStringExtra("gameID");
        int totalCash = Integer.parseInt(i.getStringExtra("totalCash"));
        new GetGameInfoTask(numOwned, stockName, uName, hash).execute(uName, hash, gameID);
        this.name = uName;
        this.hash = hash;
        this.numOwned = numOwned;
        this.totalCash = totalCash;

    }

    private PopupWindow sellPopupSetup() {

        final PopupWindow popupWindow = new PopupWindow(this);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setAnimationStyle(R.style.Animation);
        popupWindow.setElevation(75);
        LinearLayout.LayoutParams popupParams = (
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        );
        LinearLayout popupLayout = new LinearLayout(this);
        popupLayout.setOrientation(LinearLayout.VERTICAL);
        popupLayout.setPadding(15, 15, 15, 15);
        CardView popupCard = new CardView(this);
        CardView.LayoutParams cardParams = (
                new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT)
        );
        popupCard.setLayoutParams(cardParams);
        TextView popupMessage = new TextView(this);
        popupMessage.setText("How many shares to sell?");
        popupMessage.setGravity(Gravity.CENTER_HORIZONTAL);
        popupMessage.setTextSize(15);
        popupLayout.addView(popupMessage);
        popupLayout.setLayoutParams(popupParams);
        popupCard.addView(popupLayout);
        final NumberPicker numberPicker = new NumberPicker(this);
        numberPicker.setMaxValue(numOwned);
        numberPicker.setEnabled(true);
        numberPicker.setOnValueChangedListener(new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker, int oldVal, int newVal) {

            }
        });
        Button confirm = new Button(this);
        confirm.setText("Confirm");
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new SellStockTask().execute(name, hash, gameState.getGameID(), stockName, String.valueOf(numberPicker.getValue()));
                new GetGameInfoTask(numOwned, stockName, name, hash).execute(name, hash, gameState.getGameID());
                popupWindow.dismiss();
            }
        });
        popupLayout.addView(numberPicker);
        popupLayout.addView(confirm);
        popupWindow.setContentView(popupCard);
        popupWindow.setFocusable(true);
        return popupWindow;
    }

    private PopupWindow buyPopupSetup() {
        final PopupWindow popupWindow = new PopupWindow(this);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        popupWindow.setAnimationStyle(R.style.Animation);
        popupWindow.setElevation(75);
        LinearLayout.LayoutParams popupParams = (
                new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)
        );
        LinearLayout popupLayout = new LinearLayout(this);
        popupLayout.setOrientation(LinearLayout.VERTICAL);
        popupLayout.setPadding(15, 15, 15, 15);
        CardView popupCard = new CardView(this);
        CardView.LayoutParams cardParams = (
                new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT, CardView.LayoutParams.WRAP_CONTENT)
        );
        popupCard.setLayoutParams(cardParams);
        TextView popupMessage = new TextView(this);
        popupMessage.setText("How many shares to buy?");
        popupMessage.setGravity(Gravity.CENTER_HORIZONTAL);
        popupMessage.setTextSize(15);
        popupLayout.addView(popupMessage);
        popupLayout.setLayoutParams(popupParams);
        popupCard.addView(popupLayout);
        final NumberPicker numberPicker = new NumberPicker(this);
        int maxCanBuy = (totalCash / price);
        numberPicker.setMaxValue(maxCanBuy);
        numberPicker.setEnabled(true);
        popupLayout.addView(numberPicker);

        Button confirm = new Button(this);
        confirm.setText("Confirm");
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new BuyStockTask().execute(name, hash, gameState.getGameID(), stockName, String.valueOf(numberPicker.getValue()));
                popupWindow.dismiss();
                new GetGameInfoTask(numOwned, stockName, name, hash).execute(name, hash, gameState.getGameID());
            }
        });
        popupLayout.addView(confirm);

        popupWindow.setContentView(popupCard);
        popupWindow.setFocusable(true);
        return popupWindow;
    }

    private class GetGameInfoTask extends AsyncTask<String, GameState, GameState> {
        public GetGameInfoTask(int numOwned, String stockName, String name, String hash) {
            this.numOwned = numOwned;
            this.stockName = stockName;
            this.name = name;
            this.hash = hash;
        }

        private int numOwned;
        private String stockName;
        private String name;
        private String hash;

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
            StockScreen.this.gameState = gameState;
            Stock s = gameState.getStock(stockName);
            StockScreen.this.price = s.getPrice();
            StockScreen.this.stockName = stockName;
            Player p = gameState.getPlayer(name);
            StockScreen.this.numOwned = p.getNumOwned(s);
            StockScreen.this.totalCash = p.getTotalCash();
            this.numOwned = p.getNumOwned(s);
            Log.d("StockScreen", "onPostExecute: " + s.getPrice());
            TextView title = findViewById(R.id.stockScreenName);
            TextView num = findViewById(R.id.stockScreenNum);
            title.setText("Stock: " + stockName + ".");
            num.setText("You own " + numOwned + ((numOwned == 1) ? " share." : " shares."));
            Button sell = findViewById(R.id.button);
            Button buy = findViewById(R.id.button2);
            final ConstraintLayout layout = findViewById(R.id.constraintLayout);
            final PopupWindow buyWindow = buyPopupSetup();
            final PopupWindow sellWindow = sellPopupSetup();
            sell.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    sellWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);
                }
            });
            buy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    buyWindow.showAtLocation(layout, Gravity.CENTER, 0, 0);

                }
            });
            new GetHistoryClass().execute(name, hash, gameState.getGameID());
        }
    }

    private class SellStockTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            String name = strings[0];
            String hash = strings[1];
            String gameID = strings[2];
            String stockName = strings[3];
            String num = strings[4];
            try {
                URL apiUrl = (
                        new URL(getString(R.string.server_url) + "game/" + gameID + "/sell/" +
                                name + "-" + hash + "-" + stockName + "-" + num)
                );
                HttpURLConnection con = (HttpURLConnection) apiUrl.openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(5000);
                con.setReadTimeout(5000);
                int code = con.getResponseCode();
                // Error code
                if (code > 399 && code < 500) {
                    return "Error selling stocks";
                } else {
                    return "Success";
                }
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(StockScreen.this, s, Toast.LENGTH_LONG).show();
        }
    }

    private class BuyStockTask extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... strings) {
            String name = strings[0];
            String hash = strings[1];
            String gameID = strings[2];
            String stockName = strings[3];
            String num = strings[4];
            try {
                URL apiUrl = (
                        new URL(getString(R.string.server_url) + "game/" + gameID + "/buy/" +
                                name + "-" + hash + "-" + stockName + "-" + num)
                );
                HttpURLConnection con = (HttpURLConnection) apiUrl.openConnection();
                con.setRequestMethod("GET");
                con.setConnectTimeout(5000);
                con.setReadTimeout(5000);
                int code = con.getResponseCode();
                // Error code
                if (code > 399 && code < 500) {
                    return "Error selling stocks";
                } else {
                    return "Success";
                }
            } catch (Exception e) {
                return null;
            }
        }

        @Override
        protected void onPostExecute(String s) {
            Toast.makeText(StockScreen.this, s, Toast.LENGTH_LONG).show();
        }
    }

    private class GetHistoryClass extends AsyncTask<String, String, GameState[]> {
        //Total game time in minutes
        private final int totalTime = 7200;

        @Override
        protected GameState[] doInBackground(String... strings) {
            String name = strings[0];
            String hash = strings[1];
            String gameID = strings[2];
            try {
                URL apiUrl = new URL(getString(R.string.server_url) + "game/" + gameID + "/getGameHistory/" + name + "-" + hash);
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
            String stockName = StockScreen.this.stockName;
            TextView title = findViewById(R.id.graphTitle);
            title.setText("Stock info for " +stockName + " in the last hour");
            List<Entry> entries = new ArrayList<>();
            Log.d("StockScreen", "onPostExecute: " + gameStates.length);
            int max = -1;
            int minY = Integer.MAX_VALUE;
            int minX = Integer.MAX_VALUE;
            int maxX = -1;
            for (int i = 0; i < gameStates.length; i++) {
                GameState g = gameStates[i];
                Stock s = g.getStock(stockName);
                int x = totalTime - g.getTicksLeft();
                int y = s.getPrice();
                if (y > max) {
                    max = y;
                } if (y < minY) {
                    minY = y;
                }
                if (x > maxX) {
                    maxX = x;
                } if (x < minX) {
                    minX = x;
                }
                Entry entry = new Entry(x, y);
                entries.add(entry);
            }
            Collections.sort(entries, new EntryXComparator());

            LineChart chart = findViewById(R.id.lineChart);
            chart.setNoDataText("No history");
            chart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
            chart.getAxisRight().setEnabled(false);
            chart.getAxisLeft().setAxisMaximum(max);
            chart.getAxisLeft().setAxisMinimum(minY);
            chart.getAxisLeft().setValueFormatter(new PriceValueFormatter());
            chart.getDescription().setEnabled(false);
            LineDataSet set = new LineDataSet(entries, "Price");
            set.setDrawValues(false);
            LineData data = new LineData(set);
            Log.d("StockScreen", "onPostExecute: " + data.getEntryCount());
            chart.setData(new LineData(set));
            chart.invalidate();
        }
    }
}
