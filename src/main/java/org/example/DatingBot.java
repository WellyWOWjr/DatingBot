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


//https://api.telegram.org/bot6216527032:AAHLBgtiiyHK6ZMkIluoiFi6PjpZsictQUk/setWebhook?url=https://edd9-46-53-253-234.ngrok-free.app
@Component
public class DatingBot extends SpringWebhookBot {
    private static final Logger log = LoggerFactory.getLogger(DatingBot.class);

    public static String PATH = "https://edd9-46-53-253-234.ngrok-free.app";
    Map<Long, DatingRunner> chats = new HashMap<>();

    public DatingBot() {
        super(
                SetWebhook.builder().url(PATH).build(),
                "6216527032:AAHLBgtiiyHK6ZMkIluoiFi6PjpZsictQUk");
    }

    @Override
    public BotApiMethod<?> onWebhookUpdateReceived(Update update) {
        log.info("Initial update {}", update);
        // = sout (вывод информации в консоль)
        try {
            Message message = getMessage(update);
            if (chats.get(message.getChatId()) == null) {
                return runStart(message);
                //нет профиля - создаем
            }
            if (update.hasCallbackQuery()) {
                log.info("Callback");
                DatingRunner runner = chats
                        .get(update.getCallbackQuery().getMessage().getChatId());
                BotApiMethod<?> callbackResponse = runner.runCallBack(update);
                execute(callbackResponse);
                return runner.runShow(message);
                //если есть Callback, создаем runner для конкретного ChatId и метод callbackResponse
            }
            if (update.getMessage().isCommand()) {
                return handleCommand(update.getMessage());
                //если ввели команду, выполняем соответствующий метод
            }
            return handleMessage(update.getMessage());
            //ни один if не подходит - выполняем handleMessage()
        } catch (Exception e) {
            log.error("GG", e);
            //если сообщение не получено(handleMessage() возвращает null) выводим GG в консоль
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
        //если message - сообщение, то возвращаем его, иначе нажали на клавиатуру, message = callback, return message
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
