package com.example.tltt_application.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.tltt_application.Fragment.HomeFragment;
import com.example.tltt_application.databinding.ActivityLoginBinding;
import com.example.tltt_application.viewmodels.AuthViewModel;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private AuthViewModel viewModel;
    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        viewModel = new ViewModelProvider(this).get(AuthViewModel.class);
        sharedPreferences = getSharedPreferences("MyPrefs", MODE_PRIVATE);

        // Gọi phương thức tạo liên kết đăng ký
        setupRegisterLink();

        // Xử lý sự kiện đăng nhập
        binding.btnLogin.setOnClickListener(v -> {
            String phone = binding.editPhone.getText().toString().trim();
            String password = binding.editPassword.getText().toString().trim();

            if (validateInput(phone, password)) {
                binding.btnLogin.setEnabled(false); // Vô hiệu hóa nút khi xử lý
                viewModel.login(phone, password);
            } else {
                Toast.makeText(this, "Please enter valid phone and password", Toast.LENGTH_SHORT).show();
            }
        });

        // Quan sát kết quả từ ViewModel
        viewModel.getAuthResult().observe(this, result -> {
            binding.btnLogin.setEnabled(true); // Kích hoạt lại nút
            if (result.isSuccess()) {
                // Lưu trạng thái đăng nhập
                sharedPreferences.edit().putBoolean("isLoggedIn", true).apply();
                Toast.makeText(this, "Login successful", Toast.LENGTH_SHORT).show();
                // Chuyển sang màn hình chính
                startActivity(new Intent(this, MainActivity.class));
                finish();
            } else {
                Toast.makeText(this, result.getMessage() != null ? result.getMessage() : "Login failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setupRegisterLink() {
        String text = "Bạn chưa có tài khoản? Đăng kí ngay";
        SpannableString spannableString = new SpannableString(text);

        int start = text.indexOf("Đăng kí ngay");
        int end = start + "Đăng kí ngay".length();

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                startActivity(new Intent(LoginActivity.this, SigninActivity.class));
            }
        };

        spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.tvRegister.setText(spannableString);
        binding.tvRegister.setMovementMethod(LinkMovementMethod.getInstance());
        binding.tvRegister.setHighlightColor(android.graphics.Color.TRANSPARENT);
    }

    private boolean validateInput(String phone, String password) {
        if (phone.isEmpty() || password.isEmpty()) {
            return false;
        }
        // Kiểm tra định dạng số điện thoại (ví dụ: 10 số)
        if (!phone.matches("\\d{10}")) {
            return false;
        }
        // Kiểm tra độ dài mật khẩu (ví dụ: tối thiểu 6 ký tự)
        return password.length() >= 6;
    }
}