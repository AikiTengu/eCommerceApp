package com.example.demo.controllers;

import com.example.demo.TestUtils;
import com.example.demo.model.persistence.Item;
import com.example.demo.model.persistence.repositories.ItemRepository;
import org.junit.Before;
import org.junit.Test;
import org.springframework.http.ResponseEntity;


import java.util.List;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;

public class ItemControllerTest {
    private ItemController itemController;

    private ItemRepository itemRepository = mock(ItemRepository.class);

    @Before
    public void setup() {
        itemController = new ItemController();
        TestUtils.injectObjects(itemController, "itemRepository", itemRepository);
    }

    @Test
    public void getAllItems() {
        ResponseEntity<List<Item>> response = itemController.getItems();
        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());

        List<Item> itemList = response.getBody();
        assertNotNull(itemList);
    }

    @Test
    public void getItemIdNotLoggedIn() {
        ResponseEntity<Item> response = itemController.getItemById(0L);

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }

    @Test
    public void getItemNameNotLoggedIn() {
        ResponseEntity<List<Item>>  response = itemController.getItemsByName("Round Widget");

        assertNotNull(response);
        assertEquals(404, response.getStatusCodeValue());
    }
}