package com.example.tltt_application;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tltt_application.databinding.ActivitySigninBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SigninActivity extends AppCompatActivity {
    private ActivitySigninBinding binding;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        binding = ActivitySigninBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        processEvents();
    }

    public void processEvents() {
        binding.btnSignup.setOnClickListener(view -> {
            String phoneNumber = binding.editPhone.getText().toString().trim();
            String name = binding.editName.getText().toString().trim();
            String password = binding.editPassword.getText().toString().trim();
            String confirmPassword = binding.editConfirmPassword.getText().toString().trim();

            if (validateInput(phoneNumber, name, password, confirmPassword)) {
                // Kiểm tra xem số điện thoại đã tồn tại chưa
                db.collection("users")
                        .whereEqualTo("phone", phoneNumber)
                        .get()
                        .addOnCompleteListener(task -> {
                            if (task.isSuccessful() && !task.getResult().isEmpty()) {
                                Toast.makeText(this, "Số điện thoại đã tồn tại!", Toast.LENGTH_SHORT).show();
                            } else {
                                // Tạo dữ liệu user
                                Map<String, Object> user = new HashMap<>();
                                user.put("phone", phoneNumber);
                                user.put("name", name);
                                user.put("password", password);

                                // Lưu vào Firestore
                                db.collection("users")
                                        .document(phoneNumber) // Dùng phoneNumber làm ID document
                                        .set(user)
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(this, "Đăng ký thành công! Chuyển đến đăng nhập...", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(this, LoginActivity.class);
                                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                            startActivity(intent);
                                            finish();
                                        })
                                        .addOnFailureListener(e -> {
                                            Log.e("Signin", "Lỗi: " + e.getMessage());
                                            Toast.makeText(this, "Đã xảy ra lỗi!", Toast.LENGTH_SHORT).show();
                                        });
                            }
                        });
            }
        });
    }

    private boolean validateInput(String phoneNumber, String name, String password, String confirmPassword) {
        if (phoneNumber.isEmpty()) {
            binding.editPhone.setError("Vui lòng nhập số điện thoại!");
            binding.editPhone.requestFocus();
            return false;
        }
        if (!phoneNumber.matches("\\d{10}")) {
            binding.editPhone.setError("Số điện thoại phải có đúng 10 chữ số!");
            binding.editPhone.requestFocus();
            return false;
        }

        if (name.isEmpty()) {
            binding.editName.setError("Vui lòng nhập tên!");
            binding.editName.requestFocus();
            return false;
        }

        if (password.isEmpty()) {
            binding.editPassword.setError("Vui lòng nhập mật khẩu!");
            binding.editPassword.requestFocus();
            return false;
        }
        if (password.length() <= 8) {
            binding.editPassword.setError("Mật khẩu phải dài hơn 8 ký tự!");
            binding.editPassword.requestFocus();
            return false;
        }

        if (!password.equals(confirmPassword)) {
            binding.editConfirmPassword.setError("Mật khẩu không khớp!");
            binding.editConfirmPassword.requestFocus();
            return false;
        }

        return true;
    }
}

