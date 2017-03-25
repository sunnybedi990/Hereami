package com.vedant.hereami.chatfolder;

/**
 * Created by sunnybedi on 16/03/17.
 */

public class usermodel {

    private String connection;
    private String timestamp;





    /* Setter */


    public void setConnection(String givenconnection) {
        connection = givenconnection;
    }

    public void setTimeStamp(String givenTimestamp) {
        timestamp = givenTimestamp;
    }


    /* Getter */


    public String getConnection() {
        return connection;
    }

    public String getTimestamp() {
        return timestamp;
    }

}
