package org.example;

import java.util.HashMap;
import java.util.Map;

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
    public static String PATH = "https://datingbot.onrender.com";
    Map<Long, DatingRunner> chats = new HashMap<>();

    public DatingBot() {
        super(
                SetWebhook.builder().url(PATH).build(),
                "6216527032:AAHLBgtiiyHK6ZMkIluoiFi6PjpZsictQUk");
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        System.out.println(update);
        if (update.getMessage().isCommand()) {
            return handleCommand(update.getMessage());
        }
        return handleMessage(update.getMessage());
    }

    private BotApiMethod<?> handleCommand(Message message) {
        String command = message.getText();
        switch (command) {
            case "/start": {
                chats.put(message.getChatId(), new DatingRunner());
                String response = chats.get(message.getChatId()).runStart();
                return new SendMessage(message.getChatId().toString(), response);
            }
            case "/me": {
                String response;
                if (chats.get(message.getChatId()) != null) {
                    response = chats.get(message.getChatId()).runMe();
                } else {
                    response = "You don't have profile.\uD83E\uDEE0";
                }
                return new SendMessage(message.getChatId().toString(), response);
            }
        }
        //Todo
        return null;
    }


    private BotApiMethod<?> handleMessage(Message message) {
        String request = message.getText();
        String response = chats.get(message.getChatId()).run(request);
        return new SendMessage(message.getChatId().toString(), response);
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
