package org.example;

public class DatingRunner implements Runner {
    private State state = State.START;

    private final Profile profile;

    public DatingRunner() {
        this.profile = new Profile();

    }

    @Override
    public String run(String message) {
        switch (state) {
            case START: {
                return runStart();
            }
            case NAME: {
                return runName(message);
            }
            case AGE: {
                return runAge(message);
            }
            case QUESTION: {
                return runQuestion();
            }
        }
        return null;
    }

    public String runStart() {
        return changeStateTo(State.NAME);
    }

    private String runQuestion() {
        //todo new state
        return changeStateTo(State.QUESTION);
    }

    private String runAge(String age) {
        profile.setAge(age);
        return String.format(State.PROFILE.getPhrase(), profile.getName(), profile.getAge());
    }

    private String runName(String name) {
        profile.setName(name);
        return changeStateTo(State.AGE);
    }

    public String runMe() {
        return profile.toString();
    }

    private String changeStateTo(State state) {
        this.state = state;
        System.out.println(profile);
        return state.getPhrase();
    }

}
