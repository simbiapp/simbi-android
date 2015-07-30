package org.simbi.simbiapp.events.doctors;

import org.simbi.simbiapp.api.models.Response.Doctor;
import org.simbi.simbiapp.events.Event;

import java.util.List;

public class DoctorsListEvent extends Event {

    private List<Doctor> doctors;

    public DoctorsListEvent(List<Doctor> doctors) {
        this.doctors = doctors;
    }

    public List<Doctor> getDoctors() {
        return doctors;
    }
}
