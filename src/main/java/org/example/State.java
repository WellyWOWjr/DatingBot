package org.example;

public enum State {
    START(""),
    AGE("Сколько тебе лет?\uD83D\uDC76"),
    QUESTION("Чо?"),
    NAME("Привет! \uD83D\uDC4B\nЯ тг бот для знакомства на кружочках. 👩‍❤️‍💋‍👨 Для начала давай заполним анкету.✅\nКак тебя зовут?\uD83E\uDD14"),
    PROFILE("Здорово!\uD83D\uDC4F Теперь у тебя есть свой профиль.✅\uD83C\uDF08\nТвоё имя: %s.\nТвой возраст: %s."),
    SHOW("");

    private final String phrase;

    public String getPhrase() {
        return phrase;
    }

    State(String phrase) {
        this.phrase = phrase;
    }


}
