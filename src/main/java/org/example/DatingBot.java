package org.example;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;


//https://api.telegram.org/bot6216527032:AAHLBgtiiyHK6ZMkIluoiFi6PjpZsictQUk/setWebhook?url=https://datingbot.onrender.com
@Component
public class DatingBot extends SpringWebhookBot {
    private static final Logger log = LoggerFactory.getLogger(DatingBot.class);

    public static String PATH = "https://4fec-151-249-141-199.eu.ngrok.io";
    Map<Long, DatingRunner> chats = new HashMap<>();

    public DatingBot() {
        super(
                SetWebhook.builder().url(PATH).build(),
                "6216527032:AAHLBgtiiyHK6ZMkIluoiFi6PjpZsictQUk");
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        log.info("Initial update {}", update);
        try {
            Message message = getMessage(update);
            if (chats.get(message.getChatId()) == null) {
                return runStart(message);
            }
            if (update.hasCallbackQuery()) {
                log.info("Callback");
                DatingRunner runner = chats
                        .get(update.getCallbackQuery().getMessage().getChatId());
                BotApiMethod<?> callbackResponse = runner.runCallBack(update);
                execute(callbackResponse);
                return runner.runShow(message);
            }
            if (update.getMessage().isCommand()) {
                return handleCommand(update.getMessage());
            }
            return handleMessage(update.getMessage());
        } catch (Exception e) {
            log.error("GG", e);
        }
        return new SendMessage();
    }

    private Message getMessage(Update update) {
        Message message;
        if (update.getMessage() != null) {
            message = update.getMessage();
        } else {
            message = update.getCallbackQuery().getMessage();
        }
        return message;
    }

    private BotApiMethod<?> handleCommand(Message message) {
        String command = message.getText();
        switch (command) {
            case "/start": {
                return runStart(message);
            }
            case "/me": {
                return chats.get(message.getChatId()).runMe(message);
            }
            case "/show": {
                return chats.get(message.getChatId()).runShow(message);
            }
        }
        //Todo
        return null;
    }

    private BotApiMethod<?> runStart(Message message) {
        chats.put(message.getChatId(), new DatingRunner());
        return chats.get(message.getChatId()).runStart(message);
    }


    private BotApiMethod<?> handleMessage(Message message) {
        return chats.get(message.getChatId()).run(message);
    }


    @Override
    public String getBotPath() {
        return PATH;
    }

    @Override
    public String getBotUsername() {
        return "CirDatingBot";
    }


}
