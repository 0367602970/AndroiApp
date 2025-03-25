package com.example.tltt_application;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tltt_application.AppDatabase.AppDatabase;
import com.example.tltt_application.objects.User;

public class LoginActivity extends AppCompatActivity {
    private EditText txtPhone, txtPassword;
    private Button btnLogin;
    private AppDatabase db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login2);
        SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
        boolean isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false);

        if (isLoggedIn) {
            Intent intent = new Intent(LoginActivity.this, HomeActivity.class);
            startActivity(intent);
            finish();
        }
        customRegister();
        init();
        processEvents();
    }

    public void init(){
        try {
            txtPhone = findViewById(R.id.editPhone);
            txtPassword = findViewById(R.id.editPassword);
            btnLogin = findViewById(R.id.btnLogin);
            db = AppDatabase.getDatabase(this);
        } catch (Exception ex) {
            Log.e("Init", ex.getMessage());
        }
    }

    public void processEvents(){
        btnLogin.setOnClickListener(v -> {
            try {
                String phone = txtPhone.getText().toString();
                String password = txtPassword.getText().toString();

                AppDatabase.databaseWriteExecutor.execute(() -> {
                    User user = db.userDao().getUserByPhoneNumber(phone);
                    if (user == null) {
                        runOnUiThread(() -> Toast.makeText(this, "Số điện thoại không tồn tại!", Toast.LENGTH_SHORT).show());
                        return;
                    }
                    String name = user.getName();
                    if (user != null && user.getPassword().equals(password)){
                        runOnUiThread(() -> {
                            SharedPreferences sharedPreferences = getSharedPreferences("LoginPrefs", MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();
                            editor.putBoolean("isLoggedIn", true);
                            editor.apply();
                            Intent intent = new Intent(this, HomeActivity.class);
                            intent.putExtra("name", name);
                            startActivity(intent);
                            finish();
                        });
                    } else {
                        runOnUiThread(() -> Toast.makeText(this, "Incorrect phone or password!", Toast.LENGTH_SHORT).show());
                    }
                });
            } catch (Exception ex) {
                Log.e("Login", ex.getMessage());
                Toast.makeText(this, "Error occurred!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void customRegister(){
        TextView tvRegister = findViewById(R.id.tvRegister);

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

        tvRegister.setText(spannableString);
        tvRegister.setMovementMethod(LinkMovementMethod.getInstance());
        tvRegister.setHighlightColor(Color.TRANSPARENT);

    }
}