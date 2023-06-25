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
        return "Профиль:\uD83D\uDC40\nИмя: " + name + ".\nВозраст: " + age + ".";
    }
}
