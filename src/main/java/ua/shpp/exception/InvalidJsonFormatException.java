package ua.shpp.exception;

import com.fasterxml.jackson.core.JsonProcessingException;

public class InvalidJsonFormatException extends RuntimeException {
    public InvalidJsonFormatException(String message, JsonProcessingException e) {
    }
}