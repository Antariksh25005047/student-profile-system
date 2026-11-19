package com.student.student_management_system.service;
import org.springframework.stereotype.Service;
import java.util.*;
import java.util.stream.Collectors;
import com.student.student_management_system.model.student;
import com.student.student_management_system.repository.StudentRepository;
@Service
public class StudentService {

    private final StudentRepository repo;

    public StudentService(StudentRepository repo) {
        this.repo = repo;
    }

    
    public student add(student s) {
        if (repo.existsById(s.getStudentId())) {
            throw new RuntimeException("Student ID already exists");
        }
        return repo.save(s);
    }

   
    public double average(student s) {
        return (s.getProgrammingScore() + s.getMathematicsScore()) / 2;
    }

   
    public String category(student s) {
        double avg = average(s);
        if (avg >= 85) return "Excellent";
        if (avg >= 70) return "Good";
        if (avg >= 50) return "Satisfactory";
        return "Needs Improvement";
    }

    
    public String strongerSubject(student s) {
        if (s.getProgrammingScore() > s.getMathematicsScore()) return "Programming";
        if (s.getMathematicsScore() > s.getProgrammingScore()) return "Mathematics";
        return "Both equal";
    }

    
    public Optional<student> findById(String id) { return repo.findById(id); }
    public Optional<student> findByEmail(String email) { return repo.findByEmail(email); }
    public List<student> findByCourse(String course) { return repo.findByCourseName(course); }

    // 5. Merit list (average ke hisaab se, high to low)
    public List<student> meritList() {
        List<student> list = repo.findAll();
        list.sort((a, b) -> Double.compare(average(b), average(a)));
        return list;
    }

    
    public Map<String, List<student>> sameSkills() {
        Map<String, List<student>> map = new HashMap<>();
        for (student s : repo.findAll()) {
            for (String skill : s.getSkills()) {
                map.computeIfAbsent(skill.toLowerCase(), k -> new ArrayList<>()).add(s);
            }
        }
        // sirf wo skills jo 2+ students ke paas hain
        return map.entrySet().stream()
                .filter(e -> e.getValue().size() > 1)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
    }

    
    public List<student> weakProgrammingStrongMaths() {
        return repo.findAll().stream()
                .filter(s -> s.getProgrammingScore() < 50 && s.getMathematicsScore() >= 75)
                .collect(Collectors.toList());
    }
}