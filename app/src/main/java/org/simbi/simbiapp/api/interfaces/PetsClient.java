package org.simbi.simbiapp.api.interfaces;

import com.squareup.otto.Bus;

import org.simbi.simbiapp.api.models.Response.Pet;

import java.util.List;

public interface PetsClient {

    public List<Pet> getPets();

    public Bus getBus();
}
