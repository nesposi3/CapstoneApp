package com.nesposi3.capstoneapp.data;

import android.content.res.Resources;
import android.util.Log;

import com.google.android.gms.common.util.Hex;
import com.nesposi3.capstoneapp.data.model.LoggedInUser;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.security.auth.login.LoginException;

/**
 * Class that handles authentication w/ login credentials and retrieves user information.
 */
public class LoginDataSource {
    private static final String SERVER_URL = "http://3.83.107.69:8090/";
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
            URL apiUrl = new URL(SERVER_URL + "login/" + username + "-" + hash);
            LoggedInUser fakeUser = new LoggedInUser(username, hash);
            HttpURLConnection con = (HttpURLConnection) apiUrl.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            int code = con.getResponseCode();
            // Error code
            if(code > 399 && code < 500){
                return new Result.Error(new LoginException("Incorrect username or password"));
            }else{
                return new Result.Success<>(fakeUser);
            }
        } catch (Exception e) {
            return new Result.Error(new IOException("Error logging in", e));
        }
    }
    public Result<LoggedInUser> register(String username, String password) {
        String hash =  shaHash(password);
        try {
            URL apiUrl = new URL(SERVER_URL + "register/" + username + "-" + hash);
            LoggedInUser fakeUser = new LoggedInUser(username, hash);
            HttpURLConnection con = (HttpURLConnection) apiUrl.openConnection();
            con.setRequestMethod("GET");
            con.setConnectTimeout(5000);
            con.setReadTimeout(5000);
            int code = con.getResponseCode();
            // Error code
            Log.d(TAG, "register: " + code);
            if(code > 399 && code < 500){
                return new Result.Error(new LoginException("User Already Exists"));
            }else{
                return new Result.Success<>(fakeUser);
            }
        } catch (Exception e) {
            Log.e(TAG, "register: ",e );
            return new Result.Error(new IOException("Error registering", e));
        }
    }

    public void logout() {
        // TODO: revoke authentication
    }
}
