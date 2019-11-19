package com.nesposi3.capstoneapp.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.nesposi3.capstoneapp.R;
import com.nesposi3.capstoneapp.data.model.GameState;

/**
 * A placeholder fragment containing a simple view.
 */
public class PlaceholderFragment extends Fragment {

    private static final String ARG_SECTION_NUMBER = "section_number";

    private PageViewModel pageViewModel;
    private GameState state;
    public static PlaceholderFragment newInstance(int index) {
        PlaceholderFragment fragment = new PlaceholderFragment();
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
        MainActivity activity = (MainActivity) getActivity();
        state =activity.gameState;
        pageViewModel.setIndex(index);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root;
        if (getArguments().getInt(ARG_SECTION_NUMBER) == 1) {
            root = inflater.inflate(R.layout.fragment_main, container, false);

        } else if (getArguments().getInt(ARG_SECTION_NUMBER) == 2) {
            root = inflater.inflate(R.layout.fragment_main, container, false);
        } else {
            root = inflater.inflate(R.layout.fragment_main, container, false);
        }

        return root;
    }
}