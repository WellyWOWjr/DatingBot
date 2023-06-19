package org.example.api.service;

import org.example.api.repo.UserRepository;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Update;

@Service
public class CallBackService {

    private final UserRepository userRepository;

    public CallBackService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public BotApiMethod<?> runCallBack(Update update) {
        userRepository.findById(update.getCallbackQuery().getMessage().getChatId().toString());
        String data = update.getCallbackQuery().getData();
        SendMessage sendMessage = new SendMessage(
                update.getCallbackQuery().getMessage().getChatId().toString(),
                "Your answer was " + data);
        return sendMessage;
    }
}

