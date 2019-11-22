package com.nesposi3.capstoneapp.ui.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nesposi3.capstoneapp.R;
import com.nesposi3.capstoneapp.data.StaticUtils;
import com.nesposi3.capstoneapp.data.model.Dividend;
import com.nesposi3.capstoneapp.data.model.GameState;
import com.nesposi3.capstoneapp.data.model.Player;
import com.nesposi3.capstoneapp.data.model.Stock;
import com.nesposi3.capstoneapp.ui.stockPages.StockScreen;

public class UserInfoFragment extends RefreshableFragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private GameState state;
    private String name;
    private String hash;
    private Player user;

    public static UserInfoFragment newInstance(int index) {
        UserInfoFragment fragment = new UserInfoFragment();
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

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_user, container, false);
        MainActivity activity = (MainActivity) getActivity();
        state = activity.gameState;
        name = activity.userName;
        hash = activity.hash;
        user = activity.gameState.getPlayer(name);
        setupUser(root);
        return root;
    }
    private void setupUser(View v){
        SwipeRefreshLayout swipeRefreshLayout = v.findViewById(R.id.userRefresh);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listener.onRefresh(2);
            }
        });
        TextView title = v.findViewById(R.id.userTitle);
        title.setText(name +"'s profile.");
        TextView totalCash = v.findViewById(R.id.totalCash);
        totalCash.setText("Liquid Equity: " +user.getTotalCashDollarValue());
        TextView totalNonLiquid = v.findViewById(R.id.totalNonLiquid);
        totalNonLiquid.setText("Portfolio Value: " + user.getPortfolioDollarValue());
        TextView totalEquity = v.findViewById(R.id.overallTotal);
        totalEquity.setText("Total equity: " + user.getTotalEquityDollarValue() );
        LinearLayout portfolioLayout = v.findViewById(R.id.portfolioLayout);
        for(Dividend d: user.getPortfolio()){
            if(d.getNumShares()==0){
                continue;
            }
            Stock s = d.getBoughtStock();
            CardView dividendCard = new CardView(v.getContext());
            LinearLayout cardLayout = new LinearLayout(v.getContext());
            CardView.LayoutParams cardParams =(
                    new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT,CardView.LayoutParams.WRAP_CONTENT)
            );
            cardParams.setMargins(5,15,5,15);
            dividendCard.setLayoutParams(cardParams);
            LinearLayout.LayoutParams linearParams =(
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,LinearLayout.LayoutParams.MATCH_PARENT)
            );
            cardLayout.setOrientation(LinearLayout.VERTICAL);
            cardLayout.setLayoutParams(linearParams);
            dividendCard.addView(cardLayout);
            TextView stockName = new TextView(v.getContext());
            stockName.setText(s.getName());
            stockName.setTextSize(20);
            TextView numOwned = new TextView(v.getContext());
            numOwned.setTextSize(15);
            numOwned.setText("You own: " + d.getNumShares()+ " shares");
            numOwned.setGravity(Gravity.RIGHT);
            cardLayout.addView(stockName);
            cardLayout.addView(numOwned);
            TextView value = new TextView(v.getContext());
            value.setGravity(Gravity.RIGHT);
            value.setText("Worth: " + d.getDollarValue());
            cardLayout.addView(value);
            portfolioLayout.addView(dividendCard);
        }
    }
}
