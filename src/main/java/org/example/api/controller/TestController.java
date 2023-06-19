package org.example.api.controller;

import org.example.api.model.User;
import org.example.api.repo.UserRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/test")
public class TestController {

    private final UserRepository userRepository;

    public TestController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @PostMapping("/user/save")
    public User save(@RequestBody User user) {
        return userRepository.save(user);
    }

    @GetMapping("/user")
    public Iterable<User> getAll() {
        return userRepository.findAll();
    }

}
