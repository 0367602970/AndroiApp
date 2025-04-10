package com.example.tltt_application.Fragment;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;

import com.example.tltt_application.CarListActivity;
import com.example.tltt_application.R;
import com.example.tltt_application.databinding.FragmentHomeBinding;
import java.util.Calendar;

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

        // Nhận name từ Bundle
        Bundle bundle = getArguments();
        if (bundle != null) {
            String name = bundle.getString("name");
            if (name != null) {
                binding.nameWelcome.setText("Chào mừng, " + name);
            }
        }

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

        calendar = Calendar.getInstance();

        // Gắn sự kiện cho pickup date (TextView và ImageView)
        setupDatePicker(binding.pickupDate, binding.pickupDateIcon);
        setupTimePicker(binding.pickupTime, binding.pickupTimeIcon);

        // Gắn sự kiện cho return date (TextView và ImageView)
        setupDatePicker(binding.returnDate, binding.returnDateIcon);
        setupTimePicker(binding.returnTime, binding.returnTimeIcon);

        setupSearchButton();

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

    private void setupDatePicker(android.widget.TextView textView, android.widget.ImageView imageView) {
        View.OnClickListener dateClickListener = v -> {
            int year = calendar.get(Calendar.YEAR);
            int month = calendar.get(Calendar.MONTH);
            int day = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    getActivity(),
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

    private void setupTimePicker(android.widget.TextView textView, android.widget.ImageView imageView) {
        View.OnClickListener timeClickListener = v -> {
            int hour = calendar.get(Calendar.HOUR_OF_DAY);
            int minute = calendar.get(Calendar.MINUTE);

            TimePickerDialog timePickerDialog = new TimePickerDialog(
                    getActivity(),
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

    private void setupSearchButton() {
        // Xử lý sự kiện bấm nút "Tìm xe" trong tab_date
        binding.tabDateContent.findViewById(R.id.btn_search_date).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CarListActivity.class);
            startActivity(intent);
        });

        // Xử lý sự kiện bấm nút "Tìm xe" trong tab_month
        binding.tabMonthContent.findViewById(R.id.btn_search_month).setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), CarListActivity.class);
            startActivity(intent);
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}