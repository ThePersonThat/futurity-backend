package com.alex.futurity.userserver.service;

import com.alex.futurity.userserver.entity.User;
import org.springframework.core.io.Resource;

import java.util.Optional;

public interface UserService {
    boolean isUserExist(String email);
    void saveUser(User user);
    Optional<User> findUserByEmail(String email);
    Resource findUserAvatar(long id);
}
