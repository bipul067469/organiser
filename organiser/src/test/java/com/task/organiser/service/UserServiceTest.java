package com.task.organiser.service;

import com.task.organiser.entity.User;
import com.task.organiser.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTests {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        user = new User();
        user.setUsername("testuser");
        user.setPassword("password123");
    }

    @Test
    void register_ShouldEncodePasswordAndSaveUser() {

        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(user);

        User registeredUser = userService.register(user);

        assertNotNull(registeredUser);
        assertEquals(user.getUsername(), registeredUser.getUsername());
        assertEquals("encodedPassword", registeredUser.getPassword());

        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(user);
    }



    @Test
    void login_ShouldReturnUserWhenCredentialsAreValid() {
        user.setPassword("encodedPassword");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(passwordEncoder.matches("password123", user.getPassword())).thenReturn(true);

        User loggedInUser = userService.login(user.getUsername(), "password123");

        assertNotNull(loggedInUser);
        assertEquals(user.getUsername(), loggedInUser.getUsername());
        verify(userRepository).findByUsername(user.getUsername());
        verify(passwordEncoder).matches("password123", user.getPassword());
    }

    @Test
    void login_ShouldReturnNullWhenUserNotFound() {
        when(userRepository.findByUsername(user.getUsername())).thenReturn(null);

        User loggedInUser = userService.login(user.getUsername(), "password123");

        assertNull(loggedInUser);
        verify(userRepository).findByUsername(user.getUsername());
    }

    @Test
    void login_ShouldReturnNullWhenPasswordIsInvalid() {
        user.setPassword("encodedPassword");
        when(userRepository.findByUsername(user.getUsername())).thenReturn(user);
        when(passwordEncoder.matches("wrongPassword", user.getPassword())).thenReturn(false);

        User loggedInUser = userService.login(user.getUsername(), "wrongPassword");

        assertNull(loggedInUser);
        verify(userRepository).findByUsername(user.getUsername());
        verify(passwordEncoder).matches("wrongPassword", user.getPassword());
    }
}
