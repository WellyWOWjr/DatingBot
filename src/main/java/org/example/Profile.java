package org.example;

public class Profile {
    private String name;
    private String age;
    private String question;

    public String getName() {
        return name;
    }

    public String getAge() {
        return age;
    }

    public String getQuestion() {
        return question;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    @Override
    public String toString() {
        return "Твой профиль:\uD83D\uDC40\nТвоё имя: " + name + ".\nТвой возраст: " + age + ".";
    }
}
