package com.careflow.notifications;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
import java.util.*;
@RestController @RequestMapping("/api/v1/notifications") @PreAuthorize("isAuthenticated()")
public class NotificationController {
 private final JdbcTemplate db;public NotificationController(JdbcTemplate db){this.db=db;}
 @GetMapping public List<Map<String,Object>> mine(org.springframework.security.core.Authentication auth){return db.queryForList("SELECT n.id,n.type,n.title,n.body,n.read_at AS \"readAt\",n.created_at AS \"createdAt\" FROM notifications n JOIN users u ON u.id=n.user_id WHERE u.email=? ORDER BY n.created_at DESC LIMIT 100",auth.getName());}
 @PostMapping("/{id}/read") public void markRead(@PathVariable UUID id,org.springframework.security.core.Authentication auth){int updated=db.update("UPDATE notifications n SET read_at=COALESCE(read_at,now()) FROM users u WHERE n.user_id=u.id AND u.email=? AND n.id=?",auth.getName(),id);if(updated==0)throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Notification not found");}
}
