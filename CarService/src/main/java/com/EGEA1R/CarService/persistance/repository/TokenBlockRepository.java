package com.EGEA1R.CarService.persistance.repository;

import com.EGEA1R.CarService.persistance.entity.TokenBlock;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface TokenBlockRepository extends CrudRepository<TokenBlock, Long> {

    Optional<TokenBlock> findByUserId(Long userId);
}
