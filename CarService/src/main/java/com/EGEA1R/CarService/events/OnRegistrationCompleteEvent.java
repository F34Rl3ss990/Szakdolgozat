package com.EGEA1R.CarService.events;

import com.EGEA1R.CarService.entity.Credential;
import lombok.*;
import org.springframework.context.ApplicationEvent;


public class OnRegistrationCompleteEvent extends ApplicationEvent {
    @Getter
    @Setter
    private String appUrl;
    @Getter
    @Setter
    private Credential credential;

    public OnRegistrationCompleteEvent(
            Credential credential, String appUrl) {
        super(credential);

        this.credential = credential;
        this.appUrl = appUrl;
    }
}
