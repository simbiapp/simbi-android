package org.simbi.simbiapp.api.interfaces;

import com.squareup.otto.Bus;

public interface DoctorsClient {

    public void getDoctors(String token);

    public void getDoctorsById(String id, String token);

    public Bus getBus();
}
