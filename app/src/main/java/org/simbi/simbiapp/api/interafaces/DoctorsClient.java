package org.simbi.simbiapp.api.interafaces;

import com.squareup.otto.Bus;

public interface DoctorsClient {

    public void getDoctors(String token);

    public void getDoctorsById(String id, String token);

    public Bus getBus();
}
