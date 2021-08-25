package com.car_service.egea1r.service.classes;

import com.car_service.egea1r.service.interfaces.TotpManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TotpManagerImplTest {

    @InjectMocks
    TotpManager totpManager;

    @BeforeEach
    void setUp() {
    }

    @Test
    void generateSecret() {

    }

    @Test
    void getUriForImage() {
    }

    @Test
    void verifyCode() {
    }
}
