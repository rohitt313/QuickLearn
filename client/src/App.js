import React, { useState } from "react";
import { motion } from "framer-motion";
import "./App.css";

function App() {
  const [goal, setGoal] = useState("");
  const [roadmap, setRoadmap] = useState(null);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState(null);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setError(null);
    setRoadmap(null);

    try {
      const response = await fetch("http://localhost:8080/api/roadmap", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ skill: goal }),
      });

      if (!response.ok) {
        throw new Error("Failed to fetch roadmap. Please try again.");
      }

      const data = await response.json();

      const roadmapWithCompletion = data.roadmap.map((step) => ({
        ...step,
        completed: false,
        subtasks: step.subtasks
          ? step.subtasks.map((subtask) => ({
              ...subtask,
              completed: false,
            }))
          : [],
      }));

      setRoadmap(roadmapWithCompletion);
    } catch (err) {
      setError(err.message);
    } finally {
      setLoading(false);
    }
  };

  const toggleCompletion = (index) => {
    setRoadmap((prev) =>
      prev.map((step, i) =>
        i === index
          ? {
              ...step,
              completed: step.subtasks.every((s) => s.completed)
                ? !step.completed
                : step.completed,
            }
          : step
      )
    );
  };

  const toggleSubtaskCompletion = (stepIndex, subtaskIndex) => {
    setRoadmap((prev) =>
      prev.map((step, i) =>
        i === stepIndex
          ? {
              ...step,
              subtasks: step.subtasks.map((subtask, j) =>
                j === subtaskIndex
                  ? { ...subtask, completed: !subtask.completed }
                  : subtask
              ),
              completed: step.subtasks.every(
                (s, j) =>
                  j === subtaskIndex ? !s.completed : s.completed
              ),
            }
          : step
      )
    );
  };

  const isInvalidSkill = () =>
    roadmap &&
    roadmap.length === 1 &&
    roadmap[0].step === "0" &&
    roadmap[0].title.toLowerCase().includes("invalid");

  return (
    <div className="App">
      <motion.header
        className="App-header"
        initial={{ opacity: 0, scale: 0.8 }}
        animate={{ opacity: 1, scale: 1 }}
        transition={{ duration: 1 }}
      >
        <h1>QuickLearn</h1>
        <form onSubmit={handleSubmit}>
          <motion.input
            type="text"
            value={goal}
            onChange={(e) => setGoal(e.target.value)}
            placeholder="What tech skill do you want to learn?"
            className="goal-input"
            whileFocus={{ scale: 1.1 }}
          />
          <motion.button
            type="submit"
            className="submit-button"
            whileHover={{ scale: 1.1 }}
            whileTap={{ scale: 0.9 }}
          >
            Generate Roadmap
          </motion.button>
        </form>

        {loading && <p>Loading...</p>}
        {error && <p className="error-message">{error}</p>}

        {roadmap && isInvalidSkill() && (
          <div className="invalid-message">
             {roadmap[0].subtasks[0]?.title ||
              "The provided skill is not valid."}
          </div>
        )}

        {roadmap && !isInvalidSkill() && (
          <motion.div
            className="roadmap-container"
            initial={{ opacity: 0, y: 50 }}
            animate={{ opacity: 1, y: 0 }}
            transition={{ duration: 0.8 }}
          >
            <h2>Roadmap for "{goal}"</h2>
            <ol>
              {roadmap.map((step, stepIndex) => (
                <motion.li
                  key={step.step}
                  initial={{ opacity: 0, x: -20 }}
                  animate={{ opacity: 1, x: 0 }}
                  transition={{ duration: 0.5 }}
                >
                  <label>
                    <input
                      type="checkbox"
                      checked={step.completed}
                      onChange={() => toggleCompletion(stepIndex)}
                      disabled={!step.subtasks.every((s) => s.completed)}
                    />
                    <span
                      style={{
                        textDecoration: step.completed
                          ? "line-through"
                          : "none",
                      }}
                    >
                      {step.title}
                    </span>
                  </label>
                  {step.subtasks && step.subtasks.length > 0 && (
                    <ul className="subtask-list">
                      {step.subtasks.map((subtask, subtaskIndex) => (
                        <li key={subtaskIndex}>
                          <label>
                            <input
                              type="checkbox"
                              checked={subtask.completed}
                              onChange={() =>
                                toggleSubtaskCompletion(stepIndex, subtaskIndex)
                              }
                            />
                            <span
                              style={{
                                textDecoration: subtask.completed
                                  ? "line-through"
                                  : "none",
                              }}
                            >
                              {subtask.title}
                            </span>
                          </label>
                        </li>
                      ))}
                    </ul>
                  )}
                </motion.li>
              ))}
            </ol>
          </motion.div>
        )}
      </motion.header>
    </div>
  );
}

export default App;
