package com.QuickLearn.Roadmap;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

public class RoadmapResponse {
    @Getter @Setter
    private List<RoadmapStep> roadmap;

    public RoadmapResponse(List<RoadmapStep> roadmap) {
        this.roadmap = roadmap;
    }

    // Getter and setter
}
