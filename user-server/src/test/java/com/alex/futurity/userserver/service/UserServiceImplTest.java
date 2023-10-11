package com.alex.futurity.userserver.service;

import com.alex.futurity.userserver.entity.User;
import com.alex.futurity.userserver.repo.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepo;

    @InjectMocks
    private UserService userService;

    @Test
    @DisplayName("Should return true if an user exists")
    void testIsUserExistWhenUserExists() {
        User user = new User();
        user.setEmail("alex@gmail.com");

        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user));

        assertThat(userService.isUserExist(user.getEmail())).isTrue();
    }

    @Test
    @DisplayName("Should return false if an user does not exist")
    void testIsUserExistWhenUserDoesNotExist() {
        when(userRepo.findByEmail(anyString())).thenReturn(Optional.empty());

        assertThat(userService.isUserExist(anyString())).isFalse();
    }

    @Test
    @DisplayName("Should call the save method with the user")
    void testSave() {
        User user = new User();
        userService.saveUser(user);

        verify(userRepo).save(eq(user));
    }

    @Test
    @DisplayName("Should return the user if it exists")
    void findUserByEmailIfUserExists() {
        User user = new User();

        when(userRepo.findByEmail(anyString())).thenReturn(Optional.of(user));

        Optional<User> userByEmail = userService.findUserByEmail(anyString());

        assertThat(userByEmail.isPresent()).isTrue();
        assertThat(userByEmail.get()).isEqualTo(user);
    }

    @Test
    @DisplayName("Should return the Optional's null if user does not exist")
    void findUserByEmailIfUserDoesNotExist() {
        Optional<User> user = userService.findUserByEmail(anyString());

        assertThat(user.isPresent()).isFalse();
    }
}