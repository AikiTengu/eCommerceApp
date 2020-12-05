package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.CreateUserRequest;
import com.example.demo.security.JWTAuthenticationFilter;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {

    private UserController userController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);

    @Before
    public void setup() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);
    }

    private JWTAuthenticationFilter auth = mock(JWTAuthenticationFilter.class);

    @Test
    public void allGoodRunCreate() {
        when(encoder.encode("qwertyuiop")).thenReturn("encodedPassword");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("Abaddon");
        r.setPassword("qwertyuiop");
        r.setConfirmPassword("qwertyuiop");

        ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals(r.getUsername(), u.getUsername());
        assertEquals("encodedPassword", u.getPassword());
    }

    @Test
    public void BadRunInvalidPasswords() {

        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("Abaddon");
        r.setPassword("qwertyuiop");
        r.setConfirmPassword("qwertyuiodp");

        when(encoder.encode("qwertyuiop")).thenReturn("encodedPassword");

        ResponseEntity<User> response = userController.createUser(r);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void FindByIdLogged() {
        when(encoder.encode("qwertyuiop")).thenReturn("encodedPassword");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("Abaddon");
        r.setPassword("qwertyuiop");
        r.setConfirmPassword("qwertyuiop");

        ResponseEntity<User> response = userController.createUser(r);

        when(encoder.matches(r.getPassword(),encoder.encode(r.getPassword()))).thenReturn(true);
        ResponseEntity<User> response1 = userController.findById(0L);

        assertNotNull(response1);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals(r.getUsername(), u.getUsername());
        assertEquals("encodedPassword", u.getPassword());

    }

    @Test
    public void FindByNameLogged() {
        when(encoder.encode("qwertyuiop")).thenReturn("encodedPassword");
        CreateUserRequest r = new CreateUserRequest();
        r.setUsername("Abaddon");
        r.setPassword("qwertyuiop");
        r.setConfirmPassword("qwertyuiop");

        ResponseEntity<User> response = userController.createUser(r);

        when(encoder.matches(r.getPassword(),encoder.encode(r.getPassword()))).thenReturn(true);
        ResponseEntity<User> response1 = userController.findByUserName(r.getUsername());

        assertNotNull(response1);
        assertEquals(200, response.getStatusCodeValue());

        User u = response.getBody();
        assertNotNull(u);
        assertEquals(0, u.getId());
        assertEquals(r.getUsername(), u.getUsername());
        assertEquals("encodedPassword", u.getPassword());

    }
}
