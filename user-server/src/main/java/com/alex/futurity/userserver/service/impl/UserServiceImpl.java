package com.alex.futurity.userserver.service.impl;

import com.alex.futurity.userserver.entity.User;
import com.alex.futurity.userserver.repo.UserRepository;
import com.alex.futurity.userserver.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

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
}
