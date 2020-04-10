package com.devapp.usbhostmanager.service;

public class AdsServiceSetupException extends RuntimeException {
    public AdsServiceSetupException() {
    }

    public AdsServiceSetupException(String message) {
        super(message);
    }

    public AdsServiceSetupException(String message, Throwable cause) {
        super(message, cause);
    }

    public AdsServiceSetupException(Throwable cause) {
        super(cause);
    }
}
