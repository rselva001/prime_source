//package com.example.Prime_Source.entity;
//
//import java.util.List;
//
//import com.fasterxml.jackson.annotation.JsonBackReference;
//
//import jakarta.persistence.*;
//
//@Entity
//public class Skill {
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long id;
//    // Store multiple skills as one comma-separated String
//    private String skillNames;  // e.g. "Java,Python,HTML"
//
//    @ManyToOne
//    @JoinColumn(name = "job_id")
//    @JsonBackReference
//    private Job job;
//
//	public Long getId() {
//		return id;
//	}
//
//	public void setId(Long id) {
//		this.id = id;
//	}
//
//	public String getSkillNames() {
//		return skillNames;
//	}
//
//	public void setSkillNames(String skillNames) {
//		this.skillNames = skillNames;
//	}
//
//	public Job getJob() {
//		return job;
//	}
//
//	public void setJob(Job job) {
//		this.job = job;
//	}
//
//    
//    // Getters and Setters
//	
//}
