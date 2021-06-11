package com.github.kancyframework.springx.swing.exception;

public class XRuntimeException extends RuntimeException {

    public XRuntimeException(String message) {
        super(message);
    }

    public XRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public XRuntimeException(Throwable cause) {
        super(cause);
    }

}
