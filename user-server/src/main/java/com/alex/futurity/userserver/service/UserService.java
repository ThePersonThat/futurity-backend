package com.alex.futurity.userserver.service;

import com.alex.futurity.userserver.entity.User;
import com.alex.futurity.userserver.exception.ClientSideException;
import com.alex.futurity.userserver.repo.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserService {
    private final UserRepository userRepo;


    public boolean isUserExist(String email) {
        return userRepo.findByEmail(email).isPresent();
    }


    public void saveUser(User user) {
        userRepo.save(user);
    }


    public Optional<User> findUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }


    public Resource findUserAvatar(long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ClientSideException(String.format("User with id \"%s\" is not found", id), HttpStatus.NOT_FOUND));

        return new ByteArrayResource(user.getAvatar());
    }
}
