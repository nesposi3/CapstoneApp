package com.nesposi3.capstoneapp.ui.main;

import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.nesposi3.capstoneapp.R;
import com.nesposi3.capstoneapp.data.StaticUtils;
import com.nesposi3.capstoneapp.data.model.GameState;
import com.nesposi3.capstoneapp.data.model.Player;
import com.nesposi3.capstoneapp.data.model.Stock;

public class MarketFragment extends Fragment {
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
        if(state==null) return;
        LinearLayout linearLayout = v.findViewById(R.id.marketLayout);
        Player player = null;
        Stock[] stocks = state.getStocks();
        Player[] players = state.getPlayers();
        for (Player p : players) {
            if (p.getName().equals(name)) {
                player = p;
                break;
            }
        }
        if(player==null) return;
        for(Stock s:stocks){
            CardView stockCard = new CardView(v.getContext());
            CardView.LayoutParams cardParams = new CardView.LayoutParams(CardView.LayoutParams.MATCH_PARENT,CardView.LayoutParams.WRAP_CONTENT);
            cardParams.setMargins(5,5,5,5);
            stockCard.setLayoutParams(cardParams);
            LinearLayout cardLayout = new LinearLayout(v.getContext());
            LinearLayout.LayoutParams linearParams = (
                    new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            );
            cardLayout.setOrientation(LinearLayout.VERTICAL);
            cardLayout.setLayoutParams(linearParams);
            TextView name = new TextView(v.getContext());
            name.setGravity(Gravity.LEFT);
            TextView price = new TextView(v.getContext());
            price.setGravity(Gravity.RIGHT);
            price.setText(StaticUtils.centsToDolars(s.getPrice()));
            name.setTextSize(30);
            name.setText(s.getName());
            cardLayout.addView(name);
            cardLayout.addView(price);
            stockCard.addView(cardLayout);
            linearLayout.addView(stockCard);
        }
    }
}
