package com.QuickLearn.Roadmap;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import java.util.stream.Collectors;

import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.vertexai.gemini.VertexAiGeminiChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;




//make a roadmap for a skill using gemini api and save it as a string which can be converted to jso for react frontend in the crontoller class
@Component
public class RoadmapMaker {
    private Roadmap roadmap;
    @Autowired
    private VertexAiGeminiChatModel chatModel;


    public RoadmapMaker() {
        this.roadmap = new Roadmap();
    }


    public boolean checkValidSkill(String skill) {
        if (skill == null || skill.trim().isEmpty()) {
            return false;
        }
    
        String message = """
            Is "%s" a valid programming or software development skill?
            Reply with only "yes" or "no".
            """.formatted(skill);
    
        Prompt prompt = new Prompt(message);
        String response = chatModel.call(prompt).getResult().getOutput().getText().toLowerCase();
    
        // Handle response like "yes", "yes it is", "no", etc.
        return response.contains("yes");
    }
    public void generateDescription() {
        String skill = roadmap.getSkill();

        String promptText = """
            Write a concise, beginner-friendly description of the coding skill "%s" . 
            Limit it to 2-3 sentences and explain its purpose in modern software development.
            """.formatted(skill);

        Prompt prompt = new Prompt(promptText);
        String response = chatModel.call(prompt).getResult().getOutput().getText();

        roadmap.setDescription(response);
    }



    public List<RoadmapStep> generateStructuredRoadmap(String skill) {
        Logger logger = LoggerFactory.getLogger(RoadmapMaker.class);
    
        String prompt = """
            Create a 3-step learning roadmap for the skill "%s". For each step, include a title and 2-3 subtasks as short bullet points. Respond in JSON format like this:
            [
              {
                "step": "1",
                "title": "Step title",
                "subtasks": ["subtask 1", "subtask 2"]
              },
              ...
            ]
            """.formatted(skill);
        String json = chatModel.call(new Prompt(prompt)).getResult().getOutput().getText();
        logger.debug("Raw AI response: {}", json);
    
        if (json == null || json.trim().isEmpty()) {
            logger.error("AI response is empty or null.");
            return List.of(errorStep("AI returned empty response"));
        }
    
        json = json.replace("```json", "").replace("```", "").trim();
    
        if (!json.startsWith("[") && !json.startsWith("{")) {
            logger.error("Invalid AI response format after sanitization: {}", json);
            return List.of(errorStep("AI response not in valid JSON format"));
        }
    
        ObjectMapper mapper = new ObjectMapper();
        RoadmapStep[] stepArray;
    
        try {
            stepArray = mapper.readValue(json, RoadmapStep[].class);
        } catch (IOException e) {
            logger.error("Failed to parse AI response as JSON: {}", json, e);
            return List.of(errorStep("Failed to parse AI response"));
        }
    
        if (stepArray == null || stepArray.length == 0) {
            logger.warn("AI response returned an empty roadmap.");
            return List.of(errorStep("AI returned empty roadmap"));
        }
    
        return List.of(stepArray);
    }
    
    // Helper method to create a fallback error step
    private RoadmapStep errorStep(String errorMessage) {
        List<Subtask> subtasks = List.of(new Subtask(errorMessage));
        return new RoadmapStep("0", "Error generating roadmap", subtasks);
    }
    
    

}
