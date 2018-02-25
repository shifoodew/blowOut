package com.blowout.blowout.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.HashMap;

import static android.content.ContentValues.TAG;

/**
 * Created by shifoodew on 2/25/2018.
 */

public class SQLiteHandler extends SQLiteOpenHelper{

    // Database Name
    private static final String DATABASE_NAME = "blowout";

    // Table name
    private static final String user_table = "user";

    // user Table Column name
    private static final String u_id    = "id";
    private static final String u_name  = "name";
    private static final String u_email = "email";

    // user table create statement
    private static final String CREATE_MEMBERS_TABLE = "CREATE TABLE "  + user_table + "("
            + u_id + " TEXT,"
            + u_name + " TEXT,"
            + u_email + " TEXT" + ")";


    public SQLiteHandler(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_MEMBERS_TABLE);
        Log.d(TAG, "USER table successfully creaated");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + user_table);
        Log.d(TAG, "USER table successfully droped");

        onCreate(db);
    }
    /**
     * Storing user details in database
     * */
    public void addUser(String user_id, String user_name, String user_email) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(u_id,     user_id); // id
        values.put(u_name,   user_name); // name
        values.put(u_email,  user_email); // email At

        // Inserting Row
        long id = db.insertOrThrow(user_table, null, values);

        db.close(); // Closing database connection

        Log.d(TAG, "Member's info was inserted successfully inserted: " + id);
        Log.d(TAG, "Successfully inserted user_id:       "      + user_id);
        Log.d(TAG, "Successfully inserted user_name:     "      + user_name);
        Log.d(TAG, "Successfully inserted user_email at: "      + user_email);
        Log.d(TAG, "--------------------------------------------------");
    }

    public HashMap<String, String> getUserDetails() {
        HashMap<String, String> user = new HashMap<String, String>();
        String selectQuery = "SELECT * FROM " + user_table;

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // Move to first row
        cursor.moveToFirst();
        if (cursor.getCount() > 0) {
            user.put("u_id", cursor.getString(0));
            user.put("u_name", cursor.getString(1));
            user.put("u_email", cursor.getString(2));

            Log.d(TAG, "user's data: " + user.toString());
        }
        else{
            Log.d(TAG, "user's data is empty");
        }
        cursor.close();
        db.close();
        // return user
        Log.d(TAG, "Member's info was successfully fetch: " + user.toString());

        return user;
    }
    /**
     * Re create database Delete all tables and create them again
     * */
    public void deleteUserData() {
        SQLiteDatabase db = this.getWritableDatabase();
        // Delete All Rows
        db.delete(user_table, null, null);
        db.close();

        Log.d(TAG, "user data was deleted");
    }
}
