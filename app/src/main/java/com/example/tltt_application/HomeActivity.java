package com.example.tltt_application;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import java.util.Calendar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tltt_application.databinding.ActivityHomeBinding;

public class HomeActivity extends AppCompatActivity {
    private ActivityHomeBinding binding;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        calendar = Calendar.getInstance();

        // Gắn sự kiện cho pickup date (TextView và ImageView)
        setupDatePicker(binding.pickupDate, binding.pickupDateIcon);
        setupTimePicker(binding.pickupTime, binding.pickupTimeIcon);

        // Gắn sự kiện cho return date (TextView và ImageView)
        setupDatePicker(binding.returnDate, binding.returnDateIcon);
        setupTimePicker(binding.returnTime, binding.returnTimeIcon);

        logout();
    }

    // Phương thức để gắn sự kiện chọn ngày
    private void setupDatePicker(android.widget.TextView textView, android.widget.ImageView imageView) {
        View.OnClickListener dateClickListener = v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    HomeActivity.this,
                    (view, selectedYear, selectedMonth, selectedDay) -> {
                        String date = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                        textView.setText(date);
                    },
                    year, month, day);
            datePickerDialog.show();
        };

        textView.setOnClickListener(dateClickListener);
        imageView.setOnClickListener(dateClickListener);
    }

    // Phương thức để gắn sự kiện chọn giờ
    private void setupTimePicker(android.widget.TextView textView, android.widget.ImageView imageView) {
        View.OnClickListener timeClickListener = v -> {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    HomeActivity.this,
                    (view, selectedHour, selectedMinute) -> {
                        String time = String.format("%02d:%02d", selectedHour, selectedMinute);
                        textView.setText(time);
                    },
                    hour, minute, true);
            timePickerDialog.show();
        };

        textView.setOnClickListener(timeClickListener);
        imageView.setOnClickListener(timeClickListener);
    }

    public void logout() {
        binding.btnLogout.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
            finish();
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}