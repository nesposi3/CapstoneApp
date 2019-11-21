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
import androidx.lifecycle.ViewModelProviders;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.nesposi3.capstoneapp.R;
import com.nesposi3.capstoneapp.data.StaticUtils;
import com.nesposi3.capstoneapp.data.model.GameState;
import com.nesposi3.capstoneapp.data.model.Player;
import com.nesposi3.capstoneapp.data.model.Stock;
import com.nesposi3.capstoneapp.ui.stockPages.StockScreen;

public class MarketFragment extends RefreshableFragment {
    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private GameState state;
    private String name;
    private String hash;
    public static MarketFragment newInstance(int index) {
        MarketFragment fragment = new MarketFragment();
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
        View root = inflater.inflate(R.layout.fragment_market, container, false);
        MainActivity activity = (MainActivity) getActivity();
        state = activity.gameState;
        name = activity.userName;
        hash = activity.hash;
        setUpStocks(root);
        return root;
    }
    private void setUpStocks(View v){
        SwipeRefreshLayout layout = v.findViewById(R.id.marketRefresh);
        layout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                listener.onRefresh(0);
            }
        });
        if(state==null) return;
        LinearLayout linearLayout = v.findViewById(R.id.marketLayout);
        Player player = null;
        Stock[] stocks = state.getStocks();
        final Player[] players = state.getPlayers();
        for (Player p : players) {
            if (p.getName().equals(name)) {
                player = p;
                break;
            }
        }
        final String userName = name;
        if(player==null) return;
        for(final Stock s:stocks){
            CardView stockCard = new CardView(v.getContext());
            CardView.LayoutParams cardParams = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT,CardView.LayoutParams.WRAP_CONTENT);
            cardParams.setMargins(10,10,10,10);
            stockCard.setLayoutParams(cardParams);
            LinearLayout cardLayout = new LinearLayout(v.getContext());
            LinearLayout.LayoutParams linearParams = (
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            );
            LinearLayout nameAndTrend = new LinearLayout(v.getContext());
            nameAndTrend.setLayoutParams(linearParams);
            nameAndTrend.setOrientation(LinearLayout.HORIZONTAL);
            cardLayout.setOrientation(LinearLayout.VERTICAL);
            cardLayout.setLayoutParams(linearParams);
            final TextView name = new TextView(v.getContext());
            name.setGravity(Gravity.LEFT);
            TextView price = new TextView(v.getContext());
            price.setGravity(Gravity.RIGHT);
            price.setText(StaticUtils.centsToDolars(s.getPrice()));
            name.setTextSize(30);
            name.setText(s.getName());
            ImageView trend = new ImageView(v.getContext());

            if(s.getTrend()){
                trend.setImageDrawable(v.getContext().getDrawable(R.drawable.ic_arrow_drop_up_24px));
                stockCard.setCardBackgroundColor(v.getContext().getColor(R.color.stockTrendUp));
            }else{
                trend.setImageDrawable(v.getContext().getDrawable(R.drawable.ic_arrow_drop_down_24px));
                stockCard.setCardBackgroundColor(v.getContext().getColor(R.color.stockTrendDown));
            }
            nameAndTrend.addView(trend);
            nameAndTrend.addView(name);
            cardLayout.addView(nameAndTrend);
            cardLayout.addView(price);
            stockCard.addView(cardLayout);
            final int numOwned  = player.getNumOwned(s);
            final int totalCash = player.getTotalCash();
            if(!state.isDone()){
                stockCard.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent(getActivity(), StockScreen.class);
                        i.putExtra("stockName",s.getName());
                        i.putExtra("userName",userName);
                        i.putExtra("hash",hash);
                        i.putExtra("gameID",state.getGameID());
                        i.putExtra("totalCash",String.valueOf(totalCash));
                        i.putExtra("numOwned",String.valueOf(numOwned));
                        startActivity(i);
                    }
                });
            }

            linearLayout.addView(stockCard);
        }
    }

}
