package com.example.myapplication;
import static java.util.Calendar.getInstance;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Button addHabitButton;
    private RecyclerView habitRecyclerView;
    private HabitAdapter habitAdapter;
    private List<Habit> habitList = new ArrayList<>();
    private Dialog dialog;

    private TextView dateTextViewToday;
    private TextView dateTextViewToday_1;
    private TextView dateTextViewToday_2;
    private TextView dateTextViewToday_3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        habitRecyclerView = findViewById(R.id.habitRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        habitRecyclerView.setLayoutManager(layoutManager);

        habitAdapter = new HabitAdapter(habitList);
        habitRecyclerView.setAdapter(habitAdapter);

        Button show = findViewById(R.id.addHabitButton);
        dialog = new Dialog(MainActivity.this);

        // Инициализация TextView
        dateTextViewToday = findViewById(R.id.date_today);
        dateTextViewToday_1 = findViewById(R.id.date_today_1);
        dateTextViewToday_2 = findViewById(R.id.date_today_2);
        dateTextViewToday_3 = findViewById(R.id.date_today_3);

        // Получите текущую дату
        Calendar calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());
        String currentDate_1 = String.valueOf(Integer.parseInt(dateFormat.format(calendar.getTime())) - 1);
        String currentDate_2 = String.valueOf(Integer.parseInt(dateFormat.format(calendar.getTime())) - 2);
        String currentDate_3 = String.valueOf(Integer.parseInt(dateFormat.format(calendar.getTime())) - 3);

        // Установите текущую дату в TextView
        dateTextViewToday.setText(currentDate);
        dateTextViewToday_1.setText(currentDate_1);
        dateTextViewToday_2.setText(currentDate_2);
        dateTextViewToday_3.setText(currentDate_3);

        updateDateVisibility();

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAddHabit();
            }
        });
    }

    private void updateDateVisibility() {
        boolean hasItems = habitAdapter.hasItems();
        if (hasItems) {
            dateTextViewToday.setVisibility(View.VISIBLE);
            dateTextViewToday_1.setVisibility(View.VISIBLE);
            dateTextViewToday_2.setVisibility(View.VISIBLE);
            dateTextViewToday_3.setVisibility(View.VISIBLE);
        } else {
            dateTextViewToday.setVisibility(View.GONE);
            dateTextViewToday_1.setVisibility(View.GONE);
            dateTextViewToday_2.setVisibility(View.GONE);
            dateTextViewToday_3.setVisibility(View.GONE);
        }
    }

    private void showDialogAddHabit() {
        dialog.setContentView(R.layout.dialog_add_habit);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        Button saveHabitName = dialog.findViewById(R.id.saveHabitButton);
        EditText habitName = dialog.findViewById(R.id.habit_name);

        saveHabitName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String habitNameText = habitName.getText().toString().trim();
                if (!habitNameText.isEmpty()) {
                    addHabit(habitNameText);
                    dialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, "Введите название привычки", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }

    private void addHabit(String habitName) {
        Habit newHabit = new Habit(habitName);
        habitList.add(newHabit);
        habitAdapter.notifyDataSetChanged();
        updateDateVisibility();
    }
}
