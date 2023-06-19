package org.example.api.service;

import java.util.ArrayList;
import java.util.List;

import org.example.api.repo.UserRepository;
import org.example.api.model.State;
import org.example.api.model.User;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

@Service
public class MessageService {

    private final UserRepository userRepository;

    public MessageService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    public BotApiMethod<?> run(Message message) {
        User user = userRepository
                .findById(message.getChatId().toString())
                .orElseThrow();
        switch (user.getState()) {
            case START: {
                return runStart(message);
            }
            case NAME: {
                return runName(message, user);
            }
            case AGE: {
                return runAge(message, user);
            }
            case QUESTION: {
                return runQuestion(message, user);
            }
            case SHOW:
                return runShow(user);

        }
        return null;
    }


    public BotApiMethod<?> runShow(Message message) {
        User user = userRepository.findById(message.getChatId().toString()).orElseThrow();
        return runShow(user);
    }

    private SendMessage runShow(User user) {
        List<List<InlineKeyboardButton>> buttons = new ArrayList<>();
        List<InlineKeyboardButton> buttonLine = new ArrayList<>();
        InlineKeyboardButton yes = new InlineKeyboardButton();
        yes.setText("YES");
        yes.setCallbackData("YES");
        InlineKeyboardButton no = new InlineKeyboardButton();
        no.setText("no");
        no.setCallbackData("no");
        buttonLine.add(yes);
        buttonLine.add(no);
        buttons.add(buttonLine);
        InlineKeyboardMarkup markup = new InlineKeyboardMarkup();
        markup.setKeyboard(buttons);

        String profile = "Profile Example";
        SendMessage sendMessage = new SendMessage(user.getChatId(), profile);
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }

    public SendMessage runStart(Message message) {
        String chatId = message.getChatId().toString();
        User user = new User(chatId);
        userRepository.save(user);
        String response = changeStateTo(State.NAME, user);
        return new SendMessage(chatId, response);
    }

    private SendMessage runQuestion(Message message, User user) {
        //todo new state
        String response = changeStateTo(State.SHOW, user);
        return new SendMessage(message.getChatId().toString(), response);
    }

    private SendMessage runAge(Message message, User user) {
        user.getProfile().setAge(message.getText());
        String response = String.format(
                State.PROFILE.getPhrase(),
                user.getProfile().getName(),
                user.getProfile().getAge());
        changeStateTo(State.SHOW, user);
        return new SendMessage(message.getChatId().toString(), response);
    }

    private SendMessage runName(Message message, User user) {
        user.getProfile().setName(message.getText());
        String response = changeStateTo(State.AGE, user);
        return new SendMessage(message.getChatId().toString(), response);
    }

    public SendMessage runMe(Message message) {
        User user = userRepository.findById(message.getChatId().toString()).orElseThrow();
        return runMe(message, user);
    }

    public SendMessage runMe(Message message, User user) {
        String response = user.getProfile().toString();
        return new SendMessage(message.getChatId().toString(), response);
    }

    private String changeStateTo(State state, User user) {
        user.setState(state);
        userRepository.save(user);
        return state.getPhrase();
    }


}
