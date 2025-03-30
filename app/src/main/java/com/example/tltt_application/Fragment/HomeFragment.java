package com.example.tltt_application.Fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.tltt_application.LoginActivity;
import com.example.tltt_application.R;
import com.example.tltt_application.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;
    private Calendar calendar;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Khởi tạo View Binding cho Fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false);

        // Hiển thị tab_date mặc định
        showTabDate();

        // Xử lý sự kiện bấm vào tab_date
        binding.tabDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTabDate();
            }
        });

        // Xử lý sự kiện bấm vào tab_month
        binding.tabMonth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showTabMonth();
            }
        });

        // Khởi tạo calendar
        calendar = Calendar.getInstance();

        // Gắn sự kiện cho pickup date (TextView và ImageView)
        setupDatePicker(binding.pickupDate, binding.pickupDateIcon);
        setupTimePicker(binding.pickupTime, binding.pickupTimeIcon);

        // Gắn sự kiện cho return date (TextView và ImageView)
        setupDatePicker(binding.returnDate, binding.returnDateIcon);
        setupTimePicker(binding.returnTime, binding.returnTimeIcon);

        return binding.getRoot();
    }

    private void showTabDate() {
        // Hiển thị nội dung của tab_date
        binding.tabDateContent.setVisibility(View.VISIBLE);
        binding.tabMonthContent.setVisibility(View.GONE);

        // Cập nhật giao diện của tab
        binding.tabDate.setBackgroundResource(R.drawable.tab_btn_l);
        binding.tabDate.setTextColor(getResources().getColor(android.R.color.white));
        binding.tabMonth.setBackgroundResource(android.R.color.transparent);
        binding.tabMonth.setTextColor(getResources().getColor(android.R.color.black));
    }

    private void showTabMonth() {
        // Hiển thị nội dung của tab_month
        binding.tabDateContent.setVisibility(View.GONE);
        binding.tabMonthContent.setVisibility(View.VISIBLE);

        // Cập nhật giao diện của tab
        binding.tabMonth.setBackgroundResource(R.drawable.tab_btn_r);
        binding.tabMonth.setTextColor(getResources().getColor(android.R.color.white));
        binding.tabDate.setBackgroundResource(android.R.color.transparent);
        binding.tabDate.setTextColor(getResources().getColor(android.R.color.black));
    }

    // Phương thức để gắn sự kiện chọn ngày
    private void setupDatePicker(android.widget.TextView textView, android.widget.ImageView imageView) {
        View.OnClickListener dateClickListener = v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    requireContext(), // Sử dụng requireContext() thay vì HomeFragment.this
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
                    requireContext(), // Sử dụng requireContext() thay vì HomeFragment.this
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Giải phóng binding
    }
}