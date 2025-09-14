package com.example.dao;

import com.example.model.User;
import com.example.util.HibernateUtil;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class UserDAOTest {
    private static UserDAO userDAO;
    private static User testUser;

    @BeforeAll
    static void setUp() {
        userDAO = new UserDAOImpl();
    }

    @Test
    @Order(1)
    void testCreateUser() {
        testUser = new User("Test User", "test@example.com", 25);
        User createdUser = userDAO.createUser(testUser);

        assertNotNull(createdUser.getId());
        assertEquals("Test User", createdUser.getName());
        assertEquals("test@example.com", createdUser.getEmail());
        assertEquals(25, createdUser.getAge());
    }

    @Test
    @Order(2)
    void testGetUserById() {
        Optional<User> foundUser = userDAO.getUserById(testUser.getId());
        assertTrue(foundUser.isPresent());
        assertEquals(testUser.getEmail(), foundUser.get().getEmail());
    }

    @Test
    @Order(3)
    void testGetAllUsers() {
        List<User> users = userDAO.getAllUsers();
        assertFalse(users.isEmpty());
    }

    @Test
    @Order(4)
    void testUpdateUser() {
        testUser.setName("Updated User");
        testUser.setAge(30);

        User updatedUser = userDAO.updateUser(testUser);
        assertEquals("Updated User", updatedUser.getName());
        assertEquals(30, updatedUser.getAge());
    }

    @Test
    @Order(5)
    void testGetUserByEmail() {
        Optional<User> foundUser = userDAO.getUserByEmail("test@example.com");
        assertTrue(foundUser.isPresent());
        assertEquals("Updated User", foundUser.get().getName());
    }

    @Test
    @Order(6)
    void testDeleteUser() {
        boolean deleted = userDAO.deleteUser(testUser.getId());
        assertTrue(deleted);

        Optional<User> foundUser = userDAO.getUserById(testUser.getId());
        assertFalse(foundUser.isPresent());
    }

    @AfterAll
    static void tearDown() {
        HibernateUtil.shutdown();
    }
}