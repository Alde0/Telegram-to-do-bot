package com.github.alde0.todocore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository implements AutoCloseable {

    private static final Logger log =
            LoggerFactory.getLogger(TaskRepository.class);

    private Connection connection;

    void initDb() {
        initConnection();
        createTable();
    }

    private void initConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:todo.db");
        } catch (SQLException e) {
            throw new TaskStorageException(
                    "Failed to initialize connection", e);
        }
    }

    private void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS tasks (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT NOT NULL,
                    completed BOOLEAN DEFAULT 0,
                    description TEXT
                );
                """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (SQLException e) {
            throw new TaskStorageException(
                    "Failed to create table", e);
        }
    }

    void insert(String title, String description) {
        String sql =
                "INSERT INTO tasks (title, description) VALUES (?, ?)";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, title);
            statement.setString(2, description);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                log.info(
                        "Task successfully created in db | title='{}'",
                        title);
            } else {
                log.warn("Task not created | title='{}'", title);
            }
        } catch (SQLException e) {
            throw new TaskStorageException(
                    "Failed to create task", e);
        }
    }

    List<Task> selectAll() {
        String sql = "SELECT * FROM tasks";
        List<Task> tasks = new ArrayList<>();

        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {

            while (resultSet.next()) {
                tasks.add(new Task(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getBoolean("completed"),
                        resultSet.getString("description")
                ));
            }

            if (!tasks.isEmpty()) {
                log.info(
                        "Tasks successfully read from db | size='{}'",
                        tasks.size());
            } else {
                log.info("Task table is empty");
            }
        } catch (SQLException e) {
            throw new TaskStorageException(
                    "Failed to read tasks", e);
        }

        return tasks;
    }

    void updateTitle(int id, String newTitle) {
        String sql = "UPDATE tasks SET title = ? WHERE id = ?";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, newTitle);
            statement.setInt(2, id);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                log.info(
                        "Task title successfully updated in db | id='{}'",
                        id);
            } else {
                log.warn("Task not found for title update | id='{}'", id);
            }
        } catch (SQLException e) {
            throw new TaskStorageException(
                    "Failed to update task title", e);
        }
    }

    void updateDescription(int id, String newDescription) {
        String sql = "UPDATE tasks SET description = ? WHERE id = ?";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setString(1, newDescription);
            statement.setInt(2, id);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                log.info(
                        "Task description successfully updated in db | id='{}'",
                        id);
            } else {
                log.warn(
                        "Task not found for description update | id='{}'",
                        id);
            }
        } catch (SQLException e) {
            throw new TaskStorageException(
                    "Failed to update task description", e);
        }
    }

    void complete(int id) {
        String sql =
                "UPDATE tasks SET completed = (1 - completed) WHERE id = ?";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                log.info(
                        "Task status successfully updated in db | id='{}'",
                        id);
            } else {
                log.warn(
                        "Task not found for status update | id='{}'",
                        id);
            }
        } catch (SQLException e) {
            throw new TaskStorageException(
                    "Failed to update task status", e);
        }
    }

    void delete(int id) {
        String sql = "DELETE FROM tasks WHERE id = ?";

        try (PreparedStatement statement =
                     connection.prepareStatement(sql)) {

            statement.setInt(1, id);

            int rowsAffected = statement.executeUpdate();

            if (rowsAffected > 0) {
                log.info(
                        "Task successfully deleted from db | id='{}'",
                        id);
            } else {
                log.warn("Task not found for deletion | id='{}'", id);
            }
        } catch (SQLException e) {
            throw new TaskStorageException(
                    "Failed to delete task", e);
        }
    }

    @Override
    public void close() {
        if (connection == null) {
            return;
        }

        try {
            connection.close();
            connection = null;
            log.info("Database connection closed");
        } catch (SQLException e) {
            throw new TaskStorageException(
                    "Failed to close database connection", e);
        }
    }
}
/*package com.github.alde0.todocore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class TaskRepository {
    private static final Logger log = LoggerFactory.getLogger(TaskRepository.class);

    private Connection connection;

    void initDb() {
        initConnection();
        createTable();
    }
    private void initConnection() {
        try {
            connection = DriverManager.getConnection("jdbc:sqlite:todo.db");
        } catch (SQLException e) {
            throw new TaskStorageException("Failed to initialize connection", e);
        }
    }
    private void createTable() {
        String sql = """
                CREATE TABLE IF NOT EXISTS tasks (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    title TEXT NOT NULL,
                    completed BOOLEAN DEFAULT 0,
                    description TEXT
                );
                """;
        try (Statement statement = connection.createStatement()) {
            statement.execute(sql);
        } catch (Exception e) {
            throw new TaskStorageException("Failed to create table", e);
        }
    }
    void insert(String title, String description) {
        String sql = "INSERT INTO tasks (title, description) VALUES (?, ?)";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, title);
            preparedStatement.setString(2, description);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0)
                log.info("Task successfully created in db | title='{}'", title);
            else
                log.warn("Task not created | title='{}'", title);
        } catch (SQLException e) {
            throw new TaskStorageException("Failed to create task", e);
        }
    }
    List<Task> selectAll() {
        String sql = "SELECT * FROM tasks";

        List<Task> tasks = new ArrayList<>();
        try (Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery(sql)) {
            while (resultSet.next()) {
                tasks.add(new Task(
                        resultSet.getInt("id"),
                        resultSet.getString("title"),
                        resultSet.getBoolean("completed"),
                        resultSet.getString("description")
                ));
            }
            if (!tasks.isEmpty())
                log.info("Tasks successfully read from db | size='{}'", tasks.size());
            else
                log.info("Task tables is empty");
        } catch (SQLException e) {
            throw new TaskStorageException("Failed to read tasks", e);
        }
        return tasks;
    }
    void updateTitle(int id, String newTitle)  {
        String sql = "UPDATE tasks SET title = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newTitle);
            preparedStatement.setInt(2, id);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0)
                log.info("Task title successfully updated in db | id='{}'", id);
            else
                log.warn("Task not found for title update | id='{}'", id);
        } catch (SQLException e) {
            throw new TaskStorageException("Failed to update task", e);
        }
    }
    void updateDescription(int id, String newDescription) {
        String sql = "UPDATE tasks SET description = ? WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setString(1, newDescription);
            preparedStatement.setInt(2, id);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0)
                log.info("Task description successfully updated in db | id='{}'", id);
            else
                log.warn("Task not found for description update | id='{}'", id);
        } catch (SQLException e) {
            throw new TaskStorageException("Failed to update task", e);
        }
    }
    void complete(int id) {
        String sql = "UPDATE tasks SET completed = (1 - completed) WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0)
                log.info("Task status successfully updated in db | id='{}'", id);
            else
                log.warn("Task not found for status update | id='{}'", id);
        } catch (SQLException e) {
            throw new TaskStorageException("Failed to update task", e);
        }
    }
    void delete(int id) {
        String sql = "DELETE FROM tasks WHERE id = ?";
        try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
            preparedStatement.setInt(1, id);

            int rowsAffected = preparedStatement.executeUpdate();
            if (rowsAffected > 0)
                log.info("Task successfully deleted from db | id='{}'", id);
            else
                log.warn("Task not found for deletion | id='{}'", id);
        } catch (SQLException e) {
            throw new TaskStorageException("Failed to delete task", e);
        }
    }
}*/