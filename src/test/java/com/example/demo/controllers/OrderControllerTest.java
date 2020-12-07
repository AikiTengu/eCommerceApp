package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.UserOrder;
import com.example.demo.model.persistence.repositories.OrderRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;


import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;

public class OrderControllerTest {
    private OrderController orderController;

    private UserRepository userRepository = mock(UserRepository.class);

    private OrderRepository orderRepository = mock(OrderRepository.class);

    @Before
    public void setup() {
        orderController = new OrderController();
        TestUtils.injectObjects(orderController, "userRepository", userRepository);
        TestUtils.injectObjects(orderController, "orderRepository", orderRepository);
    }
/*
    @Test
    public void submitOrder() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("test1");
        Cart cart = new Cart();
        user.setCart(cart);

        BigDecimal bg = new BigDecimal(2.99);
        Item item = new Item();
        item.setId(1L);
        item.setName("Apple");
        item.setPrice(bg);
        item.setDescription("It's a fruit");
        List<Item> addItems = new ArrayList<>();
        addItems.add(item);
        cart.setItems(addItems);
        Mockito.when(userRepo.findByUsername(user.getUsername())).thenReturn(user);

        ResponseEntity<UserOrder> response = orderController.submit(user.getUsername());
        assertEquals(200, response.getStatusCodeValue());
        UserOrder output = response.getBody();
        assertEquals("It's a fruit", output.getItems().get(0).getDescription());
    }

    @Test
    public void getOrdersForUserTest() {
        User user = new User();
        user.setUsername("test");
        user.setPassword("test1");
        Cart cart = new Cart();
        user.setCart(cart);
        Mockito.when(userRepo.findByUsername(user.getUsername())).thenReturn(user);

        BigDecimal bg = new BigDecimal(2.99);
        Item item = new Item();
        item.setId(1L);
        item.setName("Apple");
        item.setPrice(bg);
        item.setDescription("It is a fruit");
        List<Item> addItems = new ArrayList<>();
        addItems.add(item);
        cart.setItems(addItems);

        UserOrder order = new UserOrder();
        order.setItems(addItems);
        order.setUser(user);
        order.setTotal(bg);

        List<UserOrder> orderList = new ArrayList<>();
        orderList.add(order);

        Mockito.when(orderRepo.findByUser(user)).thenReturn(orderList);

        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("test");
        assertEquals(200, response.getStatusCodeValue());
        List<UserOrder> orderHistory = response.getBody();
        assertEquals("Apple", orderHistory.get(0).getItems().get(0).getName());
    }
*/
    @Test
    public void submitNotLoggedIn() {
        ResponseEntity<UserOrder> response = orderController.submit("Beholder");
        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getOrdersNotLoggedIn() {
        ResponseEntity<List<UserOrder>> response = orderController.getOrdersForUser("Beholder");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}