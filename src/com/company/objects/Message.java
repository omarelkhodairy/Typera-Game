package com.company.objects;

import java.io.Serializable;

public class Message implements Serializable {
    private long serialVersionUID;
    private String sender;
    private String message;

    public Message() {
    }

    public Message(String sender, String message, long l) {
        this.sender = sender;
        this.message = message;
        this.serialVersionUID = l;
    }




    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public long getSerialVersionUID() {
        return serialVersionUID;
    }

    public void setSerialVersionUID(long serialVersionUID) {
        this.serialVersionUID = serialVersionUID;
    }
}
