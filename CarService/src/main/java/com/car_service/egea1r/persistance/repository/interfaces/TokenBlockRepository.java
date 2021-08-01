package com.car_service.egea1r.persistance.repository.interfaces;

import com.car_service.egea1r.persistance.entity.TokenBlock;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TokenBlockRepository extends CrudRepository<TokenBlock, Long> {

    List<TokenBlock> findByUserId(long userId);
}
