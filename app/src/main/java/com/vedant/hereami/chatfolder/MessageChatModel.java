package com.vedant.hereami.chatfolder;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by Marcel on 11/7/2015.
 */
public class MessageChatModel {

    private String message;
    private String recipient;
    private String sender;

    private int mRecipientOrSenderStatus;


    /* Setter */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public void setMessage(String message) {
        this.message = message;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public void setRecipientOrSenderStatus(int recipientOrSenderStatus) {
        this.mRecipientOrSenderStatus = recipientOrSenderStatus;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public void setRecipient(String givenRecipient) {
        recipient = givenRecipient;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public void setSender(String givenSender) {
        sender = givenSender;
    }


    /* Getter */

    public String getMessage() {
        return message;
    }

    public String getRecipient() {
        return recipient;
    }

    public String getSender() {
        return sender;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public int getRecipientOrSenderStatus() {
        return mRecipientOrSenderStatus;
    }
}
