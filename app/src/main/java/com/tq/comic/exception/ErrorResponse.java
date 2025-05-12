package com.tq.comic.exception;

public class ErrorResponse {
    private int code;
    private String message;

    // Getter + Setter
    public int getCode() { return code; }
    public void setCode(int code) { this.code = code; }

    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
