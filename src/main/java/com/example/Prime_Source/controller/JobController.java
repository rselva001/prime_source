// JobController.java
package com.example.Prime_Source.controller;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.Prime_Source.entity.Job;
import com.example.Prime_Source.service.JobService;

@RestController
@RequestMapping("/jobs")
public class JobController {

    @Autowired
    private JobService jobService;

//    @PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public Job addJob(@RequestBody Job job) {
        job.setCreatedAt(LocalDate.now()); // ✅ Set creation date
        return jobService.addJob(job);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public Job updateJob(@PathVariable Long id, @RequestBody Job job) {
        return jobService.updateJob(id, job);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public void deleteJob(@PathVariable Long id) {
        jobService.deleteJob(id);
    }

   

    @GetMapping("/{id}")
    public Job getJob(@PathVariable Long id) {
        return jobService.getJobById(id);
    }
    
    //name by fetch
    @GetMapping("/search/{name}")
    public ResponseEntity<List<Job>> searchJobs(@PathVariable String name) {
        List<Job> jobs = jobService.searchJobsByName(name);
        return ResponseEntity.ok(jobs);
    }
    @GetMapping("/location/{location}")
    public ResponseEntity<List<Job>> getJobsByLocation(@PathVariable String location) {
        List<Job> jobs = jobService.getJobsByLocation(location);
        return ResponseEntity.ok(jobs);
    }
    @GetMapping("/date")
    public ResponseEntity<List<Job>> getJobsByDate(@RequestParam("createdAt") String createdAt) {
        LocalDate date = LocalDate.parse(createdAt); // Format must be YYYY-MM-DD
        List<Job> jobs = jobService.getJobsByDate(date);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping
    public ResponseEntity<List<Job>> getAllJobs() {
        List<Job> jobs = jobService.getAllJobs();
        return ResponseEntity.ok(jobs);
    }
 // JobController.java
  
    @GetMapping("/search/skill/{keyword}")
    public ResponseEntity<List<Job>> searchJobsBySkill(@PathVariable String keyword) {
        List<Job> jobs = jobService.searchJobsBySkill(keyword);
        return ResponseEntity.ok(jobs);
    }

    @GetMapping("/counts")
    public long getJobCount() {
        return jobService.getJobCount();
    }


}
