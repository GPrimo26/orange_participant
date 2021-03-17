package com.maksat.participantapp.models;

import java.util.List;

public class TokenBody {
    public Integer participantId;
    public Integer eventId;
    public String access_token;
    public String username;
    public List<String> roles;
    public Integer languageId;

    public TokenBody(Integer participantId, Integer eventId, String access_token, String username, List<String> roles, Integer languageId) {
        this.participantId = participantId;
        this.eventId = eventId;
        this.access_token = access_token;
        this.username = username;
        this.roles = roles;
        this.languageId = languageId;
    }
}
