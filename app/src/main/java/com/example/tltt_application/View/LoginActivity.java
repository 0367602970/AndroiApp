package com.example.tltt_application.View;

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

import com.example.tltt_application.databinding.ActivityLoginBinding;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
            return;
        }

        customRegister();
        processEvents();
    }

    public void processEvents() {
        binding.btnLogin.setOnClickListener(v -> {
            String phone = binding.editPhone.getText().toString().trim();
            String password = binding.editPassword.getText().toString().trim();

            if (phone.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Vui lòng nhập số điện thoại và mật khẩu!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Truy vấn Firestore
            db.collection("users")
                    .whereEqualTo("phone", phone)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            DocumentSnapshot document = task.getResult().getDocuments().get(0);
                            String storedPassword = document.getString("password");
                            String name = document.getString("name");

                            if (storedPassword != null && storedPassword.equals(password)) {
                                SharedPreferences.Editor editor = getSharedPreferences("LoginPrefs", MODE_PRIVATE).edit();
                                editor.putBoolean("isLoggedIn", true);
                                editor.putString("userName", name); // Lưu name vào SharedPreferences
                                editor.apply();

                                Intent intent = new Intent(this, MainActivity.class);
                                intent.putExtra("name", name); // Truyền name qua Intent
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(this, "Sai mật khẩu!", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(this, "Số điện thoại không tồn tại!", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Log.e("Login", "Lỗi: " + e.getMessage());
                        Toast.makeText(this, "Đã xảy ra lỗi!", Toast.LENGTH_SHORT).show();
                    });
        });
    }

    public void customRegister() {
        String text = "Bạn chưa có tài khoản? Đăng kí ngay";
        SpannableString spannableString = new SpannableString(text);

        int start = text.indexOf("Đăng kí ngay");
        int end = start + "Đăng kí ngay".length();

        ClickableSpan clickableSpan = new ClickableSpan() {
            @Override
            public void onClick(View widget) {
                Intent intent = new Intent(LoginActivity.this, SigninActivity.class);
                startActivity(intent);
            }
        };

        spannableString.setSpan(clickableSpan, start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
        binding.tvRegister.setText(spannableString);
        binding.tvRegister.setMovementMethod(LinkMovementMethod.getInstance());
        binding.tvRegister.setHighlightColor(android.graphics.Color.TRANSPARENT);
    }
}