package com.EGEA1R.CarService.service.interfaces;

import com.EGEA1R.CarService.persistance.entity.TokenBlock;

import java.util.List;
import java.util.Optional;

public interface JwtTokenCheckService {

    List<TokenBlock> findTokenBlock(String userId);
}
