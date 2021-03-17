package com.maksat.participantapp.models;

import com.google.gson.annotations.SerializedName;

public class AuthBody {
    private String userName;
    private String password;

    public AuthBody(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }
}
