package com.example.pchat;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class GroupInfoDatabaseAdapter  extends SQLiteOpenHelper {


    public static String DATABASE_NAME = "groupinfostoring_db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "groupinfo_table";
    private static final String KEY_ID = "id";
    private static final String KEY_FIRSTNAME = "groupname";


    /* CREATE TABLE students (id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, phone_number TEXT); */

    private static final String CREATE_TABLE_COMMAND = "CREATE TABLE "
            + TABLE_NAME + "("+KEY_FIRSTNAME+" TEXT);";


    public GroupInfoDatabaseAdapter(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Log.d("table", CREATE_TABLE_COMMAND);
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_COMMAND);
        Log.d("table","created");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS '" + TABLE_NAME+ "'");
        onCreate(db);
    }

    public long addGroup(String  groupname ) {
        SQLiteDatabase db = this.getWritableDatabase();
        // Creating content values
        ContentValues values = new ContentValues();
        values.put(KEY_FIRSTNAME, groupname);

        // insert row in students table
        long insert = db.insert(TABLE_NAME, null, values);
        db.close();
        return insert;
    }


    public void truncatetable() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Creating content values
        String truncate_command="delete from " +TABLE_NAME+";";
        Log.d("truncate", truncate_command);
        db.execSQL(truncate_command);

        String truncate_command2="vacuum;";
        Log.d("truncate2", truncate_command2);
        db.execSQL(truncate_command2);
        db.close();

    }








    public ArrayList<String> getAllGroups() {
        ArrayList<String> GroupArrayList = new ArrayList<String>();
        String name="";
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (c.moveToFirst()) {

            do {
                name = c.getString(c.getColumnIndex(KEY_FIRSTNAME));
                GroupArrayList.add(name);
            } while (c.moveToNext());

            Log.d("array", GroupArrayList.toString());
        }
        db.close();
        return GroupArrayList;
    }








}
