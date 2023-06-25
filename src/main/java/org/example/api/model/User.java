package org.example.api.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;

@RedisHash("user")
@Getter
@Setter
@NoArgsConstructor
public class User {
    @Id
    private Long chatId;
    private State state;
    private Profile profile = new Profile();

    public User(Long chatId) {
        state = State.START;
        profile = new Profile();
        this.chatId = chatId;
    }
}
