package com.car_service.egea1r.service.interfaces;

import com.car_service.egea1r.persistance.entity.TokenBlock;

import java.util.List;

public interface JwtTokenCheckService {

    List<TokenBlock> findTokenBlock(String userId);
}
