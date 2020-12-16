package com.EGEA1R.CarService.controller;

import com.EGEA1R.CarService.service.ItemService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ItemController {

    private ItemService itemsService;

    @Autowired
    public void setItemsService(ItemService itemsService){
        this.itemsService = itemsService;
    }
}
