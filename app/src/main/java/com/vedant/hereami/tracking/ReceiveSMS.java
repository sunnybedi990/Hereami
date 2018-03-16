package com.vedant.hereami.tracking;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.UriMatcher;
import android.database.Cursor;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.EditText;

/*
 * Created by bedi on 3/6/2016.
 */
public class ReceiveSMS extends BroadcastReceiver {

    //   ArrayList<String> mylist = new ArrayList<String>();
    private final static String STORETEXT = "storetext.txt";
    public static final String CONTACTS_COLUMN_PHONE = "phone";
    Boolean SendSMS;
    public String Mobileno;
    public String VarMessageBody;
    public String senderNum;
    private ReceiveSMS mBase;
    private EditText editText1;
    private String name;
    public SharedPreferences sharedpreferences;
    private String MyPREFERENCES;
    //  private DBHelper mydb;
    int id_To_Update = 0;
    Context context1;
    private UriMatcher sUriMatcher;
    private Cursor cursor;
    public static final String mypreference = "mypref";
    public static final String Name = "nameKey";
    public static final String Email = "emailKey";
    public static final String Email1 = "emailKey1";
    public static final String Email2 = "emailKey2";
    public static final String Email3 = "emailKey3";
    public String message;


    //   private EditText first;
    //   private EditText second;

    @Override
    public void onReceive(Context context, Intent intent) {


        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs = null;
        String str = "";
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            msgs = new SmsMessage[pdus.length];
            int i;
            for (i = 0; i < msgs.length; i++) {
                msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                str += "SMS from " + msgs[i].getOriginatingAddress();
                Mobileno = msgs[i].getOriginatingAddress();
                str += " :";
                str += msgs[i].getMessageBody();
                VarMessageBody = msgs[i].getMessageBody().toLowerCase().replace(" ", "");
                Mobileno = msgs[i].getOriginatingAddress();
                senderNum = Mobileno.replace(" ", "");
                message = VarMessageBody;
            }


            if (VarMessageBody.equals("tracker")) {
                Intent intentHome = new Intent(context, Sendlocation.class);
                intentHome.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(intentHome);

            }
        }
    }
}
