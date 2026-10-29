package com.github.alde0.todocore;

import java.util.List;
import java.util.Objects;

public class TaskService implements AutoCloseable {

    private final TaskRepository taskRepository;

    public TaskService() {
        taskRepository = new TaskRepository();
        taskRepository.initDb();
    }

    public void createTask(String taskName, String taskDescription) {
        Objects.requireNonNull(taskName, "Task name cannot be null");
        Objects.requireNonNull(
                taskDescription, "Task description cannot be null");

        if (taskName.isBlank()) {
            throw new IllegalArgumentException(
                    "Task name cannot be blank");
        }

        taskRepository.insert(taskName, taskDescription);
    }

    public List<Task> listAllTasks() {
        return taskRepository.selectAll();
    }

    public void changeTaskName(int id, String newTaskName) {
        validateId(id);
        Objects.requireNonNull(newTaskName, "Task name cannot be null");

        if (newTaskName.isBlank()) {
            throw new IllegalArgumentException(
                    "Task name cannot be blank");
        }

        taskRepository.updateTitle(id, newTaskName);
    }

    public void changeTaskDescription(
            int id, String newTaskDescription) {
        validateId(id);

        Objects.requireNonNull(
                newTaskDescription,
                "Task description cannot be null");

        taskRepository.updateDescription(id, newTaskDescription);
    }

    public void complete(int id) {
        validateId(id);
        taskRepository.complete(id);
    }

    public void deleteTask(int id) {
        validateId(id);
        taskRepository.delete(id);
    }

    private void validateId(int id) {
        if (id <= 0) {
            throw new IllegalArgumentException(
                    "Task ID must be positive");
        }
    }

    @Override
    public void close() {
        taskRepository.close();
    }
}