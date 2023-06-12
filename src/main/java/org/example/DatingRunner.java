package org.example;

import java.util.ArrayList;
import java.util.List;

import org.telegram.telegrambots.meta.api.methods.BotApiMethod;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.Message;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

public class DatingRunner implements Runner {
    private final Profile profile;
    private final InlineKeyboardMaker keyboardMaker = new InlineKeyboardMaker();
    private State state = State.START;

    public DatingRunner() {
        this.profile = new Profile();
    }


    @Override
    public BotApiMethod<?> run(Message message) {
        switch (state) {
            case START: {
                return runStart(message);
            }
            case NAME: {
                return runName(message);
            }
            case AGE: {
                return runAge(message);
            }
            case QUESTION: {
                return runQuestion(message);
            }
            case SHOW:
                return runShow(message);

        }
        return null;
    }

    public BotApiMethod<?> runShow(Message message) {
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
        SendMessage sendMessage = new SendMessage(message.getChatId().toString(), profile);
        sendMessage.setReplyMarkup(markup);
        return sendMessage;
    }

    public SendMessage runStart(Message message) {
        String response = changeStateTo(State.NAME);
        return new SendMessage(message.getChatId().toString(), response);
    }

    private SendMessage runQuestion(Message message) {
        //todo new state
        String response = changeStateTo(State.SHOW);
        return new SendMessage(message.getChatId().toString(), response);
    }

    private SendMessage runAge(Message message) {
        profile.setAge(message.getText());
        String response = String.format(
                State.PROFILE.getPhrase(),
                profile.getName(),
                profile.getAge());
        changeStateTo(State.SHOW);
        return new SendMessage(message.getChatId().toString(), response);
    }

    private SendMessage runName(Message message) {
        profile.setName(message.getText());
        String response = changeStateTo(State.AGE);
        return new SendMessage(message.getChatId().toString(), response);
    }

    public SendMessage runMe(Message message) {
        String response = profile.toString();
        return new SendMessage(message.getChatId().toString(), response);
    }

    private String changeStateTo(State state) {
        this.state = state;
        System.out.println(profile);
        return state.getPhrase();
    }

    public BotApiMethod<?> runCallBack(Update update) {
        String data = update.getCallbackQuery().getData();
        SendMessage sendMessage = new SendMessage(
                update.getCallbackQuery().getMessage().getChatId().toString(),
                "Your answer was " + data);
        return sendMessage;
    }
}
