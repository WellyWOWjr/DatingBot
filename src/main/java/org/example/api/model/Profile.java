package org.example.api.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Profile {
    private String name;
    private String age;
    private String question;

    @Override
    public String toString() {
        return "Твой профиль:\uD83D\uDC40\nТвоё имя: " + name + ".\nТвой возраст: " + age + ".";
    }
}
