package com.pee.tokopee;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Array;
import java.nio.file.Files;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "TokopeeDB";
    // Column Table User
    private static final String TABLE_NAME_USER = "User";
    private static final String KEY_USER_ID = "id";
    private static final String KEY_USER_EMAIL = "email";
    private static final String KEY_USER_USERNAME = "username";
    private static final String KEY_USER_PASSWORD = "password";
    // Column Table Item
    private static final String TABLE_NAME_ITEM = "Item";
    private static final String KEY_ITEM_ID = "id";
    private static final String KEY_ITEM_IMAGE_URL = "imageUrl";
    private static final String KEY_ITEM_NAME = "name";
    private static final String KEY_ITEM_SIZE = "size";
    private static final String KEY_ITEM_PRICE = "price";
    // Column Table History
    private static final String TABLE_NAME_HISTORY = "History";
    private static final String KEY_HISTORY_ID = "id";
    private static final String KEY_HISTORY_ID_USER = "userId";
    private static final String KEY_HISTORY_ID_ITEM = "itemId";
    private static final String KEY_HISTORY_DATE = "date";

    public DatabaseHandler(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TABLE_USER = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_USER + "(" +
                KEY_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                KEY_USER_EMAIL + " TEXT NOT NULL," +
                KEY_USER_USERNAME + " TEXT NOT NULL," +
                KEY_USER_PASSWORD + " TEXT NOT NULL)";
        String CREATE_TABLE_ITEM = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_ITEM + "(" +
                KEY_ITEM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                KEY_ITEM_IMAGE_URL + " TEXT NOT NULL," +
                KEY_ITEM_NAME + " TEXT NOT NULL," +
                KEY_ITEM_PRICE + " INTEGER NOT NULL," +
                KEY_ITEM_SIZE + " TEXT NOT NULL)";
        String CREATE_TABLE_HISTORY = "CREATE TABLE IF NOT EXISTS " + TABLE_NAME_HISTORY + "(" +
                KEY_HISTORY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL," +
                KEY_HISTORY_ID_USER + " INTEGER NOT NULL," +
                KEY_HISTORY_ID_ITEM + " INTEGER NOT NULL," +
                KEY_HISTORY_DATE + " TEXT NOT NULL)";
        db.execSQL(CREATE_TABLE_HISTORY);
        db.execSQL(CREATE_TABLE_USER);
        db.execSQL(CREATE_TABLE_ITEM);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_HISTORY);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_ITEM);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME_USER);
        onCreate(db);
    }

    // INSERT OPS
    public long insertUser(User user){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_USER_USERNAME, user.getUsername());
        values.put(KEY_USER_EMAIL, user.getEmail());
        values.put(KEY_USER_PASSWORD, user.getPassword());

        if(db.insert(TABLE_NAME_USER, null, values) == -1){
            db.close();
            return -1;
        }
        db.close();
        return 1;
    }
    public long insertItem(Item item){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_ITEM_IMAGE_URL, item.getImageUrl());
        values.put(KEY_ITEM_NAME, item.getName());
        values.put(KEY_ITEM_SIZE, item.getSize());
        values.put(KEY_ITEM_PRICE, item.getPrice());

        if(db.insert(TABLE_NAME_ITEM, null, values) == -1){
            db.close();
            return -1;
        }
        db.close();
        return 1;
    }
    public long insertHistory(History history){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_HISTORY_ID_ITEM, history.getItem().getId());
        values.put(KEY_HISTORY_ID_USER, history.getUserId());
        values.put(KEY_HISTORY_DATE, history.getDateTime().toString());

        if(db.insert(TABLE_NAME_HISTORY, null, values) == -1){
            db.close();
            return -1;
        }
        db.close();
        return 1;
    }
    // SELECT OPS
    public User searchUser(String email, String password) {
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME_USER + " WHERE " + KEY_USER_EMAIL + "=? AND " + KEY_USER_PASSWORD + "=?";

        User user = null;
        Cursor cursor = db.rawQuery(query, new String[]{email, password});
        if (cursor.moveToFirst()) {
            user = new User(
                    cursor.getInt(0),
                    cursor.getString(2),
                    cursor.getString(1),
                    cursor.getString(3)
            );
        }
        cursor.close();
        return user;
    }
    public User searchUserByEmail(String email){
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME_USER + " WHERE " + KEY_USER_EMAIL + "=?";
        Cursor cursor = db.rawQuery(query, new String[]{email});
        User user = null;
        if(cursor.moveToFirst()){
            user = new User(
                    cursor.getInt(0),
                    cursor.getString(2),
                    cursor.getString(1),
                    cursor.getString(3)
            );
        }
        return user;
    }
    public ArrayList<History> getHistory(Integer userId){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<History> arrHistory = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_NAME_HISTORY + " WHERE " + KEY_HISTORY_ID_USER + "=?" + " ORDER BY " + KEY_HISTORY_ID + " DESC";
        Cursor cursor = db.rawQuery(query, new String[]{userId.toString()});
        if(cursor.moveToFirst()){
            arrHistory.add(new History(
                    cursor.getInt(0),
                    cursor.getInt(1),
                    searchItem(cursor.getInt(2)),
                    formatDateTime(cursor.getString(3))
            ));
            while(cursor.moveToNext()){
                arrHistory.add(new History(
                        cursor.getInt(0),
                        cursor.getInt(1),
                        searchItem(cursor.getInt(2)),
                        formatDateTime(cursor.getString(3))
                ));
            }
        }
        return arrHistory;
    }
    public Item searchItem(Integer id){
        SQLiteDatabase db = getReadableDatabase();

        // String query = "SELECT * FROM " + TABLE_NAME_ITEM + " WHERE " + KEY_ITEM_ID + "=?";
        String query = "SELECT * FROM " + TABLE_NAME_ITEM + " WHERE " + KEY_ITEM_ID + "=" + id;
        // Cursor cursor = db.rawQuery(query, new String[]{id.toString()});
        Cursor cursor = db.rawQuery(query, null);
        Item item = null;
        if(cursor.moveToFirst()){
            item = new Item(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getString(4)
            );
        }
        return item;
    }
    public ArrayList<Item> getItems(){
        ArrayList<Item> arrItem = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();

        String query = "SELECT * FROM " + TABLE_NAME_ITEM;
        Cursor cursor = db.rawQuery(query, null);
        // Integer id-0, String imageUrl-1, String name-2, String size-4, Integer price-3
        if(cursor.moveToFirst()){
            arrItem.add(new Item(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getString(4)
            ));
            while(cursor.moveToNext()){
                arrItem.add(new Item(
                        cursor.getInt(0),
                        cursor.getString(1),
                        cursor.getString(2),
                        cursor.getInt(3),
                        cursor.getString(4)
                ));
            }
        }
        return arrItem;
    }
    // DELETE OPS
    public boolean deleteUser(Integer userId){
        SQLiteDatabase db = getReadableDatabase();
        // Delete history data
        db.delete(TABLE_NAME_HISTORY, KEY_HISTORY_ID_USER + "=?", new String[]{userId.toString()});
        return db.delete(TABLE_NAME_USER, KEY_USER_ID + "=?", new String[]{userId.toString()}) > 0;
    }
    // UPDATE OPS
    public boolean updatePassword(Integer userId, String newPassword){
        SQLiteDatabase db = getReadableDatabase();
        ContentValues values = new ContentValues();
        values.put("password", newPassword);
        return db.update(TABLE_NAME_USER, values, KEY_USER_ID + "=?", new String[]{userId.toString()}) > 0;
    }
    // OTHER OPS
    public LocalDateTime formatDateTime(String dateTime){
        DateTimeFormatter formatter = DateTimeFormatter.ISO_LOCAL_DATE_TIME;
        LocalDateTime formattedDateTime = LocalDateTime.parse(dateTime, formatter);
        return formattedDateTime;
    }
}
