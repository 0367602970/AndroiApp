package com.example.tltt_application.View;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.tltt_application.R;
import com.example.tltt_application.objects.User;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.gson.Gson;

import java.util.HashMap;
import java.util.Map;

public class MyAccountActivity extends AppCompatActivity {

    private static final String TAG = "MyAccountActivity";
    private FirebaseFirestore db;
    private TextView userName, birthDate, gender, license, phone, email;
    private ImageView btnBack, btnEdit;

    private CardView editProfileCard;
    private EditText editName, editBirth, editGender;
    private Button btnSave;

    @SuppressLint("WrongViewCast")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            setContentView(R.layout.activity_my_account);
        } catch (Exception e) {
            Log.e(TAG, "Lỗi khi tải layout: " + e.getMessage(), e);
            Toast.makeText(this, "Lỗi tải giao diện: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Khởi tạo Firestore
        try {
            db = FirebaseFirestore.getInstance();
        } catch (Exception e) {
            Log.e(TAG, "Lỗi khởi tạo Firestore: " + e.getMessage(), e);
            Toast.makeText(this, "Lỗi khởi tạo Firestore: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        String userJson = sharedPreferences.getString("userJson", "");
        User users = null;
        if (!userJson.isEmpty()) {
            Gson gson = new Gson();
            users = gson.fromJson(userJson, User.class);
        }

        // Ánh xạ các thành phần giao diện
        try {
            userName = findViewById(R.id.user_name);
            birthDate = findViewById(R.id.birth_date);
            gender = findViewById(R.id.gender);
            license = findViewById(R.id.license);
            phone = findViewById(R.id.phone);
            email = findViewById(R.id.email);
            btnBack = findViewById(R.id.btnBackImage);
            btnEdit = findViewById(R.id.btnEditImage);
        } catch (Exception e) {
            Log.e(TAG, "Lỗi ánh xạ giao diện: " + e.getMessage(), e);
            Toast.makeText(this, "Lỗi ánh xạ giao diện: " + e.getMessage(), Toast.LENGTH_LONG).show();
            finish();
            return;
        }

        // Lấy số điện thoại từ user
        String userPhone = users.getPhone();

        // Kiểm tra trạng thái đăng nhập
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);
        if (!isLoggedIn || userPhone == null || userPhone.isEmpty()) {
            Log.w(TAG, "Không tìm thấy số điện thoại hoặc chưa đăng nhập");
            Toast.makeText(this, "Vui lòng đăng nhập", Toast.LENGTH_SHORT).show();
            try {
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                finish();
            } catch (Exception e) {
                Log.e(TAG, "Lỗi khi chuyển đến LoginActivity: " + e.getMessage(), e);
                Toast.makeText(this, "Lỗi chuyển màn hình đăng nhập: " + e.getMessage(), Toast.LENGTH_LONG).show();
                finish();
            }
            return;
        }

        // Lấy dữ liệu người dùng từ Firestore
        db.collection("users").document(userPhone).get()
                .addOnCompleteListener(task -> {
                    try {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();
                            if (document != null && document.exists()) {
                                // Chuyển đổi tài liệu thành đối tượng User
                                User user = document.toObject(User.class);
                                if (user != null) {
                                    // Cập nhật giao diện với kiểm tra null
                                    userName.setText(user.getName() != null ? user.getName() : "Không có tên");
                                    birthDate.setText(user.getBirth() != null ? user.getBirth() : "Không có ngày sinh");
                                    gender.setText(user.getGender() != null ? user.getGender() : "Không có giới tính");
                                    license.setText(user.getGPLX() != null ? user.getGPLX() : "Không có GPLX");
                                    phone.setText(user.getPhone() != null ? user.getPhone() : "Không có số điện thoại");
                                    email.setText("Chưa liên kết");
                                } else {
                                    Toast.makeText(this, "Dữ liệu người dùng không hợp lệ", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(this, "Không tìm thấy thông tin người dùng", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Exception e = task.getException();
                            Log.e(TAG, "Lỗi khi lấy dữ liệu Firestore: " + (e != null ? e.getMessage() : "Unknown"), e);
                            Toast.makeText(this, "Lỗi khi lấy dữ liệu: " + (e != null ? e.getMessage() : "Unknown"), Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        Log.e(TAG, "Lỗi khi xử lý dữ liệu Firestore: " + e.getMessage(), e);
                        Toast.makeText(this, "Lỗi xử lý dữ liệu: " + e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });

        // Xử lý nút Quay lại
        try {
            btnBack.setOnClickListener(v -> {
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

        //edit
        editProfileCard = findViewById(R.id.edit_profile_card);
        editName = findViewById(R.id.edit_name);
        editBirth = findViewById(R.id.edit_birth);
        editGender = findViewById(R.id.edit_gender);
        btnSave = findViewById(R.id.btn_save);

        // Click edit icon
        btnEdit.setOnClickListener(v -> {
            editProfileCard.setVisibility(View.VISIBLE);

            // Đổ dữ liệu sẵn
            editName.setText(userName.getText().toString());
            editBirth.setText(birthDate.getText().toString());
            editGender.setText(gender.getText().toString());
        });

        // Click save
        btnSave.setOnClickListener(v -> {
            String newName = editName.getText().toString();
            String newBirth = editBirth.getText().toString();
            String newGender = editGender.getText().toString();

            if (newName.isEmpty() || newBirth.isEmpty() || newGender.isEmpty()) {
                Toast.makeText(this, "Điền đầy đủ thông tin!", Toast.LENGTH_SHORT).show();
                return;
            }

            Map<String, Object> updatedData = new HashMap<>();
            updatedData.put("name", newName);
            updatedData.put("birth", newBirth);
            updatedData.put("gender", newGender);

            db.collection("users").document(userPhone)
                    .set(updatedData, com.google.firebase.firestore.SetOptions.merge())
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(this, "Đã cập nhật", Toast.LENGTH_SHORT).show();
                        editProfileCard.setVisibility(View.GONE);

                        userName.setText(newName);
                        birthDate.setText(newBirth);
                        gender.setText(newGender);
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(this, "Cập nhật thất bại!", Toast.LENGTH_SHORT).show();
                    });
        });


        // Xử lý Window Insets
        try {
            ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
                Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
                v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
                return insets;
            });
        } catch (Exception e) {
            Log.e(TAG, "Lỗi xử lý Window Insets: " + e.getMessage(), e);
            Toast.makeText(this, "Lỗi xử lý giao diện: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }
}