package com.vedant.hereami.chatfolder;

/**
 * Created by Marcel on 11/7/2015.
 */
public class MessageChatModel {

    private String message;
    private String recipient;
    private String sender;
    private String timestamp;

    private int mRecipientOrSenderStatus;


    /* Setter */

    public void setMessage(String message) {
        this.message = message;
    }


    public void setRecipientOrSenderStatus(int recipientOrSenderStatus) {
        this.mRecipientOrSenderStatus = recipientOrSenderStatus;
    }


    public void setRecipient(String givenRecipient) {
        recipient = givenRecipient;
    }


    public void setSender(String givenSender) {
        sender = givenSender;
    }

    public void setTimeStamp(String givenTimestamp) {
        timestamp = givenTimestamp;
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

    public String getTimestamp() {
        return timestamp;
    }


    public int getRecipientOrSenderStatus() {
        return mRecipientOrSenderStatus;
    }
}
