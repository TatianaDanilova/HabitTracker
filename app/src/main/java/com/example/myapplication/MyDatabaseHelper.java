package com.example.myapplication;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class MyDatabaseHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "habit_tracker.db";
    private static final int DATABASE_VERSION = 1;

    // Таблица Habits
    private static final String CREATE_TABLE_HABITS = "CREATE TABLE " +
            "Habits (habit_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "name TEXT, " +
            "description TEXT)";

    // Таблица History
    private static final String CREATE_TABLE_HISTORY = "CREATE TABLE " +
            "History (date TEXT, " +
            "completed INTEGER, " +
            "habit_id INTEGER, " +
            "FOREIGN KEY(habit_id) REFERENCES Habits(habit_id))";
    private Context context;

    public MyDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_HABITS);
        db.execSQL(CREATE_TABLE_HISTORY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // При необходимости выполните действия по обновлению базы данных (например, удаление старых таблиц и создание новых)
        db.execSQL("DROP TABLE IF EXISTS Habits");
        db.execSQL("DROP TABLE IF EXISTS History");
        onCreate(db);
    }
    public void addHabit(String name, String description) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("name", name);
        values.put("description", description);
        long result = db.insert("Habits", null, values);
        if(result == -1){
            Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Все супер", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }

    public void addHistory(String date, boolean completed, int habitId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("date", date);
        values.put("completed", completed ? 1 : 0); // 1 для true, 0 для false
        values.put("habit_id", habitId);
        long result = db.insert("History", null, values);
        if(result == -1){
            Toast.makeText(context, "Ошибка", Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "Все супер", Toast.LENGTH_SHORT).show();
        }
        db.close();
    }


}
