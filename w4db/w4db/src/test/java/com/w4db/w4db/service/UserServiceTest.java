package com.w4db.w4db.service;

import com.w4db.w4db.model.User;
import com.w4db.w4db.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllUsers() {
        User user1 = new User(1L, "user1", "user1@example.com", "password1");
        User user2 = new User(2L, "user2", "user2@example.com", "password2");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getAllUsers();
        assertEquals(2, users.size());
        assertEquals("user1", users.get(0).getUsername());
    }

    @Test
    void testGetAllUsers_ShouldPass() {
        User user1 = new User(1L, "user1", "user1@example.com", "password1");
        User user2 = new User(2L, "user2", "user2@example.com", "password2");

        when(userRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        List<User> users = userService.getAllUsers();
        assertEquals(2, users.size());
        assertEquals("user1", users.get(0).getUsername());
    }

    @Test
    void testGetAllUsers_ShouldFail() {
        when(userRepository.findAll()).thenReturn(Arrays.asList());

        List<User> users = userService.getAllUsers();
        assertEquals(2, users.size()); 
    }

    @Test
    void testUsernameExists_ShouldFail() {
        when(userRepository.existsByUsername("nonExistingUser")).thenReturn(false);

        assertTrue(userService.usernameExists("nonExistingUser"));
    }

    @Test
    void testSaveUser_ShouldFail() {
        User user = new User(1L, "failUser", "failUser@example.com", "plainPassword");

        when(passwordEncoder.encode("plainPassword")).thenReturn("plainPassword"); 

        userService.saveUser(user);

    
    }

    @Test
    void testSaveUser_Uncertain() {
        User user = new User(1L, "uncertainUser", "uncertainUser@example.com", "plainPassword");
        userService.saveUser(user);   
        assertNotEquals("plainPassword", user.getPassword()); 
    }

    @Test
    void testUsernameExists_Uncertain() { 
        boolean exists = userService.usernameExists("uncertainUser");
        
        if (exists) {
            assertTrue(exists);
        } else {
            assertFalse(exists);
        }
    }
    @Test
    void testGetAllUsers_Uncertain() {
        List<User> users = userService.getAllUsers();
        if (users.isEmpty()) {
            fail("No users found in the database");
        } else {
            assertNotNull(users);
        }
    }

    @Test
    void testGetUserById() {
        User user = new User(1L, "user1", "user1@example.com", "password1");

        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        Optional<User> foundUser = userService.getUserById(1L);
        assertTrue(foundUser.isPresent());
        assertEquals("user1", foundUser.get().getUsername());
    }

    @Test
    void testSaveUser() {
        User user = new User(1L, "user1", "user1@example.com", "password1");

        when(passwordEncoder.encode(user.getPassword())).thenReturn("encodedPassword1");
        when(userRepository.save(user)).thenReturn(user);

        userService.saveUser(user);

        assertEquals("encodedPassword1", user.getPassword());
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testDeleteUser() {
        Long userId = 1L;

        userService.deleteUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testUsernameExists() {
        when(userRepository.existsByUsername("existingUser")).thenReturn(true);
        when(userRepository.existsByUsername("nonExistingUser")).thenReturn(false);

        assertTrue(userService.usernameExists("existingUser"));
        assertFalse(userService.usernameExists("nonExistingUser"));
    }

    @Test
    void testEmailExists() {
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);
        when(userRepository.existsByEmail("nonExisting@example.com")).thenReturn(false);

        assertTrue(userService.emailExists("existing@example.com"));
        assertFalse(userService.emailExists("nonExisting@example.com"));
    }
}
