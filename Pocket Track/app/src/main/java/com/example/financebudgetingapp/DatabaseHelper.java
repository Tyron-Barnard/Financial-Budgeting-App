package com.example.financebudgetingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    public static final String databaseName = "signup.db";

    public DatabaseHelper(@Nullable Context context) {
        super(context, "signup.db", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase myDatabase) {
        myDatabase.execSQL("CREATE TABLE users(email TEXT PRIMARY KEY, password TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase myDatabase, int i, int i1) {
        myDatabase.execSQL("DROP TABLE IF EXISTS users");
    }

    public Boolean insert(String email, String password) {
        SQLiteDatabase myDatabase = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("email", email);
        contentValues.put("password", password);
        long result = myDatabase.insert("users", null, contentValues);

        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Boolean checkEmail(String email) {
        SQLiteDatabase myDatabase = this.getWritableDatabase();
        Cursor cursor = myDatabase.rawQuery("SELECT * FROM users WHERE email = ?", new String[]{email});

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }

    public boolean checkEmailPassword(String email, String password) {
        SQLiteDatabase myDatabase = this.getWritableDatabase();
        Cursor cursor = myDatabase.rawQuery("SELECT * FROM users WHERE email = ? AND password = ?", new String[]{email, password});

        if (cursor.getCount() > 0) {
            return true;
        } else {
            return false;
        }
    }
}
