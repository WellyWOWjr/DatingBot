package org.example.api.service;

import org.example.api.repo.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public boolean existsUser(String chatId) {
        return userRepository.existsById(chatId);
    }
}
