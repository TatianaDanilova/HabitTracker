package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class DescriptionHabitActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description_habit);

        // Получение значения habitName из интента
        String habitName = getIntent().getStringExtra("habitName");

        // Нахождение TextView habit_name и установка значения habitName
        TextView habitNameTextView = findViewById(R.id.habit_name);
        habitNameTextView.setText(habitName);
    }
}