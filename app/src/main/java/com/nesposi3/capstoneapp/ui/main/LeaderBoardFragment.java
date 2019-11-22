package com.nesposi3.capstoneapp.ui.main;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Space;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nesposi3.capstoneapp.R;
import com.nesposi3.capstoneapp.data.model.GameState;
import com.nesposi3.capstoneapp.data.model.Player;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

public class LeaderBoardFragment extends RefreshableFragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private final String TAG = "LeaderBoardFragment";
    private PageViewModel pageViewModel;
    private GameState state;
    private String name;
    private String hash;

    public static LeaderBoardFragment newInstance(int index) {
        LeaderBoardFragment fragment = new LeaderBoardFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(ARG_SECTION_NUMBER, index);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pageViewModel = ViewModelProviders.of(this).get(PageViewModel.class);
        int index = 1;
        if (getArguments() != null) {
            index = getArguments().getInt(ARG_SECTION_NUMBER);
        }

        pageViewModel.setIndex(index);
    }
    private void setUpLeaderBoard(View v){
        SwipeRefreshLayout swipeRefreshLayout = v.findViewById(R.id.leaderRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listener.onRefresh(1);
            }
        });
        LinearLayout linearLayout = (LinearLayout) v.findViewById(R.id.leaderboardLinear);
        if(state==null){
            return;
        }
        Player[] players = state.getPlayers();
        Arrays.sort(players);
        for (int i = 0; i <players.length ; i++) {
            String place = (i+1) + ". ";
            CardView cardView = new CardView(v.getContext());
            CardView.LayoutParams params = (
                    new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT,CardView.LayoutParams.WRAP_CONTENT)
            );
            params.setMargins(5,5,5,5);
            cardView.setLayoutParams(params);
            TextView textView = new TextView(v.getContext());
            textView.setText(place+ players[i].getName());
            textView.setTextSize(30);
            TextView money = new TextView(v.getContext());
            money.setTextSize(20);
            money.setText("Liquid equity: " + players[i].getTotalCashDollarValue());
            money.setGravity(Gravity.RIGHT);
            LinearLayout insideRow = new LinearLayout(v.getContext());
            LinearLayout.LayoutParams insideRowParams = (
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT)
                    );
            insideRow.setLayoutParams(insideRowParams);
            insideRow.setOrientation(LinearLayout.VERTICAL);
            insideRow.addView(textView);
            insideRow.addView(money);
            cardView.addView(insideRow);
            //Frst place, gold color card
            if(i==0){
                cardView.setCardBackgroundColor(ContextCompat.getColor(v.getContext(),R.color.cardFirstPlace));
            }else if(i==1){
                cardView.setCardBackgroundColor(ContextCompat.getColor(v.getContext(),R.color.cardSecondPlace));

            }else if(i==2){
                cardView.setCardBackgroundColor(ContextCompat.getColor(v.getContext(),R.color.cardThirdPlace));
            }
            linearLayout.setPadding(10,10,10,10);
            linearLayout.addView(cardView);
        }
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_leaderboard, container, false);
        MainActivity activity = (MainActivity) getActivity();
        state = activity.gameState;
        name = activity.userName;
        hash = activity.hash;
        setUpLeaderBoard(root);

        return root;
    }

}
