package com.revolut.demo.exception;

import com.revolut.demo.constant.RevolutResponseCode;

/*
 * Thrown by the service layer when a business conflict occur. This exception may be thrown as part
 * of an API call .
 */
public class RevolutBusinessException extends RuntimeException {

    private RevolutResponseCode responseCode;

    public RevolutBusinessException(RevolutResponseCode responseCode) {
        super(responseCode.toString());
        this.responseCode = responseCode;
    }

    public RevolutResponseCode getResponseCode() {
        return responseCode;
    }

}
