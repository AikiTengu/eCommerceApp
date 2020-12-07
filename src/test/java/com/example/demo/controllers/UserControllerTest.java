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

    private CreateUserRequest createUserRequest;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private BCryptPasswordEncoder encoder = mock(BCryptPasswordEncoder.class);


    @Before
    public void setup() {
        userController = new UserController();
        TestUtils.injectObjects(userController, "userRepository", userRepository);
        TestUtils.injectObjects(userController, "cartRepository", cartRepository);
        TestUtils.injectObjects(userController, "bCryptPasswordEncoder", encoder);

        createUserRequest = new CreateUserRequest();
        createUserRequest.setUsername("Beholder");
        createUserRequest.setPassword("qwertyuiop");
        createUserRequest.setConfirmPassword("qwertyuiop");

    }

    private JWTAuthenticationFilter auth = mock(JWTAuthenticationFilter.class);

    @Test
    public void allGoodRunCreate() {
        when(encoder.encode("qwertyuiop")).thenReturn("encodedPassword");

        ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        User user = response.getBody();
        assertNotNull(user);
        assertEquals(0, user.getId());
        assertEquals(createUserRequest.getUsername(), user.getUsername());
        assertEquals("encodedPassword", user.getPassword());
    }

    @Test
    public void FindByIdLoggedIn() {
        ResponseEntity<User> response = userController.createUser(createUserRequest);

        User userCreated = response.getBody();
        when(userRepository.findById(userCreated.getId())).thenReturn(java.util.Optional.of(userCreated));

        ResponseEntity<User> response1 = userController.findById(userCreated.getId());

        assertNotNull(response1);
        assertEquals(200, response1.getStatusCodeValue());

        User userLoggedIn = response1.getBody();
        assertNotNull(userLoggedIn);
        assertEquals(userCreated.getId(), userLoggedIn.getId());
        assertEquals(createUserRequest.getUsername(), userLoggedIn.getUsername());
    }

    @Test
    public void FindByNameLoggedIn() {
        ResponseEntity<User> response = userController.createUser(createUserRequest);

        User userCreated = response.getBody();
        when(userRepository.findByUsername(userCreated.getUsername())).thenReturn(userCreated);

        ResponseEntity<User> response1 = userController.findByUserName(userCreated.getUsername());

        assertNotNull(response1);
        assertEquals(200, response1.getStatusCodeValue());

        User userLoggedIn = response1.getBody();
        assertNotNull(userLoggedIn);
        assertEquals(userCreated.getId(), userLoggedIn.getId());
        assertEquals(createUserRequest.getUsername(), userLoggedIn.getUsername());
    }

    @Test
    public void BadRunInvalidPasswords() {

        createUserRequest.setConfirmPassword("qwertyuiopaf");
        when(encoder.encode("qwertyuiop")).thenReturn("encodedPassword");

        ResponseEntity<User> response = userController.createUser(createUserRequest);

        assertNotNull(response);
        assertEquals(400, response.getStatusCodeValue());
    }

    @Test
    public void FindByIdNotLoggedIn() {
        ResponseEntity<User> response = userController.findById(0L);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }

    @Test
    public void FindByNameNotLoggedIn() {
        ResponseEntity<User> response = userController.findByUserName("Beholder");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());

    }



}
