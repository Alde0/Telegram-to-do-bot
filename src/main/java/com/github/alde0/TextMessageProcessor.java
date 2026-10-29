package com.github.alde0;

import com.github.alde0.todocore.Task;
import com.github.alde0.todocore.TaskService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.ArrayList;
import java.util.List;

public class TextMessageProcessor {

    private final TaskService taskService;

    private String currentAction;
    private String taskTitle;
    private int taskId;

    public TextMessageProcessor(TaskService taskService) {
        this.taskService = taskService;
    }

    public SendMessage processText(String text, Long chatId) {
        String response = processTextMessage(text);

        SendMessage sendMessage = new SendMessage(
                chatId.toString(),
                response
        );

        return sendMessage;
    }

    private String processTextMessage(String text) {
        if (text == null) {
            return "The message cannot be empty.";
        }

        String message = text.trim();

        if (message.isEmpty()) {
            return "The message cannot be empty.";
        }

        if (message.equals("/cancel")) {
            resetState();
            return "Action cancelled.";
        }

        if (message.equals("/start")) {
            resetState();
            return "Hello! I'm a To-Do bot.\n\n" + getHelpMessage();
        }

        if (message.equals("/help")) {
            return getHelpMessage();
        }

        try {
            if (currentAction != null) {
                return processDialogStep(message);
            }

            return processCommand(message);
        } catch (Exception e) {
            resetState();
            return "The operation failed. Please try again.";
        }
    }

    private String processCommand(String message) {
        switch (message) {
            case "/createTask":
                currentAction = "create_title";
                return "Enter the task title:";

            case "/listAllTasks":
                return listAllTasks();

            case "/editTaskName":
                currentAction = "edit_name_id";
                return "Enter the ID of the task whose title you want to change:";

            case "/editTaskDescription":
                currentAction = "edit_description_id";
                return "Enter the ID of the task whose description you want to change:";

            case "/editTaskStatus":
                currentAction = "edit_status_id";
                return "Enter the ID of the task whose status you want to change:";

            case "/deleteTask":
                currentAction = "delete_id";
                return "Enter the ID of the task you want to delete:";

            default:
                return "Unknown command. Use /help to see available commands.";
        }
    }

    private String processDialogStep(String message) {
        switch (currentAction) {
            case "create_title":
                taskTitle = message;
                currentAction = "create_description";
                return "Enter the task description:";

            case "create_description":
                taskService.createTask(taskTitle, message);
                resetState();
                return "Task created successfully.";

            case "edit_name_id":
                taskId = parseTaskId(message);
                currentAction = "edit_name_value";
                return "Enter the new task title:";

            case "edit_name_value":
                taskService.changeTaskName(taskId, message);
                resetState();
                return "Task title updated successfully.";

            case "edit_description_id":
                taskId = parseTaskId(message);
                currentAction = "edit_description_value";
                return "Enter the new task description:";

            case "edit_description_value":
                taskService.changeTaskDescription(taskId, message);
                resetState();
                return "Task description updated successfully.";

            case "edit_status_id":
                taskId = parseTaskId(message);
                taskService.complete(taskId);
                resetState();
                return "Task status updated successfully.";

            case "delete_id":
                taskId = parseTaskId(message);
                taskService.deleteTask(taskId);
                resetState();
                return "Task deleted successfully.";

            default:
                resetState();
                return "The conversation state has been reset. Use /help to see available commands.";
        }
    }

    private int parseTaskId(String message) {
        try {
            int id = Integer.parseInt(message);

            if (id <= 0) {
                throw new NumberFormatException();
            }

            return id;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Task ID must be a positive integer.");
        }
    }

    private String listAllTasks() {
        List<Task> tasks = taskService.listAllTasks();

        if (tasks == null || tasks.isEmpty()) {
            return "Your task list is empty.";
        }

        List<String> formatedTasks = new ArrayList<>();
        for (Task task : tasks) {
            formatedTasks.add(task.getTask());
        }
        return String.join("\n", formatedTasks);
    }

    private String getHelpMessage() {
        return """
                Available commands:

                /createTask — create a task
                /listAllTasks — list all tasks
                /editTaskName — edit a task title
                /editTaskDescription — edit a task description
                /editTaskStatus — mark a task as completed
                /deleteTask — delete a task
                /cancel — cancel the current action
                /help — show this help message
                """;
    }

    private void resetState() {
        currentAction = null;
        taskTitle = null;
        taskId = 0;
    }
}
/*package com.github.alde0.Processors;

import com.github.alde0.todocore.Task;
import com.github.alde0.todocore.TaskService;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;

import java.util.ArrayList;
import java.util.List;

public class TextMessageProcessor {
    public static SendMessage processText(String textCommand, long chatId) {
        String text;
        switch (textCommand) {
            case "/start" -> text = "Hello! This is a simple to-do manager";
            case "/help" -> text = "/createTask {name} {description} - create task\n" +
                    "/listAllTasks - read all tasks\n" +
                    "/editTaskName {id} {name} - change task name\n" +
                    "/editTaskDescription {id} {description} - change task description\n" +
                    "/editTaskStatus {id} - change task status\n" +
                    "/deleteTask {id} - delete task";
            case "/createTask" -> createTaskProcessor();
            case "/listAllTasks" -> text = listAllTasksProcessor();
            case "/editTaskName" ->
            case "/editTaskDescription" ->
            case "/editTaskStatus" ->
            case "/deleteTask" ->
            default -> text = "Please select an action from the command menu";
        }
        return SendMessage.builder()
                .chatId(chatId)
                .text(text)
                .build();
    }
    private static void createTaskProcessor() {
        TaskService.createTask();
    }
    private static String listAllTasksProcessor() {
        List<Task> tasks = TaskService.listAllTasks();
        List<String> formatedTasks = new ArrayList<>();
        for (Task task : tasks) {
            formatedTasks.add(task.getTask());
        }
        return String.join("\n", formatedTasks);
    }
}*/