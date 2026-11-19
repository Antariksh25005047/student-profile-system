package com.student.student_management_system.controller;

import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.*;
import com.student.student_management_system.model.student;
import com.student.student_management_system.repository.StudentRepository;
import com.student.student_management_system.service.StudentService;
@RestController
@RequestMapping("/students")
public class StudentController {

    private final StudentService service;
    private final StudentRepository repo;

    public StudentController(StudentService service, StudentRepository repo) {
        this.service = service;
        this.repo = repo;
    }

   
    @PostMapping
    public ResponseEntity<?> add(@Valid @RequestBody student s) {
        try {
            return ResponseEntity.ok(service.add(s));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

   
    @GetMapping
    public List<student> getAll() {
        return repo.findAll();
    }

   
    @GetMapping("/{id}")
    public ResponseEntity<?> getById(@PathVariable String id) {
        return service.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElse(ResponseEntity.status(404).body("Student not found"));
    }

   
    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id, @RequestBody student updated) {
        Optional<student> opt = service.findById(id);
        if (opt.isEmpty()) {
            return ResponseEntity.status(404).body("Student not found");
        }
        double p = updated.getProgrammingScore();
        double m = updated.getMathematicsScore();
        if (p < 0 || p > 100 || m < 0 || m > 100) {
            return ResponseEntity.badRequest().body("Scores must be between 0 and 100");
        }
        student s = opt.get();
        s.setProgrammingScore(p);
        s.setMathematicsScore(m);
        s.setSkills(updated.getSkills());
        return ResponseEntity.ok(repo.save(s));
    }

    
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable String id) {
        if (!repo.existsById(id)) {
            return ResponseEntity.status(404).body("Student not found");
        }
        repo.deleteById(id);
        return ResponseEntity.ok("Deleted");
    }

    
    @GetMapping("/merit")
    public List<student> merit() {
        return service.meritList();
    }

    @GetMapping("/search")
    public Object search(@RequestParam(required = false) String email,
                         @RequestParam(required = false) String course) {
        if (email != null) return service.findByEmail(email);
        if (course != null) return service.findByCourse(course);
        return "Give email or course";
    }

    @GetMapping("/same-skills")
    public Map<String, List<student>> sameSkills() {
        return service.sameSkills();
    }

    @GetMapping("/needs-programming-help")
    public List<student> needsHelp() {
        return service.weakProgrammingStrongMaths();
    }

    @GetMapping("/{id}/report")
    public ResponseEntity<?> report(@PathVariable String id) {
        return service.findById(id).<ResponseEntity<?>>map(s -> {
            Map<String, Object> r = new LinkedHashMap<>();
            r.put("name", s.getName());
            r.put("average", service.average(s));
            r.put("category", service.category(s));
            r.put("strongerSubject", service.strongerSubject(s));
            return ResponseEntity.ok(r);
        }).orElse(ResponseEntity.status(404).body("Student not found"));
    }
}