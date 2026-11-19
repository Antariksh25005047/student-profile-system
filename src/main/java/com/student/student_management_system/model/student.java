package com.student.student_management_system.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.util.*;

@Entity
public class student {
    @Id
    private String studentId;         

    private String name;

    @Column(unique = true)
    private String email;

    private String courseName;

    @Min(0) @Max(100)                 
    private double programmingScore;

    @Min(0) @Max(100)
    private double mathematicsScore;

    @ElementCollection(fetch = FetchType.EAGER)
    private List<String> skills = new ArrayList<>();

    public student() {}

    public student(String studentId, String name, String email, String courseName,
                   double programmingScore, double mathematicsScore, List<String> skills) {
        this.studentId = studentId;
        this.name = name;
        this.email = email;
        this.courseName = courseName;
        this.programmingScore = programmingScore;
        this.mathematicsScore = mathematicsScore;
        this.skills = skills;
    }

    
    public String getStudentId() { return studentId; }
    public void setStudentId(String studentId) { this.studentId = studentId; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public double getProgrammingScore() { return programmingScore; }
    public void setProgrammingScore(double p) { this.programmingScore = p; }
    public double getMathematicsScore() { return mathematicsScore; }
    public void setMathematicsScore(double m) { this.mathematicsScore = m; }
    public List<String> getSkills() { return skills; }
    public void setSkills(List<String> skills) { this.skills = skills; }

    @Override
    public String toString() {
        return studentId + " - " + name + " (" + courseName + ")";
    }
}