package org.simbi.simbiapp.events.user;

import org.simbi.simbiapp.events.Event;

public class UserLoginEvent extends Event {

    private String userName, token;

    public UserLoginEvent(String user, String token) {
        this.userName = user;
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public String getToken() {
        return token;
    }
}
