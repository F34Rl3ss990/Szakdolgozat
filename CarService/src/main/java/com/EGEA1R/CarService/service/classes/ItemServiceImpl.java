package com.EGEA1R.CarService.service;

import com.EGEA1R.CarService.repository.ItemRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ItemServiceImpl implements ItemService {

    private ItemRepository itemsRepository;

    @Autowired
    public void setItemsRepository(ItemRepository itemsRepository){
        this.itemsRepository = itemsRepository;
    }
}
