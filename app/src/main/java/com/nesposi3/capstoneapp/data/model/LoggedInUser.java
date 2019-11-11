package com.nesposi3.capstoneapp.data.model;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String userId;
    private String passHash;

    public LoggedInUser(String userId, String passHash) {
        this.userId = userId;
        this.passHash = passHash;
    }

    public String getUserId() {
        return userId;
    }

    public String getPassHash() {
        return passHash;
    }
}
