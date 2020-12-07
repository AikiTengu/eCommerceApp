package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Cart;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.User;
import com.example.demo.model.persistence.repositories.CartRepository;
import com.example.demo.model.persistence.repositories.ItemRepository;
import com.example.demo.model.persistence.repositories.UserRepository;
import com.example.demo.model.requests.ModifyCartRequest;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;

import java.math.BigDecimal;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class CartControllerTest {
    private CartController cartController;

    private UserRepository userRepository = mock(UserRepository.class);

    private CartRepository cartRepository = mock(CartRepository.class);

    private ItemRepository itemRepository = mock(ItemRepository.class);

    private User user;
    private Cart cart;
    private BigDecimal bd;
    private Item item;

    @Before
    public void setup() {
        cartController = new CartController();
        TestUtils.injectObjects(cartController, "userRepository", userRepository);
        TestUtils.injectObjects(cartController, "cartRepository", cartRepository);
        TestUtils.injectObjects(cartController, "itemRepository", itemRepository);

        user = new User();
        user.setUsername("Beholder");
        user.setPassword("1234567");

        cart = new Cart();
        cart.setUser(user);
        user.setCart(cart);

        bd = new BigDecimal(2.99);
        item = new Item();
        item.setId(1L);
        item.setName("Round Widget");
        item.setPrice(bd);
        item.setDescription("A widget that is round");
    }

    @Test
    public void AddToCartLoggedIn() {
        when(itemRepository.findById(item.getId())).thenReturn(java.util.Optional.of(item));

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(user.getUsername());
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setItemId(1);

        when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(user);
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        Cart cartbody = response.getBody();

        assertNotNull(cartbody);
        assertEquals(item.getName(),cartbody.getItems().get(0).getName());
        assertEquals(item.getPrice(), cartbody.getTotal());
        assertEquals(1, cartbody.getItems().size());
    }

    @Test
    public void RemoveFromCartLoggedIn() {

        when(itemRepository.findById(item.getId())).thenReturn(java.util.Optional.of(item));

        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername(user.getUsername());
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setItemId(1);

        when(userRepository.findByUsername(modifyCartRequest.getUsername())).thenReturn(user);
        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1,response.getBody().getItems().size());

        ResponseEntity<Cart> response1 = cartController.removeFromcart(modifyCartRequest);

        Cart cartbody = response1.getBody();

        assertNotNull(cartbody);
        assertEquals(0,cartbody.getItems().size());

    }

    @Test
    public void AddToCartNotLoggedIn() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("Beholder");
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setItemId(0);

        ResponseEntity<Cart> response = cartController.addTocart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void RemoveFromCartNotLoggedIn() {
        ModifyCartRequest modifyCartRequest = new ModifyCartRequest();
        modifyCartRequest.setUsername("Beholder");
        modifyCartRequest.setQuantity(1);
        modifyCartRequest.setItemId(0);

        ResponseEntity<Cart> response = cartController.removeFromcart(modifyCartRequest);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }


}
