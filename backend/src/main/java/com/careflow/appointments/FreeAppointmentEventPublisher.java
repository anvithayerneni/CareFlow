package com.careflow.appointments;

import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.UUID;

/** Keeps the portfolio demo's appointment notifications working without a Kafka service. */
@Service
@Profile("free")
public class FreeAppointmentEventPublisher implements AppointmentEventPublisher {
    private final JdbcTemplate db;

    public FreeAppointmentEventPublisher(JdbcTemplate db) { this.db = db; }

    @Override
    public void publish(String type, UUID appointmentId, UUID patientId, UUID doctorId) {
        String title;
        String body;
        if (type.equals("APPOINTMENT_CANCELLED")) {
            title = "Appointment cancelled";
            body = "Your demo appointment was cancelled.";
        } else if (type.equals("APPOINTMENT_RESCHEDULED")) {
            title = "Appointment rescheduled";
            body = "Your demo appointment has been rescheduled.";
        } else {
            title = "Appointment booked";
            body = "Your demo appointment has been booked.";
        }
        db.update("INSERT INTO notifications(user_id,type,title,body) VALUES(?,?,?,?)",
                patientId, type, title, body);
    }
}
