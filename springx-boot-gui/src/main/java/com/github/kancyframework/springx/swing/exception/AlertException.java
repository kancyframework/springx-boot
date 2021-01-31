package com.github.kancyframework.springx.swing.exception;

import com.github.kancyframework.springx.utils.StringUtils;

public class AlertException extends RuntimeException {
    private String friendlyMessage;

    public AlertException(String message) {
        super(message);
        setFriendlyMessage(message);
    }

    public AlertException(String message, Throwable cause) {
        super(message, cause);
        setFriendlyMessage(message);
    }

    public AlertException(Throwable cause) {
        super(cause);
        setFriendlyMessage(String.format("\u672a\u77e5\u5f02\u5e38\uff1a%s", StringUtils.left(cause.getMessage(), 50)));
}

    public String getFriendlyMessage() {
        return friendlyMessage;
    }

    public void setFriendlyMessage(String friendlyMessage) {
        this.friendlyMessage = friendlyMessage;
    }

}
