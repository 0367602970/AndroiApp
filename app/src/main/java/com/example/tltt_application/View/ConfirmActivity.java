package com.example.tltt_application.View;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.bumptech.glide.Glide;
import com.example.tltt_application.R;
import com.example.tltt_application.databinding.ActivityConfirmBinding;
import com.example.tltt_application.objects.Car;
import com.example.tltt_application.objects.User;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class ConfirmActivity extends AppCompatActivity {
    private ActivityConfirmBinding binding;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityConfirmBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }

        Intent intent = getIntent();
        String pickupDate = intent.getStringExtra("pickupDate");
        String pickupTime = intent.getStringExtra("pickupTime");
        String returnDate = intent.getStringExtra("returnDate");
        String returnTime = intent.getStringExtra("returnTime");
        String city = intent.getStringExtra("city");
        Car car = (Car) intent.getSerializableExtra("car");

        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String userJson = sharedPreferences.getString("userJson", "");
        Log.d("ConfirmActivity", "User JSON: " + userJson);
        User user = null;
        if (!userJson.isEmpty()) {
            Gson gson = new Gson();
            user = gson.fromJson(userJson, User.class);
        }

        if (user != null) {
            binding.edName.setText((user.getName() != null ? user.getName() : "N/A"));
            binding.edPhone.setText((user.getPhone() != null ? user.getPhone() : "N/A"));
        } else {
            binding.edName.setText("N/A");
            binding.edPhone.setText("N/A");
        }

        String searchInfoText = city != null ? city : "N/A";
        if (pickupDate != null && pickupTime != null && returnDate != null && returnTime != null) {
            try {
                SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
                Date pickupDateTime = dateFormat.parse(pickupDate + " " + pickupTime);
                Date returnDateTime = dateFormat.parse(returnDate + " " + returnTime);

                long diffInMillies = returnDateTime.getTime() - pickupDateTime.getTime();
                long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

                searchInfoText += ", " + diffInDays + " ngày - " + pickupDate + " " + pickupTime + " - " + returnDate + " " + returnTime;
            } catch (Exception e) {
                e.printStackTrace();
                searchInfoText += ", N/A";
            }
        } else {
            searchInfoText += ", N/A";
        }
        binding.searchInfo.setText(searchInfoText);

        if (car != null) {
            binding.carName.setText(car.getName() != null ? car.getName() : "N/A");
            if (car.getImageUrl() != null) {
                Glide.with(this)
                        .load(car.getImageUrl())
                        .into(binding.carImage);
            } else {
                binding.carImage.setImageResource(R.drawable.ic_launcher_foreground);
            }
            if (pickupDate != null && returnDate != null) {
                try {
                    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
                    Date pickup = dateFormat.parse(pickupDate);
                    Date returnD = dateFormat.parse(returnDate);
                    long diffInMillies = returnD.getTime() - pickup.getTime();
                    long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);

                    double totalPrice = diffInDays * car.getPrice();
                    binding.totalPrice.setText(String.format("%,dđ", (long) totalPrice));
                } catch (Exception e) {
                    e.printStackTrace();
                    binding.totalPrice.setText("N/A");
                }
            } else {
                binding.totalPrice.setText("N/A");
            }
        } else {
            binding.carName.setText("N/A");
            binding.carImage.setImageResource(R.drawable.ic_launcher_foreground);
            binding.totalPrice.setText("N/A");
        }

        Button confirmButton = binding.confirmButton;
        confirmButton.setOnClickListener(v -> {
            Toast.makeText(this, "Nút đặt xe đang xử lý! Hãy thử lại sau", Toast.LENGTH_LONG).show();
//            String pickupLocation = binding.getCar.toString();
//
//            // Xử lý xác nhận đơn hàng (ví dụ: lưu vào Firestore)
//            Toast.makeText(this, "Đặt xe thành công!\nNơi nhận xe: " + pickupLocation + "\nGhi chú: " + note, Toast.LENGTH_LONG).show();
//            finish();
        });
    }
}