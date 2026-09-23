package com.careflow.ai;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController @RequestMapping("/api/v1/assistant")
public class AssistantController {
 public record Question(@NotBlank @Size(max=500) String question){}
 @PostMapping public Map<String,String> ask(@Valid @RequestBody Question q){return Map.of("answer","I can help with general CareFlow navigation, such as finding appointments, clinicians, and demo documents. I cannot interpret health information, diagnose conditions, or recommend treatment. For medical concerns, contact a qualified clinician.","mode","informational-demo","disclaimer","Synthetic demonstration only; not medical advice.");}
}
