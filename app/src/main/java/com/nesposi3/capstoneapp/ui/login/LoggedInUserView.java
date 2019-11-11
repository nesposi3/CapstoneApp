package com.nesposi3.capstoneapp.ui.login;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String displayName;
    private String hash;
    //... other data fields that may be accessible to the UI

    LoggedInUserView(String displayName,String hash) {
        this.displayName = displayName;
        this.hash = hash;
    }

    String getDisplayName() {
        return displayName;
    }

    public String getHash() {
        return hash;
    }
}
