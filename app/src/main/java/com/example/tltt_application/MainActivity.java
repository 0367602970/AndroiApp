package com.example.tltt_application;

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
import com.example.tltt_application.databinding.ActivityMainBinding;

public class MainActivity extends AppCompatActivity {

    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Khởi tạo View Binding
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Mặc định hiển thị HomeFragment
        showFragment(new HomeFragment());

        // Xử lý sự kiện bấm vào các ImageView trong linearLayout3
        setupBottomNavigation();
    }

    private void setupBottomNavigation() {
        // Đặt trạng thái được chọn cho trang Home mặc định
        binding.navHome.setSelected(true);

        // Trang Home
        binding.navHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetNavigationSelection();
                binding.navHome.setSelected(true);
                showFragment(new HomeFragment());
            }
        });

        // Trang Notify
        binding.navNotify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               resetNavigationSelection();
               binding.navNotify.setSelected(true);
                showFragment(new NotifyFragment());
            }
        });

        // Trang Trip
        binding.navTrip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetNavigationSelection();
                binding.navTrip.setSelected(true);
                showFragment(new TripFragment());
            }
        });

        //Trang Support
        binding.navSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetNavigationSelection();
                binding.navSupport.setSelected(true);
                showFragment(new SupportFragment());
            }
        });

        // Trang Account
        binding.navAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetNavigationSelection();
                binding.navAccount.setSelected(true);
                showFragment(new AccountFragment());
            }
        });
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