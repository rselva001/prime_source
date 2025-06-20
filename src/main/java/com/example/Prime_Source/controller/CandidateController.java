package com.example.Prime_Source.controller;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.example.Prime_Source.entity.Candidate;
import com.example.Prime_Source.enums.ResultStatus;
import com.example.Prime_Source.repository.CandidateRepository;
import com.example.Prime_Source.service.CandidateService;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
@RestController
@RequestMapping("/api/candidates")
public class CandidateController {

    @Autowired
    private CandidateService candidateService;

    @Autowired
    private CandidateRepository candidateRepository;
 // Change your controller method to:
//    @PreAuthorize("hasRole('ADMIN')")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> createCandidate(
            @RequestPart("candidate") String candidateJson,
            @RequestPart(value = "resumeFile", required = false) MultipartFile resumeFile) {
        
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Candidate candidate = objectMapper.readValue(candidateJson, Candidate.class);
            
            // Set default status if null
            if (candidate.getStatus() == null) {
                candidate.setStatus(ResultStatus.SCHEDULED); // Use an existing enum value
            }

            // Set resume if provided
            if (resumeFile != null && !resumeFile.isEmpty()) {
                candidate.setResumePdf(resumeFile.getBytes());
            }

            // ✅ Set updatedAt to today's date
            candidate.setUpdatedAt(LocalDate.now());

            Candidate savedCandidate = candidateRepository.save(candidate);
            return ResponseEntity.ok(savedCandidate);
            
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(
                Map.of(
                    "error", "Invalid request",
                    "message", e.getMessage(),
                    "details", "Allowed status values: " + Arrays.toString(ResultStatus.values())
                )
            );
        }
    }

//    @PreAuthorize("hasRole('ADMIN')")
   
