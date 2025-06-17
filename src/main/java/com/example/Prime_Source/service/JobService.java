package com.example.Prime_Source.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.Prime_Source.entity.Job;
import com.example.Prime_Source.repository.JobRepository;

@Service
public class JobService {

    @Autowired
    private JobRepository jobRepository;

    public Job addJob(Job job) {
        if (job.getSkillsname() == null || job.getSkillsname().isBlank()) {
            job.setSkillsname("No skills specified");
        }
        return jobRepository.save(job);
    }


    public Job updateJob(Long id, Job jobDetails) {
        Job job = jobRepository.findById(id).orElseThrow();
        job.setJobName(jobDetails.getJobName());
        job.setJobLocation(jobDetails.getJobLocation());
        job.setSkillsname(jobDetails.getSkillsname());
        return jobRepository.save(job);
    }

    public void deleteJob(Long id) {
        jobRepository.deleteById(id);
    }

    public Job getJobById(Long id) {
        return jobRepository.findById(id).orElseThrow();
    }

    public List<Job> searchJobsByName(String jobName) {
        return jobRepository.findByJobNameContainingIgnoreCase(jobName);
    }

    public List<Job> getJobsByLocation(String location) {
        return jobRepository.findByJobLocationContainingIgnoreCase(location);
    }

    public List<Job> getJobsByDate(LocalDate date) {
        return jobRepository.findByCreatedAt(date);
    }

    public List<Job> getAllJobs() {
        return jobRepository.findAll();
    }
 // JobService.java
    public List<Job> searchJobsBySkill(String skillKeyword) {
        return jobRepository.findBySkillsnameContainingIgnoreCase(skillKeyword);
    }

    public long getJobCount() {
        return jobRepository.count();
    }
}
