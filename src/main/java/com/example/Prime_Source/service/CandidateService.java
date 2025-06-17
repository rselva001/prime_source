package com.example.Prime_Source.service;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.Prime_Source.entity.Candidate;
import com.example.Prime_Source.entity.Job;
import com.example.Prime_Source.enums.ResultStatus;
import com.example.Prime_Source.repository.CandidateRepository;
import com.example.Prime_Source.repository.JobRepository;

@Service
public class CandidateService {

    @Autowired
    private CandidateRepository candidateRepository;
    
    @Autowired
    private JobRepository jobRepository;

    // ✅ Create a new candidate with resume file
    public Candidate createCandidate(Candidate candidate, MultipartFile resumeFile) {
        if (candidate.getId() != null) {
            throw new IllegalArgumentException("New candidate cannot have an ID");
        }

        try {
            if (resumeFile != null && !resumeFile.isEmpty()) {
                candidate.setResumePdf(resumeFile.getBytes());
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read resume file", e);
        }

        return candidateRepository.save(candidate);
    }
    
    //get pdf
    public Candidate getDocument(Long id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Document not found"));
    }

    // ✅ Update candidate
    public Candidate updateCandidate(Long id, Candidate updatedCandidate) {
        Candidate existingCandidate = candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        existingCandidate.setName(updatedCandidate.getName());
        existingCandidate.setEmail(updatedCandidate.getEmail());
        existingCandidate.setPhone(updatedCandidate.getPhone());
        existingCandidate.setResumePdf(updatedCandidate.getResumePdf());
        existingCandidate.setStatus(updatedCandidate.getStatus());
        existingCandidate.setJob(updatedCandidate.getJob());

        return candidateRepository.save(existingCandidate);
    }
    
    //get by job name
    public List<Candidate> getCandidatesByJobName(String jobName) {
        return candidateRepository.findByJob_JobName(jobName);
    }


    public Candidate getCandidateById(Long id) {
        return candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));
    }

    public List<Candidate> getCandidatesByJobId(Long jobId) {
        return candidateRepository.findByJobId(jobId);
    }

    public List<Candidate> getCandidatesByStatus(ResultStatus status) {
        return candidateRepository.findByStatus(status);
    }

    public List<Candidate> getCandidatesByEmail(String email) {
        return candidateRepository.findByEmail(email);
    }


    public Optional<Candidate> getCandidateByPhone(String phone) {
        return candidateRepository.findByPhone(phone);
    }

    public List<Candidate> searchCandidatesByName(String name) {
        return candidateRepository.findByNameContainingIgnoreCase(name);
    }

    public List<Candidate> getCandidatesByJobIdAndStatus(Long jobId, ResultStatus status) {
        return candidateRepository.findByJobIdAndStatus(jobId, status);
    }

    public void deleteCandidate(Long id) {
        Optional<Candidate> optionalCandidate = candidateRepository.findById(id);
        if (optionalCandidate.isPresent()) {
            candidateRepository.deleteById(id);
            System.out.println("Candidate with ID " + id + " deleted.");
        } else {
            System.out.println("Candidate with ID " + id + " not found. No action taken.");
        }
    }
    
    //helper
    public boolean candidateExists(Long id) {
        return candidateRepository.existsById(id);
    }


 // In CandidateService.java
    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAllWithJobs(); // Use the method that fetches jobs
    }
//    public List<Candidate> searchByJobName(String jobName) {
//        return candidateRepository.findByJobName(jobName);
//    }
    public long getCandidateCount() {
        return candidateRepository.count(); // or countAllCandidates()
    }
    
////    add an existing candidate to an existing job
//    public void assignCandidateToJob(Long candidateId, Long jobId) {
//        Candidate candidate = candidateRepository.findById(candidateId)
//                .orElseThrow(() -> new RuntimeException("Candidate not found"));
//        Job job = jobRepository.findById(jobId)
//                .orElseThrow(() -> new RuntimeException("Job not found"));
//
//        candidate.setJob(job); // Ensure `Candidate` has a `Job` field
//        candidateRepository.save(candidate);
//    }
//    add an existing candidate to a new job with the same resume and status
    public Candidate copyCandidateToJob(Long candidateId, Long jobId) {
        Candidate existing = candidateRepository.findById(candidateId)
                .orElseThrow(() -> new RuntimeException("Candidate not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        Candidate newCandidate = new Candidate();
        newCandidate.setName(existing.getName());
        newCandidate.setEmail(existing.getEmail());
        newCandidate.setPhone(existing.getPhone());

        // ✅ Use the same status
        newCandidate.setStatus(existing.getStatus());

        // ✅ Link to the new job
        newCandidate.setJob(job);

        // ✅ Use the same resume file
        newCandidate.setResumePdf(existing.getResumePdf());

        return candidateRepository.save(newCandidate);
    }


}
