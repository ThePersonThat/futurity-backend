package com.alex.futurity.userserver.service.impl;

import com.alex.futurity.userserver.entity.User;
import com.alex.futurity.userserver.exception.ClientSideException;
import com.alex.futurity.userserver.repo.UserRepository;
import com.alex.futurity.userserver.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;

    @Override
    public boolean isUserExist(String email) {
        return userRepo.findByEmail(email).isPresent();
    }

    @Override
    public void saveUser(User user) {
        userRepo.save(user);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepo.findByEmail(email);
    }

    @Override
    public Resource findUserAvatar(long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new ClientSideException(String.format("User with id \"%s\" is not found", id), HttpStatus.NOT_FOUND));

        return new ByteArrayResource(user.getAvatar());
    }

    @Override
    @Transactional
    public User findById(long id) {
        return this.userRepo.findById(id).orElseThrow(() -> new ClientSideException(String.format("User with id \"%s\" is not found", id), HttpStatus.NOT_FOUND));
    }
}
