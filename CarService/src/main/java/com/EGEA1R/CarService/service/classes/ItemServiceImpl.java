package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.persistance.repository.ItemRepository;
import com.EGEA1R.CarService.service.interfaces.ItemService;
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
