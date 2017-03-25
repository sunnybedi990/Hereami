package com.vedant.hereami.firebasepushnotification;

/**
 * Created by bedi on 3/25/2017.
 */

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class JSONParser {

    public static String[] ids;
    public static String[] names;
    public static String[] emails;

    public static final String JSON_ARRAY = "devices";
    public static final String KEY_ID = "id";
    public static final String KEY_NAME = "token";
    public static final String KEY_EMAIL = "email";

    private JSONArray users = null;

    private String json;

    public JSONParser(String json) {
        this.json = json;
    }

    protected void parseJSON() {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(json);
            users = jsonObject.getJSONArray(JSON_ARRAY);

            ids = new String[users.length()];
            names = new String[users.length()];
            emails = new String[users.length()];

            for (int i = 0; i < users.length(); i++) {
                JSONObject jo = users.getJSONObject(i);
                ids[i] = jo.getString(KEY_ID);
                emails[i] = jo.getString(KEY_EMAIL);
                names[i] = jo.getString(KEY_NAME);
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}