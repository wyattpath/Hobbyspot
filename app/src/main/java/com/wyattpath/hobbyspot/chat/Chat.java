package com.wyattpath.hobbyspot.chat;

public class Chat {
    private String message;
    private Boolean isCurrentUser;

    public Chat(String message, Boolean isCurrentUser) {
        this.message = message;
        this.isCurrentUser = isCurrentUser;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Boolean getCurrentUser() {
        return isCurrentUser;
    }

    public void setCurrentUser(Boolean currentUser) {
        isCurrentUser = currentUser;
    }
}
