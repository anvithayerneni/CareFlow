package com.careflow.appointments;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import java.time.Instant;
import java.util.Map;
import java.util.UUID;
@Service
public class AppointmentEventPublisher {
 private final KafkaTemplate<String,String> kafka; private final ObjectMapper json;
 public AppointmentEventPublisher(KafkaTemplate<String,String> kafka,ObjectMapper json){this.kafka=kafka;this.json=json;}
 public void publish(String type,UUID appointmentId,UUID patientId,UUID doctorId){try{String value=json.writeValueAsString(Map.of("eventId",UUID.randomUUID(),"eventType",type,"timestamp",Instant.now(),"appointmentId",appointmentId,"patientId",patientId,"doctorId",doctorId));kafka.send("careflow.appointments",appointmentId.toString(),value);}catch(JsonProcessingException e){throw new IllegalStateException("Unable to encode appointment event",e);}}
}
