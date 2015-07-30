package org.simbi.simbiapp.api.interafaces;

import com.squareup.otto.Bus;

public interface UserClient {

    public void login(final String username, final String password);

    public Bus getBus();
}
