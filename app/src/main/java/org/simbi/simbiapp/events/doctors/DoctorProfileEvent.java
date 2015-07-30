package org.simbi.simbiapp.events.doctors;

import org.simbi.simbiapp.api.models.Response.Doctor;
import org.simbi.simbiapp.events.Event;

public class DoctorProfileEvent extends Event {

    private Doctor doctor;

    public DoctorProfileEvent(Doctor doctor) {
        this.doctor = doctor;
    }

    public Doctor getDoctor() {
        return doctor;
    }
}
