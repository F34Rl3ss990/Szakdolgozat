package com.EGEA1R.CarService.service.interfaces;

import com.EGEA1R.CarService.persistance.entity.TokenBlock;

import java.util.Optional;

public interface JwtTokenCheckService {

    Optional<TokenBlock> findTokenBlock(String userId);
}
