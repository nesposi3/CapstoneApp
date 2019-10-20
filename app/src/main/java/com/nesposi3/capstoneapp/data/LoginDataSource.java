package com.nesposi3.capstoneapp.data;

import android.content.res.Resources;
import android.util.Log;

import com.google.android.gms.common.util.Hex;
import com.nesposi3.capstoneapp.data.model.LoggedInUser;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    private static final String SERVER_URL = "http://localhost:8090";
    private static final String TAG = "LoginDataSource: ";
    private String shaHash(String pass){
        String hash = "";
        try{
            MessageDigest messageDigest =  MessageDigest.getInstance("SHA-1");
            messageDigest.update(pass.getBytes("UTF-8"));
            hash = Hex.bytesToStringUppercase(messageDigest.digest());
        }catch (NoSuchAlgorithmException e){
            Log.e(TAG, "shaHash: ", e);
        }
        catch (UnsupportedEncodingException e){
            Log.e(TAG, "shaHash: ", e);
        }
        return hash;

    }

    public Result<LoggedInUser> login(String username, String password) {
        String hash =  shaHash(password);

        try {
            // TODO: handle loggedInUser authentication
            LoggedInUser fakeUser =
                    new LoggedInUser(
                            java.util.UUID.randomUUID().toString(),
                            "Jane Doe");
            return new Result.Success<>(fakeUser);
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
