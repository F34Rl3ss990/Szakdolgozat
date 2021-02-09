package com.EGEA1R.CarService.service.interfaces;

import com.EGEA1R.CarService.persistance.entity.TokenBlock;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

public interface JwtTokenCheckService {

    void saveBlockedToken(HttpServletRequest request);

    Optional<TokenBlock> findTokenBlock(String userId);
}
