package com.larramendiProject.RegisterLoginSystem.model.response;

public class UpdateResponse {
    String message;
    boolean status;

    public UpdateResponse() {
    }

    public UpdateResponse(String message, boolean status) {
        this.message = message;
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "UpdateMessage{" +
                "message='" + message + '\'' +
                ", status=" + status +
                '}';
    }
}
