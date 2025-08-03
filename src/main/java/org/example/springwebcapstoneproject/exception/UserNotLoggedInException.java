package org.example.springwebcapstoneproject.exception;

public class UserNotLoggedInException extends RuntimeException {
    public UserNotLoggedInException() {
        super("User not logged in");
    }
}
