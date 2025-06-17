//package com.example.Prime_Source.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.security.access.prepost.PreAuthorize;
//import org.springframework.web.bind.annotation.DeleteMapping;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import com.example.Prime_Source.entity.Skill;
//import com.example.Prime_Source.service.SkillService;
//
//@RestController
//@RequestMapping("/api/skills")
//public class SkillController {
//
//    @Autowired
//    private SkillService skillService;
//
//    
//    // Add skills (list of skill strings) to a job
//    @PreAuthorize("hasRole('ADMIN')")
//    @PostMapping("/add/{jobId}")
//    public ResponseEntity<Skill> addSkillsToJob(
//            @PathVariable Long jobId,
//            @RequestBody List<String> skillNames) {
//        Skill skill = skillService.addSkillsToJob(jobId, skillNames);
//        return ResponseEntity.ok(skill);
//    }
//
//    // Get skills by job id
//    @GetMapping("/job/{jobId}")
//    public ResponseEntity<List<Skill>> getSkillsByJob(@PathVariable Long jobId) {
//        List<Skill> skills = skillService.getSkillsByJobId(jobId);
//        return ResponseEntity.ok(skills);
//    }
//
//    // Get all skills
//    @GetMapping("/all")
//    public ResponseEntity<List<Skill>> getAllSkills() {
//        return ResponseEntity.ok(skillService.getAllSkills());
//    }
//
//    @PreAuthorize("hasRole('ADMIN')")
//    @DeleteMapping("/{id}")
//    public ResponseEntity<String> deleteSkill(@PathVariable Long id) {
//        boolean deleted = skillService.deleteSkillById(id);
//        if (deleted) {
//            return ResponseEntity.ok("Deleted skill with ID: " + id);
//        } else {
//            return ResponseEntity.status(404).body("Skill with ID " + id + " not found.");
//        }
//    }
//
//}
