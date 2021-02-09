package com.EGEA1R.CarService.web.controller;

import com.EGEA1R.CarService.persistance.entity.TokenBlock;
import com.EGEA1R.CarService.service.interfaces.JwtTokenCheckService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

    private JwtTokenCheckService jwtTokenCheckService;

    @Autowired
    public void setJwtTokenCheckService(JwtTokenCheckService jwtTokenCheckService){
        this.jwtTokenCheckService = jwtTokenCheckService;
    }

    @PostMapping("/logout")
    public String logout(HttpServletRequest request){
        jwtTokenCheckService.saveBlockedToken(request);

        return "logged out";
    }


    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }
}