# Java To-Do Bot

A simple Telegram To-Do bot written in Java. It allows users to manage tasks through a conversational interface.

## Features

- Create tasks with a title and description.
- List all stored tasks.
- Change task titles.
- Change task descriptions.
- Mark tasks as completed.
- Delete tasks.
- Cancel an ongoing operation.
- Store tasks in an SQLite database.

## Commands

| Command | Description |
|---|---|
| `/start` | Start the bot and display available commands |
| `/help` | Show the command list |
| `/createTask` | Create a task through a dialog |
| `/listAllTasks` | Display all tasks |
| `/editTaskName` | Change a task title |
| `/editTaskDescription` | Change a task description |
| `/editTaskStatus` | Mark a task as completed |
| `/deleteTask` | Delete a task |
| `/cancel` | Cancel the current operation |

Some commands require additional input. Follow the bot's instructions to complete the operation.

### Built With

- Java — application logic

- Telegram Bots API — Telegram integration

- SQLite — task storage

- JDBC — database access

## Project Structure

The project separates Telegram interaction from task management and persistence.

- `TelegramBot` — receives Telegram updates and sends responses.
- `TextMessageProcessor` — processes commands and manages conversational input.
- `TaskService` — validates task operations and coordinates the repository.
- `TaskRepository` — handles database operations.
- `Task` — represents a task and formats its output.

## License

This project is distributed under the MIT License. See [LICENSE](LICENSE) for details.