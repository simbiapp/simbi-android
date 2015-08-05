package org.simbi.simbiapp.api.interfaces;

import com.squareup.otto.Bus;

public interface UserClient {

    public void login(final String username, final String password);

    public Bus getBus();
}
