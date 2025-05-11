package com.example.tltt_application.View;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tltt_application.databinding.ActivityChangePasswordBinding;
import com.example.tltt_application.objects.User;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

public class ChangePasswordActivity extends AppCompatActivity {

    private com.example.tltt_application.databinding.ActivityChangePasswordBinding binding;
    private FirebaseFirestore db;
    private String currentPhone;
    private String currentPasswordStored; // password hiện tại lấy từ Firestore

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityChangePasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        db = FirebaseFirestore.getInstance();

        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String userJson = sharedPreferences.getString("userJson", "");
        User users = null;
        if (!userJson.isEmpty()) {
            Gson gson = new Gson();
            users = gson.fromJson(userJson, User.class);
        }
        currentPhone = users.getPhone();

        if (currentPhone == null) {
            Toast.makeText(this, "Không tìm thấy thông tin đăng nhập", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Lấy mật khẩu hiện tại từ Firestore
        db.collection("users")
                .document(currentPhone)
                .get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        currentPasswordStored = documentSnapshot.getString("password");
                    } else {
                        Toast.makeText(this, "Không tìm thấy người dùng", Toast.LENGTH_SHORT).show();
                        finish();
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi lấy dữ liệu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    finish();
                });

        binding.btnConfirm.setOnClickListener(v -> handleChangePassword());

        // Xử lý nút Quay lại
        try {
            binding.btnBack.setOnClickListener(v -> {
                Intent intent = new Intent(this, MainActivity.class);
                intent.putExtra("showFragment", "AccountFragment"); // Truyền tín hiệu để hiển thị AccountFragment
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);
                finish();
            });
        } catch (Exception e) {
            Log.e(TAG, "Lỗi khi thiết lập nút Quay lại: " + e.getMessage(), e);
            Toast.makeText(this, "Lỗi nút Quay lại: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private void handleChangePassword() {
        String currentPasswordInput = binding.edtCurrentPassword.getText().toString().trim();
        String newPassword = binding.edtNewPassword.getText().toString().trim();
        String confirmPassword = binding.edtConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(currentPasswordInput) || TextUtils.isEmpty(newPassword) || TextUtils.isEmpty(confirmPassword)) {
            Toast.makeText(this, "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!currentPasswordInput.equals(currentPasswordStored)) {
            Toast.makeText(this, "Mật khẩu hiện tại không đúng", Toast.LENGTH_SHORT).show();
            return;
        }

        if (!newPassword.equals(confirmPassword)) {
            Toast.makeText(this, "Mật khẩu mới và xác nhận mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            return;
        }

        if (newPassword.length() < 6) {
            Toast.makeText(this, "Mật khẩu mới phải từ 6 ký tự trở lên", Toast.LENGTH_SHORT).show();
            return;
        }

        // Update mật khẩu mới lên Firestore
        db.collection("users")
                .document(currentPhone)
                .update("password", newPassword)
                .addOnSuccessListener(unused -> {
                    Toast.makeText(this, "Đổi mật khẩu thành công", Toast.LENGTH_SHORT).show();

                    // Nếu muốn logout sau khi đổi mật khẩu:
                    SharedPreferences.Editor editor = getSharedPreferences("LoginPrefs", MODE_PRIVATE).edit();
                    editor.clear();
                    editor.apply();

                    Intent intent = new Intent(this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(this, "Lỗi khi đổi mật khẩu: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}