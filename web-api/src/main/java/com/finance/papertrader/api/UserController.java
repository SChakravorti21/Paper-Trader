package com.finance.papertrader.api;

import com.finance.papertrader.api.requests.user.CreateUser;
import com.finance.papertrader.models.User;
import com.finance.papertrader.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@RestController
@RequestMapping(path = "/user")
public class UserController {

    private UserRepository userRepository;

    @Autowired
    public UserController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping
    public User getUser(@RequestParam String username) {
        Optional<User> user = this.userRepository.findById(username);

        if (user.isPresent()) {
            return user.get();
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found");
        }
    }

    @PostMapping(path = "/register")
    public CreateUser.Response createUser(@RequestBody CreateUser.Request request) {
        User user = this.userRepository.save(new User(request.getUsername()));
        return new CreateUser.Response(user.getUsername());
    }

}
