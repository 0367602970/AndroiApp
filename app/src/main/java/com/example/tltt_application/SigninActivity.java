package com.example.tltt_application;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tltt_application.AppDatabase.AppDatabase;
import com.example.tltt_application.objects.User;

public class SigninActivity extends AppCompatActivity {
    private EditText txtPhone, txtName, txtPassword, txtConfirmPassword;
    private Button btnRegister;
    private AppDatabase db;

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signin);
        init();
        processEvents();
    }

    public void init(){
        try {
            txtPhone = findViewById(R.id.editPhone);
            txtName = findViewById(R.id.editName);
            txtPassword = findViewById(R.id.editPassword);
            txtConfirmPassword = findViewById(R.id.editConfirmPassword);
            btnRegister = findViewById(R.id.btnSignup);
            db = AppDatabase.getDatabase(this);
        } catch (Exception ex) {
            Log.e("Init", ex.getMessage());
        }
    }

    public void processEvents() {
        btnRegister.setOnClickListener(view -> {
            try {
                String phoneNumber = txtPhone.getText().toString();
                String name = txtName.getText().toString();
                String password = txtPassword.getText().toString();
                String confirmPassword = txtConfirmPassword.getText().toString();
                if (validateInput(phoneNumber, name, password, confirmPassword)) {
                    AppDatabase.databaseWriteExecutor.execute(() -> {
                        User existingUser = db.userDao().getUserByPhoneNumber(phoneNumber);
                        if (existingUser != null) {
                            runOnUiThread(() -> Toast.makeText(this, "Phone number already exists!", Toast.LENGTH_SHORT).show());
                        } else {
                            User newUser = new User(phoneNumber, name, password, confirmPassword);
                            db.userDao().insert(newUser);
                            runOnUiThread(() -> {
                                Toast.makeText(this, "Register successful! Redirecting to login...", Toast.LENGTH_SHORT).show();

                                // Chuyển về màn hình đăng nhập
                                Intent intent = new Intent(this, LoginActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish(); // Kết thúc RegisterActivity để không quay lại nữa
                            });
                        }
                    });
                }
            } catch (Exception ex) {
                Log.e("Register", ex.getMessage());
                Toast.makeText(this, "Error occurred!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private boolean validateInput(String phoneNumber, String name, String password, String confirmPassword) {
        if (phoneNumber.isEmpty()) {
            txtPhone.setError("Username required!");
            txtPhone.requestFocus();
            return false;
        }
        if (name.isEmpty()) {
            txtPassword.setError("Name required!");
            txtPassword.requestFocus();
            return false;
        }
        if (password.isEmpty()) {
            txtPassword.setError("Password required!");
            txtPassword.requestFocus();
            return false;
        }
        if (!password.equals(confirmPassword)) {
            txtConfirmPassword.setError("Passwords do not match!");
            txtConfirmPassword.requestFocus();
            return false;
        }
        return true;
    }


}