package com.example.asfvisibook.Model;

/**
 * Created by SHEGZ on 1/4/2018.
 */
public class Token {
    private String token;
    private boolean isServerToken;

    public boolean isServerToken() {
        return isServerToken;
    }
    public Token(){

    }

    public void setServerToken(boolean serverToken) {
        isServerToken = serverToken;
    }

    public String getToken() {

        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Token(String token, boolean isServerToken) {

        this.token = token;
        this.isServerToken = isServerToken;
    }
}
