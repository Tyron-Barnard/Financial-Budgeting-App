package com.example.financebudgetingapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;

public class DatabaseController extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "my_database.db";
    private static final int DATABASE_VERSION = 1;

    SQLiteDatabase db = this.getWritableDatabase();

    public DatabaseController(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL("CREATE TABLE IF NOT EXISTS Finances (financeId INTEGER PRIMARY KEY AUTOINCREMENT, email TEXT, financeName TEXT, financeAmount REAL, financeType TEXT, FOREIGN KEY (email) REFERENCES users(email))");


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {


    }


    public Cursor getAll() {
        return this.db.rawQuery("SELECT * FROM Finances", null);
    }

    public Cursor getFinancesByEmail(String email) {
        String query = "SELECT * FROM Finances WHERE email = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        return cursor;
    }

    public Cursor getSalaryByEmail(String financeType, String email) {
        String query = "SELECT financeAmount FROM Finances WHERE financeType = ? AND email = ?";
        String[] selectionArgs = {financeType, email};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        return cursor;
    }

    public Cursor getEmergencyFundByEmail(String email) {
        String query = "SELECT * FROM Finances WHERE financeType = 'Emergency Fund' AND email = ?";
        String[] selectionArgs = {email};

        Cursor cursor = db.rawQuery(query, selectionArgs);

        return cursor;
    }


    public Cursor getRetirementFund(String email) {
        return this.db.rawQuery("SELECT financeAmount FROM Finances WHERE financeType = 'Retirement Fund' AND email = ?", new String[]{email});
    }

    public void updateRecord(String columnName, String columnValue, String condition) {
        ContentValues values = new ContentValues();
        values.put(columnName, columnValue);
        db.update("Finances", values, condition, null);

    }

    public void insert(String email, String financeName, double financeAmount, String type) {
        this.db.execSQL("INSERT INTO Finances (email, financeName,  financeAmount, financeType) VALUES(?, ?, ?, ?)",
                new Object[]{email, financeName, financeAmount, type});
    }

    // Overview
    public Cursor getNeedsByEmail(String email) {
        return this.db.rawQuery("SELECT financeAmount, financeName FROM Finances WHERE financeType = 'need' AND email = ?", new String[]{email});
    }

    public Cursor getWantByEmail(String email) {
        return this.db.rawQuery("SELECT financeAmount, financeName FROM Finances WHERE financeType = 'want' AND email = ?", new String[]{email});
    }

    public Cursor getInvestmentsByEmail(String email) {
        return this.db.rawQuery("SELECT financeAmount, financeName FROM Finances WHERE financeType = 'investment' AND email = ?", new String[]{email});
    }

    public Cursor getIncomeByEmail(String email) {
        return this.db.rawQuery("SELECT financeAmount, financeName FROM Finances WHERE financeType = 'income' AND email = ?", new String[]{email});
    }

    public void deleteAll() {
        this.db.execSQL("DELETE FROM Finances");
    }


    public void addGoal(GoalModel goal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", goal.getGoalName());
        values.put("amount", goal.getGoalAmount());
        db.insert("expenses", null, values);
        db.close();
    }

    public ArrayList<GoalModel> getAllGoals() {
        ArrayList<GoalModel> goalList = new ArrayList<>();
        String selectQuery = "SELECT  * FROM expenses";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor.moveToFirst()) {
            do {
                GoalModel goal = new GoalModel(cursor.getString(1), cursor.getDouble(2));
                goal.setId(cursor.getInt(0));
                goalList.add(goal);
            } while (cursor.moveToNext());
        }
        cursor.close();
        db.close();
        return goalList;
    }

    public void deleteGoal(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("expenses", "userId = ?",
                new String[]{String.valueOf(id)});
        db.close();
    }

    // ... your existing code ...

    public void updateGoal(GoalModel goal) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", goal.getGoalName());
        values.put("amount", goal.getGoalAmount());

        // Updating row
        db.update("expenses", values, "userId = ?",
                new String[]{String.valueOf(goal.getId())});

    }

}