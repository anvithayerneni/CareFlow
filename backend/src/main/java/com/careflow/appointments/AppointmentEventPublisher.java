package com.careflow.appointments;

import java.util.UUID;

public interface AppointmentEventPublisher {
    void publish(String type, UUID appointmentId, UUID patientId, UUID doctorId);
}
