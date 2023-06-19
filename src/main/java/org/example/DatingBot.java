package org.example;

import org.example.api.service.CallBackService;
import org.example.api.service.MessageService;
import org.example.api.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.starter.SpringWebhookBot;


//https://api.telegram.org/bot6216527032:AAHLBgtiiyHK6ZMkIluoiFi6PjpZsictQUk/setWebhook?url=https://5fee-151-249-132-109.eu.ngrok.io
@Component
public class DatingBot extends SpringWebhookBot {
    private static final Logger log = LoggerFactory.getLogger(DatingBot.class);

    public static String PATH = "https://5fee-151-249-132-109.eu.ngrok.io";

    private final MessageService messageService;
    private final CallBackService callBackService;
    private final UserService userService;

    public DatingBot(
            MessageService messageService,
            CallBackService callBackService,
            UserService userService) {
        super(
                SetWebhook.builder().url(PATH).build(),
                "6216527032:AAHLBgtiiyHK6ZMkIluoiFi6PjpZsictQUk");
        this.messageService = messageService;
        this.callBackService = callBackService;
        this.userService = userService;
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        log.info("Initial update {}", update);
        try {
            Message message = getMessage(update);
            if (!userService.existsUser(message.getChatId().toString())) {
                return messageService.runStart(message);
            }
            if (update.hasCallbackQuery()) {
                BotApiMethod<?> callbackResponse = callBackService.runCallBack(update);
                execute(callbackResponse);
                return messageService.runShow(message);
            }
            if (update.getMessage().isCommand()) {
                return handleCommand(update.getMessage());
            }
            return messageService.run(message);
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
                return messageService.runStart(message);
            }
            case "/me": {
                return messageService.runMe(message);
            }
            case "/show": {
                return messageService.runShow(message);
            }
        }
        throw new UnsupportedOperationException("unknown command");
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
