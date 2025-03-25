package com.example.tltt_application.objects;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity(tableName = "users") // Đặt tên bảng là "users", có thể thay đổi nếu cần
public class User implements Serializable {

    @PrimaryKey(autoGenerate = true)
    private int user_id;

    @ColumnInfo(name = "phoneNumber")
    private String phone;

    private String name;

    private String password;

    @ColumnInfo(name = "confirm_password") // Đặt tên cột cụ thể để rõ ràng
    private String confirmPassword;

    // Constructor rỗng (yêu cầu bởi Room)
    public User() {
    }

    // Constructor đầy đủ (tùy chọn để khởi tạo đối tượng)
    public User(String phone, String name, String password, String confirmPassword) {
        this.phone = phone;
        this.name = name;
        this.password = password;
        this.confirmPassword = confirmPassword;
    }

    // Getter và Setter cho user_id
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    // Getter và Setter cho phone
    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    // Getter và Setter cho name
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter và Setter cho password
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // Getter và Setter cho confirmPassword
    public String getConfirmPassword() {
        return confirmPassword;
    }

    public void setConfirmPassword(String confirmPassword) {
        this.confirmPassword = confirmPassword;
    }
}