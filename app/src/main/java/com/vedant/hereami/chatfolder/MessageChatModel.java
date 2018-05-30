package com.vedant.hereami.chatfolder;

/**
 * Created by Marcel on 11/7/2015.
 */
public class MessageChatModel {

    private String message;
    private String recipient;
    private String sender;
    private String timestamp;
    private String devicetoken;

    private int mRecipientOrSenderStatus;
    private String message1;
    private String messagetype1;
    private String imageurl1;


    /* Setter */

    public void setMessage(String message) {
        this.message = message;
    }

    public void setMessage1(String message1) {
        this.message1 = message1;
    }

    public void setmessagetype(String messagetype) {
        this.messagetype1 = messagetype;
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

    public void setDevicetoken(String givenDevicetoken) {
        devicetoken = givenDevicetoken;
    }

    public void setimageurl(String imageurl) {
        this.imageurl1 = imageurl;
    }
    /* Getter */

    public String getImageurl1() {
        return imageurl1;
    }

    public String getMessagetype1() {
        return messagetype1;
    }

    public String getMessage() {
        return message;
    }

    public String getMessage1() {
        return message1;
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

    public String getDevicetoken() {
        return devicetoken;
    }


    public int getRecipientOrSenderStatus() {
        return mRecipientOrSenderStatus;
    }
}
