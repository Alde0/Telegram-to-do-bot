package com.github.alde0;

import com.github.alde0.todocore.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public class Main {

    private static final Logger log =
            LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        String botToken = System.getenv("BOT_TOKEN");

        if (botToken == null || botToken.isBlank()) {
            log.error("Environment variable BOT_TOKEN is not set");
            return;
        }

        try (TaskService taskService = new TaskService();
             TelegramBotsLongPollingApplication application =
                     new TelegramBotsLongPollingApplication()) {

            application.registerBot(
                    botToken,
                    new TelegramBot(botToken, taskService));

            log.info("Bot started");
            Thread.currentThread().join();

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.info("Application interrupted");
        } catch (Exception e) {
            log.error("Failed to start or run bot", e);
        }
    }
}
/*package com.github.alde0;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.longpolling.TelegramBotsLongPollingApplication;

public class Main {
    static void main() {
        String BOT_TOKEN = System.getenv("BOT_TOKEN");
        Logger log = LoggerFactory.getLogger(Main.class);

        try (TelegramBotsLongPollingApplication telegramBotsLongPollingApplication = new TelegramBotsLongPollingApplication()) {
            telegramBotsLongPollingApplication.registerBot(BOT_TOKEN, new TelegramBot(BOT_TOKEN));

            log.info("Bot created");
            Thread.currentThread().join();
        } catch (Exception e) {
            log.error("Invalid bot token");
            System.exit(1);
        }
    }
}*/