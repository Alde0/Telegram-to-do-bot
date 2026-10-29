package com.github.alde0.todocore;

public class Task {
    private final int id;
    private final String title;
    private final boolean completed;
    private final String description;

    public Task(int id, String title, boolean completed, String description) {
        this.id = id;
        this.title = title;
        this.completed = completed;
        this.description = description;
    }
    public String getTask() {
        String status = completed ? "completed" : "uncompleted";
        return String.format("%d %s %s %s", id, title, status, description);
    }
}
