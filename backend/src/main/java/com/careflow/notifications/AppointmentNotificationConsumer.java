package com.careflow.notifications;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.UUID;
@Component
public class AppointmentNotificationConsumer {
 private final JdbcTemplate db; private final ObjectMapper json;
 public AppointmentNotificationConsumer(JdbcTemplate db,ObjectMapper json){this.db=db;this.json=json;}
 @KafkaListener(topics="careflow.appointments",groupId="careflow-notifications") @Transactional
 public void onAppointmentEvent(String payload){try{JsonNode event=json.readTree(payload);UUID eventId=UUID.fromString(event.path("eventId").asText());UUID patient=UUID.fromString(event.path("patientId").asText());String type=event.path("eventType").asText();if(db.update("INSERT INTO event_inbox(event_id) VALUES(?) ON CONFLICT DO NOTHING",eventId)==0)return;String title=type.equals("APPOINTMENT_CANCELLED")?"Appointment cancelled":"Appointment booked";String body=type.equals("APPOINTMENT_CANCELLED")?"Your demo appointment was cancelled.":"Your demo appointment has been booked.";db.update("INSERT INTO notifications(user_id,type,title,body) VALUES(?,?,?,?)",patient,type,title,body);}catch(Exception e){throw new IllegalArgumentException("Invalid appointment event",e);}}
}
