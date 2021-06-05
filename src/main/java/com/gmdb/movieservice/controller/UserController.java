package com.gmdb.movieservice.controller;


import com.gmdb.movieservice.bean.User;
import com.gmdb.movieservice.dao.UserRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {
    private UserRepository userRepository;

    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }
    @PostMapping("/user")
    public User createNewUser(@RequestBody User user) {
        return this.userRepository.save(user);
    }
}
