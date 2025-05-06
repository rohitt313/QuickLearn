package com.QuickLearn.Roadmap;

import java.util.List;

import javax.management.ConstructorParameters;

import lombok.Getter;
import lombok.Setter;

public class RoadmapStep {
    @Getter@Setter
    private String step;
    @Getter@Setter
    private String title;
    @Getter@Setter
    private List<Subtask> subtasks;

    public RoadmapStep(String step, String title, List<Subtask> subtasks) {
        this.step = step;
        this.title = title;
        this.subtasks = subtasks;
    }

    public RoadmapStep() {
        // Default constructor for JSON deserialization
    }

    
}
