package com.example.tltt_application.View;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.viewpager2.widget.ViewPager2;

import com.example.tltt_application.Adapter.ImagePagerAdapter;
import com.example.tltt_application.Adapter.ThumbnailAdapter;
import com.example.tltt_application.R;
import com.example.tltt_application.databinding.ActivityCarDetailBinding;
import com.example.tltt_application.objects.Car;

public class CarDetailActivity extends AppCompatActivity {
    private ActivityCarDetailBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCarDetailBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Nhận dữ liệu từ Intent
        Car car = (Car) getIntent().getSerializableExtra("car");
        if (car == null) {
            Toast.makeText(this, "Lỗi: Không tìm thấy thông tin xe", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Hiển thị thông tin xe
        binding.carName.setText(car.getName() != null ? car.getName() : "N/A");
        binding.carPrice.setText(formatPrice(car.getPrice()));
        binding.carType.setText("Loại xe: " + (car.getTidy() != null ? car.getTidy() : "N/A"));
        binding.carRange.setText("Quãng đường: " + (car.getKind() != null ? car.getKind() : "N/A"));
        binding.carSeats.setText("Số chỗ: " + (car.getSeats() != null ? car.getSeats() : "N/A"));
        binding.carTrunk.setText("Dung tích cốp: " + (car.getTrunk() != null ? car.getTrunk() : "N/A"));

        // Thiết lập ViewPager2 cho ảnh to
        ImagePagerAdapter imagePagerAdapter = new ImagePagerAdapter(car.getImageUrls());
        binding.imageViewPager.setAdapter(imagePagerAdapter);

        // Thiết lập RecyclerView cho ảnh bé
        ThumbnailAdapter thumbnailAdapter = new ThumbnailAdapter(car.getImageUrls(), position -> {
            // Khi nhấn ảnh bé, chuyển ViewPager2 sang ảnh tương ứng
            binding.imageViewPager.setCurrentItem(position, true);
        });
        binding.thumbnailRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.thumbnailRecyclerView.setAdapter(thumbnailAdapter);

        // Đồng bộ ViewPager2 với RecyclerView
        binding.imageViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                thumbnailAdapter.setSelectedPosition(position);
            }
        });

        // Xử lý nút Đặt xe
        binding.bookButton.setOnClickListener(v -> {
            Toast.makeText(this, "Chức năng đặt xe đang được phát triển!", Toast.LENGTH_SHORT).show();
            // Thêm logic đặt xe ở đây (ví dụ: chuyển sang Activity đặt xe)
        });
    }

    private String formatPrice(int price) {
        if (price <= 0) {
            return "N/A";
        }
        return "Chỉ từ " + String.format("%,d", price) + " VNĐ/Ngày";
    }
}