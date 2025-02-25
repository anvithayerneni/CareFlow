package com.careflow.appointments;
import org.junit.jupiter.api.Test;
import java.time.OffsetDateTime;
import java.util.UUID;
import static org.junit.jupiter.api.Assertions.*;
class BookingValidationTest {
 @Test void bookingRequestCarriesDoctorAndInterval(){OffsetDateTime start=OffsetDateTime.now().plusDays(2);var request=new AppointmentController.Booking(UUID.randomUUID(),start,start.plusMinutes(30),"Routine demo visit",AppointmentController.Type.VIDEO);assertEquals(30,java.time.Duration.between(request.startsAt(),request.endsAt()).toMinutes());assertEquals(AppointmentController.Type.VIDEO,request.appointmentType());}
}
