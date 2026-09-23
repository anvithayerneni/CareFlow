package com.careflow.analytics;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.*;
@RestController @RequestMapping("/api/v1/admin") @PreAuthorize("hasRole('ADMIN')")
public class AdminController {
 private final JdbcTemplate db; public AdminController(JdbcTemplate db){this.db=db;}
 @GetMapping("/analytics") public Map<String,Object> analytics(){return Map.of("patients",count("SELECT count(*) FROM users WHERE role='PATIENT' AND active"),"doctors",count("SELECT count(*) FROM users WHERE role='DOCTOR' AND active"),"appointmentsToday",count("SELECT count(*) FROM appointments WHERE starts_at::date=current_date"),"completedAppointments",count("SELECT count(*) FROM appointments WHERE status='COMPLETED'"),"cancelledAppointments",count("SELECT count(*) FROM appointments WHERE status='CANCELLED'"),"activeUsers",count("SELECT count(*) FROM users WHERE active"));}
 @GetMapping("/audit") public List<Map<String,Object>> audit(@RequestParam(defaultValue="50") int limit){return db.queryForList("SELECT id,user_id AS \"userId\",action,entity_type AS \"entityType\",entity_id AS \"entityId\",metadata,created_at AS \"createdAt\" FROM audit_logs ORDER BY created_at DESC LIMIT ?",Math.min(200,Math.max(1,limit)));}
 private long count(String sql){Long n=db.queryForObject(sql,Long.class);return n==null?0:n;}
}