//    @PatchMapping("/{id}")
//    public ResponseEntity<Candidate> patchCandidate(@PathVariable Long id, @RequestBody Candidate updatedCandidate) {
//        Candidate result = candidateService.updateCandidate(id, updatedCandidate);
//        return ResponseEntity.ok(result);
//    }
    
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Candidate> patchCandidate(
            @PathVariable Long id,
            @RequestParam(value = "name", required = false) String name,
            @RequestParam(value = "email", required = false) String email,
            @RequestParam(value = "phone", required = false) String phone,
            @RequestParam(value = "status", required = false) ResultStatus status,
            @RequestParam(value = "about", required = false) String about,
            @RequestParam(value = "experience", required = false) String experience,
            @RequestParam(value = "noticePeriod", required = false) String noticePeriod,
            @RequestParam(value = "location", required = false) String location,
            @RequestParam(value = "currentCtc", required = false) String currentCtc,
            @RequestParam(value = "expectedCtc", required = false) String expectedCtc,
            @RequestParam(value = "resumePdf", required = false) MultipartFile resumePdf
    ) {
        Candidate result = candidateService.updateCandidatePartial(
            id, name, email, phone, status, about, experience, noticePeriod, location, currentCtc, expectedCtc, resumePdf
        );
        return ResponseEntity.ok(result);
    }


    
    // View PDF inline in browser
    @GetMapping("/view/{id}")
    public ResponseEntity<byte[]> viewDocument(@PathVariable Long id) {
        Candidate doc = candidateService.getDocument(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + doc.getName() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(doc.getResumePdf());
    }
    
    // Download PDF
    @GetMapping("/download/{id}")
    public ResponseEntity<byte[]> downloadDocument(@PathVariable Long id) {
        Candidate doc = candidateService.getDocument(id);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + doc.getName() + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(doc.getResumePdf());
    }
    @GetMapping("/{id}")
    public ResponseEntity<Candidate> getCandidateById(@PathVariable Long id) {
        Candidate candidate = candidateService.getCandidateById(id);
        return ResponseEntity.ok(candidate);
    }

    //getCandidatesByJobId
    @GetMapping("/job/{jobId}")
    public ResponseEntity<List<Candidate>> getCandidatesByJobId(@PathVariable Long jobId) {
        List<Candidate> candidates = candidateService.getCandidatesByJobId(jobId);
        return ResponseEntity.ok(candidates);
    }

    
    
 // Get candidates by job name
    @GetMapping("/job/name/{jobName}")
    public ResponseEntity<List<Candidate>> getCandidatesByJobName(@PathVariable String jobName) {
        List<Candidate> candidates = candidateService.getCandidatesByJobName(jobName);
        return ResponseEntity.ok(candidates);
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Candidate>> getCandidatesByStatus(@PathVariable ResultStatus status) {
        List<Candidate> candidates = candidateService.getCandidatesByStatus(status);
        return ResponseEntity.ok(candidates);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<List<Candidate>> getCandidatesByEmail(@PathVariable String email) {
        List<Candidate> candidates = candidateService.getCandidatesByEmail(email);
        if (candidates.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(candidates);
    }


    @GetMapping("/phone/{phone}")
    public ResponseEntity<Candidate> getCandidateByPhone(@PathVariable String phone) {
        Optional<Candidate> candidate = candidateService.getCandidateByPhone(phone);
        return candidate.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    //searchCandidatesByName
    @GetMapping("/search/{name}")
    public ResponseEntity<List<Candidate>> searchCandidatesByName(@PathVariable String name) {
        List<Candidate> candidates = candidateService.searchCandidatesByName(name);
        return ResponseEntity.ok(candidates);
    }

    //getCandidatesByJobIdAndStatus
    @GetMapping("/job/{jobId}/status/{status}")
    public ResponseEntity<List<Candidate>> getCandidatesByJobIdAndStatus(
            @PathVariable Long jobId,
            @PathVariable ResultStatus status) {
        List<Candidate> candidates = candidateService.getCandidatesByJobIdAndStatus(jobId, status);
        return ResponseEntity.ok(candidates);
    }

//    @PreAuthorize("hasRole('ADMIN')")
    // ✅ Delete candidate by ID
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCandidate(@PathVariable Long id) {
        if (candidateService.candidateExists(id)) {
            candidateService.deleteCandidate(id);
            return ResponseEntity.ok("Candidate with ID " + id + " deleted successfully.");
        } else {
            return ResponseEntity.status(404).body("Candidate with ID " + id + " not found.");
        }
    }
    @GetMapping // This handles GET /api/candidates
    public List<Candidate> getAllCandidates() {
        return candidateService.getAllCandidates();
    }
//    @GetMapping("/search/job")
//    public List<Candidate> searchCandidatesByJobName(@RequestParam String jobName) {
//        return candidateService.searchByJobName(jobName);
//    }
    @GetMapping("/count")
    public long getCandidateCount() {
        return candidateService.getCandidateCount();
    }
////    add an existing candidate to an existing job
//    @PutMapping("/{candidateId}/assign-job/{jobId}")
//    public ResponseEntity<String> assignCandidateToJob(
//            @PathVariable Long candidateId,
//            @PathVariable Long jobId) {
//        try {
//            candidateService.assignCandidateToJob(candidateId, jobId);
//            return ResponseEntity.ok("Candidate assigned to job successfully.");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Error assigning candidate to job: " + e.getMessage());
//        }
//    }

   
    // ✅ Copy existing candidate to a new job
    @PostMapping("/copy/{candidateId}/to/{jobId}")
    public ResponseEntity<Candidate> copyCandidateToJob(
            @PathVariable Long candidateId,
            @PathVariable Long jobId) {

        Candidate copiedCandidate = candidateService.copyCandidateToJob(candidateId, jobId);
        return new ResponseEntity<>(copiedCandidate, HttpStatus.CREATED);
    }
    
//    @GetMapping("/search/{term}")
//    public ResponseEntity<List<Candidate>> searchCandidates(@PathVariable String term) {
//        List<Candidate> results = candidateService.searchCandidates(term);
//        if (results.isEmpty()) {
//            return ResponseEntity.notFound().build();
//        }
//        return ResponseEntity.ok(results);
//    }


}