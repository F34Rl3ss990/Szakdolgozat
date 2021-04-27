package com.EGEA1R.CarService.persistance.repository.interfaces;

import com.EGEA1R.CarService.persistance.entity.TokenBlock;
import org.springframework.data.repository.CrudRepository;

import java.util.List;
import java.util.Optional;

public interface TokenBlockRepository extends CrudRepository<TokenBlock, Long> {

    List<TokenBlock> findByUserId(Long userId);
}
