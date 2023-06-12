package org.example;

import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;

@Component
public class InlineKeyboardMaker {

    public InlineKeyboardMarkup getKeyboardMarkup() {
        return new InlineKeyboardMarkup();
    }
}
