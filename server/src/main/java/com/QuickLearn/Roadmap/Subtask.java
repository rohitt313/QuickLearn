package com.QuickLearn.Roadmap;

import lombok.Getter;
import lombok.Setter;

public class Subtask {
    @Getter @Setter
    private String title;
    @Getter @Setter
    private boolean completed = false;

    public Subtask(String title) {
        this.title = title;
    }
    public Subtask() {
        // Default constructor for JSON deserialization
    }

}
