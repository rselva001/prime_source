package com.example.Prime_Source.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.Prime_Source.entity.Job;

public interface JobRepository extends JpaRepository<Job, Long> {
	
	  List<Job> findByJobNameContainingIgnoreCase(String jobName);
	  List<Job> findByJobLocationContainingIgnoreCase(String location);
	  List<Job> findByCreatedAt(LocalDate date);
	// JobRepository.java
	  List<Job> findBySkillsnameContainingIgnoreCase(String skillKeyword);
	  long count();



}