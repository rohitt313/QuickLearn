package com.QuickLearn.Controller;

import com.QuickLearn.Roadmap.RoadmapMaker;
import com.QuickLearn.Roadmap.RoadmapResponse;
import com.QuickLearn.Roadmap.RoadmapStep;
import com.QuickLearn.Roadmap.Subtask;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/roadmap")
public class RoadmapController {

    @Autowired
    private RoadmapMaker roadmapMaker;

@PostMapping
public ResponseEntity<RoadmapResponse> generateRoadmap(@RequestBody Map<String, String> body) {
    String skill = body.get("skill");

    if (!roadmapMaker.checkValidSkill(skill)) {
        List<RoadmapStep> error = List.of(new RoadmapStep(
            "0", 
            "Invalid skill", 
            List.of(new Subtask("The provided skill is not recognized as a valid programming or development skill."))
        ));
        return ResponseEntity.ok(new RoadmapResponse(error));
    }

    List<RoadmapStep> roadmap = roadmapMaker.generateStructuredRoadmap(skill);
    return ResponseEntity.ok(new RoadmapResponse(roadmap));
}


}
