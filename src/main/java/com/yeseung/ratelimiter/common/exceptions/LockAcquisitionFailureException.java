package com.yeseung.ratelimiter.common.exceptions;

public class LockAcquisitionFailureException extends RuntimeException {

    public LockAcquisitionFailureException(String message) {
        super(message);
    }

}
