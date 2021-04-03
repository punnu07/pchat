package com.example.pchat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;


public class MessageStoreDatabaseAdapter extends SQLiteOpenHelper {


    public static String DATABASE_NAME = "messagestoring_db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "messagestore_table";
    private static final String KEY_ID = "id";
    private static final String KEY_FIRSTNAME = "message";
    private static final String KEY_SECONDNAME = "time";
    private static final String KEY_THIRDNAME = "sender";
    private static final String KEY_FOURTHNAME = "group_name";
    private static final String KEY_FIFTHNAME = "individual_message"; //if individaul message flag is "yes", then message from person

    /* CREATE TABLE students (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, phone_number TEXT); */

    private static final String CREATE_TABLE_COMMAND = "CREATE TABLE "
            + TABLE_NAME + "(" + KEY_FIRSTNAME + " TEXT," + KEY_SECONDNAME + " TEXT," + KEY_THIRDNAME + " TEXT," + KEY_FOURTHNAME + " TEXT," + KEY_FIFTHNAME + " TEXT);";


    public MessageStoreDatabaseAdapter(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("table", CREATE_TABLE_COMMAND);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_COMMAND);
        Log.d("table", "created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_NAME + "'");
        onCreate(db);
    }

    public long addMessage(String message, String time, String sender, String groupname, String individual_message) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Creating content values
        ContentValues values = new ContentValues();
        values.put(KEY_FIRSTNAME, message);
        values.put(KEY_SECONDNAME, time);
        values.put(KEY_THIRDNAME, sender);
        values.put(KEY_FOURTHNAME, groupname);
        values.put(KEY_FIFTHNAME, individual_message);


        // insert row in students table
        long insert = db.insert(TABLE_NAME, null, values);
        db.close();
        return insert;
    }


    public void truncatetable() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Creating content values
        String truncate_command = "delete from " + TABLE_NAME + ";";
        Log.d("truncate", truncate_command);
        db.execSQL(truncate_command);

        String truncate_command2 = "vacuum;";
        Log.d("truncate2", truncate_command2);
        db.execSQL(truncate_command2);
        db.close();

    }


    public String getCurrentMessage() {

        String currentMessage = "";
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {
            currentMessage = c.getString(c.getColumnIndex(KEY_FIRSTNAME));
            Log.d("array", currentMessage);
        }
        db.close();
        return currentMessage;
    }


    public ArrayList<String> getAllMessage() {
        ArrayList<String> messageArrayList = new ArrayList<String>();
        String name = "";
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {

            do {
                name = c.getString(c.getColumnIndex(KEY_FIRSTNAME));
                messageArrayList.add(name);
            } while (c.moveToNext());

            Log.d("array", messageArrayList.toString());
        }
        db.close();
        return messageArrayList;
    }





    public ArrayList<String> getAllTime() {
        ArrayList<String> timeArrayList = new ArrayList<String>();
        String time = "";
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {

            do {
                time = c.getString(c.getColumnIndex(KEY_SECONDNAME));
                timeArrayList.add(time);
            } while (c.moveToNext());

            Log.d("array", timeArrayList.toString());
        }
        db.close();
        return timeArrayList;
    }


    public ArrayList<String> getAllSender() {
        ArrayList<String> senderArrayList = new ArrayList<String>();
        String sender = "";
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {

            do {
                sender = c.getString(c.getColumnIndex(KEY_THIRDNAME));
                senderArrayList.add(sender);
            } while (c.moveToNext());

            Log.d("array", senderArrayList.toString());
        }
        db.close();
        return senderArrayList;
    }


    public ArrayList<String> getAllGroupname() {
        ArrayList<String> groupnameArrayList = new ArrayList<String>();
        String groupname = "";
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {

            do {
                groupname = c.getString(c.getColumnIndex(KEY_FOURTHNAME));
                groupnameArrayList.add(groupname);
            } while (c.moveToNext());

            Log.d("array", groupnameArrayList.toString());
        }
        db.close();
        return groupnameArrayList;
    }


    public ArrayList<String> getAllIndividualMessage() {
        ArrayList<String> individualmessageArrayList = new ArrayList<String>();
        String individualmessage = "";
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {

            do {
                individualmessage = c.getString(c.getColumnIndex(KEY_FIFTHNAME));
                individualmessageArrayList.add(individualmessage);
            } while (c.moveToNext());

            Log.d("array", individualmessageArrayList.toString());
        }
        db.close();
        return individualmessageArrayList;
    }

}