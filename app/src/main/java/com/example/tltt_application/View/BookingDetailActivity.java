package com.example.tltt_application.View;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import com.bumptech.glide.Glide;
import com.example.tltt_application.databinding.ActivityBookingDetailBinding;
import com.example.tltt_application.objects.Booking;

public class BookingDetailActivity extends AppCompatActivity {
    private ActivityBookingDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityBookingDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Booking booking = (Booking) getIntent().getSerializableExtra("booking");

        if (booking != null) {
            Glide.with(this).load(booking.getCarImageUrl()).into(binding.carImage);
            binding.tvPickupDate.setText("Ngày nhận: " + (booking.getPickupDate() != null ? booking.getPickupDate() : "N/A"));
            binding.tvPickupTime.setText("Giờ nhận: " + (booking.getPickupTime() != null ? booking.getPickupTime() : "N/A"));
            binding.tvReturnDate.setText("Ngày trả: " + (booking.getReturnDate() != null ? booking.getReturnDate() : "N/A"));
            binding.tvReturnTime.setText("Giờ trả: " + (booking.getReturnTime() != null ? booking.getReturnTime() : "N/A"));
            binding.tvCity.setText("Thuê xe tại: " + (booking.getCity() != null ? booking.getCity() : "N/A"));
            binding.tvCarName.setText("Tên xe: " + (booking.getCarName() != null ? booking.getCarName() : "N/A"));
            binding.tvUserName.setText("Tên người dùng: " + (booking.getUserName() != null ? booking.getUserName() : "N/A"));
            binding.tvPhone.setText("Số điện thoại: " + (booking.getPhone() != null ? booking.getPhone() : "N/A"));
            binding.tvMethod.setText("Giấy tờ xác nhận: " + (booking.getMethod() != null ? booking.getMethod() : "N/A"));
            binding.tvStatus.setText("Trạng thái: " + (booking.getStatus() == 1 ? "Đang thuê" : "Hoàn thành"));
            binding.tvTotalPrice.setText("Tổng giá: " + String.format("%,d đ", (long) booking.getTotalPrice()));
        }

        try {
            binding.btnBack.setOnClickListener(v -> {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("showFragment", "NotifyFragment"); // Truyền tín hiệu để hiển thị NotifyFragment
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        } catch (Exception e) {
            Log.e(TAG, "Lỗi khi thiết lập nút Quay lại: " + e.getMessage(), e);
            Toast.makeText(this, "Lỗi nút Quay lại: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}