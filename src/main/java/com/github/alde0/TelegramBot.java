package com.github.alde0;

import com.github.alde0.todocore.TaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.telegram.telegrambots.client.okhttp.OkHttpTelegramClient;
import org.telegram.telegrambots.longpolling.interfaces.LongPollingUpdateConsumer;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;
import org.telegram.telegrambots.meta.generics.TelegramClient;

import java.util.List;
import java.util.Objects;

public class TelegramBot implements LongPollingUpdateConsumer {

    private static final Logger log =
            LoggerFactory.getLogger(TelegramBot.class);

    private final TelegramClient telegramClient;
    private final TextMessageProcessor textMessageProcessor;

    public TelegramBot(String botToken, TaskService taskService) {
        Objects.requireNonNull(botToken, "Bot token cannot be null");
        Objects.requireNonNull(taskService, "TaskService cannot be null");

        telegramClient = new OkHttpTelegramClient(botToken);
        textMessageProcessor = new TextMessageProcessor(taskService);
    }

    @Override
    public void consume(List<Update> updates) {
        for (Update update : updates) {
            handleSingleUpdate(update);
        }
    }

    private void handleSingleUpdate(Update update) {
        if (update.hasMessage() && update.getMessage().hasText()) {
            try {
                telegramClient.execute(
                        textMessageProcessor.processText(
                                update.getMessage().getText(),
                                update.getMessage().getChatId()));

                log.info("Message successfully sent");
            } catch (TelegramApiException e) {
                log.error("Failed to send Telegram message", e);
            }
        } else {
            log.warn("Unprocessable update");
        }
    }
}