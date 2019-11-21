package com.nesposi3.capstoneapp.ui.main;

import androidx.fragment.app.Fragment;

public abstract  class RefreshableFragment extends Fragment {
    protected RefreshListener listener;
    public void setListener(RefreshListener listener){
        this.listener = listener;
    }
}
