package com.vedant.hereami.database;

/**
 * Created by bedi on 5/7/2016.
 */

import android.annotation.TargetApi;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.util.Log;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "MyDBName.db";
    public static final String CONTACTS_TABLE_NAME = "recentchat";
    public static final String CONTACTS_COLUMN_ID = "id";
    public static final String CONTACTS_COLUMN_NAME = "name";
    public static final String CONTACTS_COLUMN_MSG = "msg";
    public static final String CONTACTS_COLUMN_TIMESTAMP = "timestamp";
    public static final String CONTACTS_COLUMN_USERNAME = "username";
    public static final String CONTACTS_COLUMN_KEYPOSITION3 = "keyposition3";
    public static final String CONTACTS_COLUMN_PHONE = "phone";
    private Context myContext;
    private HashMap hp;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO Auto-generated method stub
        db.execSQL(
                "create table recentchat " +
                        "(id  INTEGER PRIMARY KEY AUTOINCREMENT, name text, username text, phone long, msg text, timestamp text)"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO Auto-generated method stub
        db.execSQL("DROP TABLE IF EXISTS recentchat");
        onCreate(db);

    }

    @Override
    @TargetApi(Build.VERSION_CODES.FROYO)
    public void onDowngrade(final SQLiteDatabase db, final int oldVersion, final int newVersion) {
        // Debug.log_d(DebugConfig.DEFAULT, LOG_TAG, "onDowngrade(): oldVersion = " + oldVersion + " : newVersion = " + newVersion);
        super.onDowngrade(db, oldVersion, newVersion);
    }

    public boolean insertContact(String name, String username, String phone, String msg, String time) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put("id", phone);
        contentValues.put("name", name);
        contentValues.put("username", username);
        contentValues.put("phone", phone);
        contentValues.put("msg", msg);
        contentValues.put("timestamp", time);
        db.insert("recentchat", null, contentValues);
        return true;
    }

    public Cursor getData(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from contacts where id=" + id + "", null);
        return res;
    }

    public int numberOfRows() {
        SQLiteDatabase db = this.getReadableDatabase();
        int numRows = (int) DatabaseUtils.queryNumEntries(db, CONTACTS_TABLE_NAME);
        return numRows;
    }

    public Cursor name(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor p1 = db.rawQuery("select * from contacts where id=" + name + "", null);
        return p1;
    }


    public boolean updateContact(String name, String username, String phone, String msg, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", name);
        contentValues.put("username", username);
        contentValues.put("phone", phone);
        contentValues.put("msg", msg);
        contentValues.put("timestamp", time);
        db.update("recentchat", contentValues, "id = ? ", null);
        return true;
    }

    public boolean updatemsgs(String id, String msg, String time) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("msg", msg);
        contentValues.put("timestamp", time);
        db.update("recentchat", contentValues, "id = ? ", new String[]{id});

        return true;
    }

    public Integer deleteContact(Integer id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete("contacts",
                "id = ? ",
                new String[]{Integer.toString(id)});
    }

    public ArrayList<String> getAllCotacts() {
        ArrayList<String> array_list = new ArrayList<String>();


        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from recentchat", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_NAME)));


            res.moveToNext();
        }
        res.close();
        return array_list;
    }

    public ArrayList<String> getAllmsgs() {
        ArrayList<String> array_list1 = new ArrayList<String>();


        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from recentchat", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list1.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_MSG)));
            res.moveToNext();
        }
        res.close();
        return array_list1;
    }

    public ArrayList<String> getAlltimestamp() {
        ArrayList<String> array_list2 = new ArrayList<String>();


        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from recentchat", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list2.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_TIMESTAMP)));
            res.moveToNext();
        }
        res.close();
        return array_list2;
    }

    public ArrayList<String> getAllkey() {
        ArrayList<String> array_list2 = new ArrayList<String>();


        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from recentchat", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list2.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_USERNAME)));
            res.moveToNext();
        }
        res.close();
        return array_list2;
    }

    public ArrayList<String> getAllphone() {
        ArrayList<String> array_list2 = new ArrayList<String>();


        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from recentchat", null);
        res.moveToFirst();

        while (res.isAfterLast() == false) {
            array_list2.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_PHONE)));
            res.moveToNext();
        }
        res.close();
        return array_list2;
    }

    public ArrayList<String> getAllid() {
        ArrayList<String> array_list3 = new ArrayList<String>();


        //hp = new HashMap();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select * from recentchat", null);
        res.moveToFirst();

        while (!res.isAfterLast()) {
            array_list3.add(res.getString(res.getColumnIndex(CONTACTS_COLUMN_ID)));
            res.moveToNext();
        }
        res.close();
        return array_list3;
    }

    public boolean gettabes() {

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("select id from recentchat", null);

        return true;
    }

    public void deletedatabse() {

        SQLiteDatabase db = this.getWritableDatabase();
        Log.e("datapath", db.getPath());
        SQLiteDatabase.deleteDatabase((new File(db.getPath())));
        // File datapath = myContext.getDatabasePath("message.db");
        //  myContext.deleteDatabase(db.getPath());


    }
}
