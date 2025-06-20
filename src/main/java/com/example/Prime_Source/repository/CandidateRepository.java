package com.example.Prime_Source.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.example.Prime_Source.entity.Candidate;
import com.example.Prime_Source.enums.ResultStatus;

public interface CandidateRepository extends JpaRepository<Candidate, Long> {
    List<Candidate> findByJobId(Long jobId);

    List<Candidate> findByStatus(ResultStatus status);

//    Optional<Candidate> findByEmail(String email);
    List<Candidate> findByEmail(String email);


    Optional<Candidate> findByPhone(String phone);

    List<Candidate> findByNameContainingIgnoreCase(String name);

    List<Candidate> findByJobIdAndStatus(Long jobId, ResultStatus status);
    
    List<Candidate> findByJob_JobName(String jobName);
    
    List<Candidate> findByJob_Id(Long jobId);
    @Query("SELECT c FROM Candidate c LEFT JOIN FETCH c.job")
    List<Candidate> findAllWithJobs();
//
//    @Query("SELECT c FROM Candidate c WHERE LOWER(c.job.jobName) LIKE LOWER(CONCAT('%', :jobName, '%'))")
//    List<Candidate> findByJobName(String jobName);
    long count();
    List<Candidate> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCaseOrPhoneContaining(
    	    String name, String email, String phone);

}
