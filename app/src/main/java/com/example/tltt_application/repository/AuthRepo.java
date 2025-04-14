package com.example.tltt_application.repository;

import com.example.tltt_application.models.UserModel;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class AuthRepo {
    private FirebaseFirestore db;

    public AuthRepo() {
        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();
    }

    public void registerUser(String name, String phone, String password, AuthCallback callback) {
        if (!phone.matches("\\d{10}")) {
            callback.onComplete(false, "Invalid phone number format. Must be 10 digits.");
            return;
        }

        // Kiểm tra xem số điện thoại đã tồn tại chưa
        db.collection("users")
                .whereEqualTo("phone", phone)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null && !querySnapshot.isEmpty()) {
                            // Số điện thoại đã tồn tại
                            callback.onComplete(false, "Phone number already exists!");
                        } else {
                            // Số điện thoại chưa tồn tại, tiến hành đăng ký
                            UserModel user = new UserModel(name, phone, password);
                            db.collection("users")
                                    .document(phone) // Sử dụng số điện thoại làm ID của tài liệu
                                    .set(user)
                                    .addOnCompleteListener(dbTask -> {
                                        if (dbTask.isSuccessful()) {
                                            callback.onComplete(true, null);
                                        } else {
                                            callback.onComplete(false, dbTask.getException().getMessage());
                                        }
                                    });
                        }
                    } else {
                        callback.onComplete(false, task.getException().getMessage());
                    }
                });
    }

    public void loginUser(String phone, String password, AuthCallback callback) {
        if (!phone.matches("\\d{10}")) {
            callback.onComplete(false, "Invalid phone number format. Must be 10 digits.");
            return;
        }

        // Truy vấn Firestore để kiểm tra thông tin đăng nhập
        db.collection("users")
                .whereEqualTo("phone", phone)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !task.getResult().isEmpty()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        UserModel user = querySnapshot.getDocuments().get(0).toObject(UserModel.class);
                        if (user != null && user.getPassword().equals(password)) {
                            callback.onComplete(true, null);
                        } else {
                            callback.onComplete(false, "Incorrect password!");
                        }
                    } else {
                        callback.onComplete(false, "Phone number does not exist!");
                    }
                })
                .addOnFailureListener(e -> {
                    callback.onComplete(false, "Error: " + e.getMessage());
                });
    }

    public interface AuthCallback {
        void onComplete(boolean success, String message);
    }
}
