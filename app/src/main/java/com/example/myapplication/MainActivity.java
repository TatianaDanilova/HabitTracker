package com.example.myapplication;
import static android.content.ContentValues.TAG;
import static java.util.Calendar.getInstance;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private RecyclerView habitRecyclerView;
    private HabitAdapter habitAdapter;
    private List<Habit> habitList = new ArrayList<>();
    private Dialog dialog;

    private TextView dateTextViewToday;
    private TextView dateTextViewToday_1;
    private TextView dateTextViewToday_2;
    private TextView dateTextViewToday_3;
    private TextView dayOfWeekTextViewToday;
    private TextView dayOfWeekTextViewToday_1;
    private TextView dayOfWeekTextViewToday_2;
    private TextView dayOfWeekTextViewToday_3;
    private TextView no_habit_msg;
    EditText habit_name;
    EditText description;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        habit_name = findViewById(R.id.habit_name);
        description = findViewById(R.id.habit_description);

        habitRecyclerView = findViewById(R.id.habitRecyclerView);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        habitRecyclerView.setLayoutManager(layoutManager);

        habitAdapter = new HabitAdapter(habitList);
        habitRecyclerView.setAdapter(habitAdapter);

        Button show = findViewById(R.id.addHabitButton);
        dialog = new Dialog(MainActivity.this);

        no_habit_msg = findViewById(R.id.no_habit_msg);

        // Инициализация TextView
        dateTextViewToday = findViewById(R.id.date_today);
        dateTextViewToday_1 = findViewById(R.id.date_today_1);
        dateTextViewToday_2 = findViewById(R.id.date_today_2);
        dateTextViewToday_3 = findViewById(R.id.date_today_3);

        dayOfWeekTextViewToday = findViewById(R.id.dayOfWeekTextViewToday);
        dayOfWeekTextViewToday_1 = findViewById(R.id.dayOfWeekTextViewToday_1);
        dayOfWeekTextViewToday_2 = findViewById(R.id.dayOfWeekTextViewToday_2);
        dayOfWeekTextViewToday_3 = findViewById(R.id.dayOfWeekTextViewToday_3);

        Calendar calendar;
        calendar = Calendar.getInstance();
        DateFormat dateFormat = new SimpleDateFormat("dd", Locale.getDefault());
        String currentDate = dateFormat.format(calendar.getTime());

        dateTextViewToday.setText(currentDate);

        DateFormat dayOfWeekFormat = new SimpleDateFormat("EE", Locale.getDefault());
        String dayOfWeek = dayOfWeekFormat.format(calendar.getTime());

        dayOfWeekTextViewToday.setText(dayOfWeek);

        calendar.add(Calendar.DAY_OF_MONTH, -1);
        dateTextViewToday_1.setText(dateFormat.format(calendar.getTime()));
        dayOfWeek = dayOfWeekFormat.format(calendar.getTime());
        dayOfWeekTextViewToday_1.setText(dayOfWeek);

        calendar.add(Calendar.DAY_OF_MONTH, -1);
        dateTextViewToday_2.setText(dateFormat.format(calendar.getTime()));
        dayOfWeek = dayOfWeekFormat.format(calendar.getTime());
        dayOfWeekTextViewToday_2.setText(dayOfWeek);

        calendar.add(Calendar.DAY_OF_MONTH, -1);
        dateTextViewToday_3.setText(dateFormat.format(calendar.getTime()));
        dayOfWeek = dayOfWeekFormat.format(calendar.getTime());
        dayOfWeekTextViewToday_3.setText(dayOfWeek);


        updateDateVisibility();

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialogAddHabit();
                habitAdapter.setOnItemClickListener(new HabitAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Habit habit) {
                        Intent intent = new Intent(MainActivity.this, DescriptionHabitActivity.class);
                        // Передача значения habit.getName() через интент
                        intent.putExtra("habitName", habit.getName());
                        // Запуск новой активити
                        startActivity(intent);
                    }
                });
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
            dayOfWeekTextViewToday.setVisibility(View.VISIBLE);
            dayOfWeekTextViewToday_1.setVisibility(View.VISIBLE);
            dayOfWeekTextViewToday_2.setVisibility(View.VISIBLE);
            dayOfWeekTextViewToday_3.setVisibility(View.VISIBLE);
            no_habit_msg.setVisibility(View.GONE);
        } else {
            dateTextViewToday.setVisibility(View.GONE);
            dateTextViewToday_1.setVisibility(View.GONE);
            dateTextViewToday_2.setVisibility(View.GONE);
            dateTextViewToday_3.setVisibility(View.GONE);
            dayOfWeekTextViewToday.setVisibility(View.GONE);
            dayOfWeekTextViewToday_1.setVisibility(View.GONE);
            dayOfWeekTextViewToday_2.setVisibility(View.GONE);
            dayOfWeekTextViewToday_3.setVisibility(View.GONE);
            no_habit_msg.setVisibility(View.VISIBLE);
        }
    }

    private void showDialogAddHabit() {
        dialog.setContentView(R.layout.dialog_add_habit);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.setCancelable(true);

        Button saveHabitName = dialog.findViewById(R.id.saveHabitButton);
        EditText habitName = dialog.findViewById(R.id.habit_name);
        EditText habitDescription = dialog.findViewById(R.id.habit_description);

        saveHabitName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String habitNameText = habitName.getText().toString().trim();
                String habitDescriptionText = habitDescription.getText().toString().trim();
                if (!habitNameText.isEmpty()) {
                    addHabit(habitNameText, habitDescriptionText);
                    dialog.dismiss();
                } else {
                    Toast.makeText(MainActivity.this, "Введите название привычки", Toast.LENGTH_SHORT).show();
                }
            }
        });

        dialog.show();
    }


    private void addHabit(String habitName, String habitDescription) {
        Habit newHabit = new Habit(habitName);
        habitList.add(newHabit);
        habitAdapter.notifyDataSetChanged();
        updateDateVisibility();
        MyDatabaseHelper myDB = new MyDatabaseHelper(MainActivity.this);
        myDB.addHabit(habitName, habitDescription);
    }
}
