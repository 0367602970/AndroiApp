package com.example.tltt_application.View;

import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.tltt_application.Fragment.AccountFragment;
import com.example.tltt_application.Fragment.HomeFragment;
import com.example.tltt_application.Fragment.NotifyFragment;
import com.example.tltt_application.Fragment.SupportFragment;
import com.example.tltt_application.Fragment.TripFragment;
import com.example.tltt_application.R;
import com.example.tltt_application.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;
    private String name; // Lưu name để sử dụng lại
    private HomeFragment homeFragment;
    private NotifyFragment notifyFragment;
    private TripFragment tripFragment;
    private SupportFragment supportFragment;
    private AccountFragment accountFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Khởi tạo View Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Lấy name từ Intent
        name = getIntent().getStringExtra("name");
        if (name == null) {
            // Nếu không có trong Intent, lấy từ SharedPreferences
            name = getSharedPreferences("LoginPrefs", MODE_PRIVATE)
                    .getString("userName", "Người dùng");
        }

        String lastName = extractLastName(name);

        // Khởi tạo các Fragment và truyền Bundle cho HomeFragment và AccountFragment
        homeFragment = new HomeFragment();
        Bundle homeBundle = new Bundle();
        homeBundle.putString("name", lastName);
        homeFragment.setArguments(homeBundle);

        notifyFragment = new NotifyFragment();
        tripFragment = new TripFragment();
        supportFragment = new SupportFragment();

        accountFragment = new AccountFragment();
        Bundle accountBundle = new Bundle();
        accountBundle.putString("name", name);
        accountFragment.setArguments(accountBundle);

        // Mặc định hiển thị HomeFragment
        showFragment(homeFragment);

        // Xử lý sự kiện bấm vào các ImageView trong linearLayout3
        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        // Đặt trạng thái được chọn cho trang Home mặc định
        binding.navHome.setSelected(true);

        // Trang Home
        binding.navHome.setOnClickListener(v -> {
            resetNavigationSelection();
            binding.navHome.setSelected(true);
            showFragment(homeFragment);
        });

        // Trang Notify
        binding.navNotify.setOnClickListener(v -> {
            resetNavigationSelection();
            binding.navNotify.setSelected(true);
            showFragment(notifyFragment);
        });

        // Trang Trip
        binding.navTrip.setOnClickListener(v -> {
            resetNavigationSelection();
            binding.navTrip.setSelected(true);
            showFragment(tripFragment);
        });

        // Trang Support
        binding.navSupport.setOnClickListener(v -> {
            resetNavigationSelection();
            binding.navSupport.setSelected(true);
            showFragment(supportFragment);
        });

        // Trang Account
        binding.navAccount.setOnClickListener(v -> {
            resetNavigationSelection();
            binding.navAccount.setSelected(true);
            showFragment(accountFragment);
        });
    }

    private String extractLastName(String fullName) {
        if (fullName == null || fullName.trim().isEmpty()) {
            return "Người dùng";
        }

        // Tách chuỗi thành mảng các từ, sử dụng khoảng trắng làm dấu phân cách
        String[] nameParts = fullName.trim().split("\\s+");

        // Lấy phần tử cuối cùng trong mảng (phần tên cuối)
        return nameParts[nameParts.length - 1];
    }

    private void showFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.replace(R.id.fragment_container, fragment);
        transaction.commit();
    }

    private void resetNavigationSelection() {
        binding.navHome.setSelected(false);
        binding.navNotify.setSelected(false);
        binding.navTrip.setSelected(false);
        binding.navSupport.setSelected(false);
        binding.navAccount.setSelected(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}