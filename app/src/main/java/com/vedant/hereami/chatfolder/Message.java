package com.vedant.hereami.chatfolder;

/**
 * Created by sunnybedi on 02/03/17.
 */

public class Message {
    private String message, sender, recipient;


    public Message() {
    }

    public String setMessage(String message) {
        this.message = message;
        return this.message;
    }

    public String setRecipient(String givenRecipient) {
        this.recipient = givenRecipient;
        return recipient;
    }

    public String setSender(String givenSender) {
        this.sender = givenSender;
        return sender;
    }

    public String getMessage() {
        return message;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getSender() {
        return sender;
    }


}
