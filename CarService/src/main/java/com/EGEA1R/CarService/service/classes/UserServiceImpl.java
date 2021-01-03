package com.EGEA1R.CarService.service.classes;

import com.EGEA1R.CarService.persistance.repository.UserRepository;
import com.EGEA1R.CarService.service.interfaces.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public void setUserRepository(UserRepository userRepository){
        this.userRepository = userRepository;
    }
}
