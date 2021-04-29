package com.example.myapplication;

public class Token {
    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String token) {
        this.accessToken = token;
    }
    @Override
    public String toString() {
        return "Token{" +
                "accessToken='" + accessToken + '\'' +
                '}';
    }
}
