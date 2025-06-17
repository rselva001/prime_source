//package com.example.Prime_Source.service;
//
//import java.util.List;
//import java.util.Optional;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import com.example.Prime_Source.entity.Job;
//import com.example.Prime_Source.entity.Skill;
//import com.example.Prime_Source.repository.SkillRepository;
//import com.example.Prime_Source.repository.JobRepository;
//
//@Service
//public class SkillService {
//
//    @Autowired
//    private SkillRepository skillRepository;
//
//    @Autowired
//    private JobRepository jobRepository;
//
//    // Add skills as comma-separated string for a job
//    public Skill addSkillsToJob(Long jobId, List<String> skillList) {
//        Optional<Job> jobOpt = jobRepository.findById(jobId);
//        if (jobOpt.isEmpty()) {
//            throw new RuntimeException("Job not found with id " + jobId);
//        }
//
//        Job job = jobOpt.get();
//
//        Skill skill = new Skill();
//        // Join list of skills with commas
//        String skillNames = String.join(",", skillList);
//        skill.setSkillNames(skillNames);
//        skill.setJob(job);
//
//        return skillRepository.save(skill);
//    }
//
//    // Get all skills for a job (returns Skill entities)
//    public List<Skill> getSkillsByJobId(Long jobId) {
//        return skillRepository.findByJobId(jobId);
//    }
//
//    // Get all skills
//    public List<Skill> getAllSkills() {
//        return skillRepository.findAll();
//    }
//
//    //delete
//    public boolean deleteSkillById(Long id) {
//        Optional<Skill> skill = skillRepository.findById(id);
//        if (skill.isPresent()) {
//            skillRepository.deleteById(id);
//            return true;
//        } else {
//            return false;
//        }
//    }
//
//}
