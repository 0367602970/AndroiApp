package com.example.tltt_application.views;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tltt_application.Adapter.CarAdapter;
import com.example.tltt_application.databinding.ActivityCarListBinding;
import com.example.tltt_application.objects.Car;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class CarListActivity extends AppCompatActivity {

    private ActivityCarListBinding binding;
    private FirebaseFirestore db;
    private List<Car> carList;
    private CarAdapter carAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCarListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo Firestore
        db = FirebaseFirestore.getInstance();

        // Khởi tạo RecyclerView
        carList = new ArrayList<>();
        carAdapter = new CarAdapter(this, carList);
        binding.recyclerViewCars.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewCars.setAdapter(carAdapter);

        // Lấy dữ liệu từ Firestore
        fetchCars();
    }

    private void fetchCars() {
        Log.d("CarListActivity", "Bắt đầu lấy dữ liệu từ Firestore...");
        db.collection("cars")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Log.d("CarListActivity", "Lấy dữ liệu thành công!");
                        carList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            try {
                                Car car = document.toObject(Car.class);
                                // Log dữ liệu để kiểm tra
                                Log.d("CarListActivity", "Document ID: " + document.getId());
                                Log.d("CarListActivity", "ImageUrl: " + car.getImageUrl());
                                Log.d("CarListActivity", "Name: " + car.getName());
                                Log.d("CarListActivity", "Price: " + car.getPrice());
                                Log.d("CarListActivity", "Tidy: " + car.getTidy());
                                Log.d("CarListActivity", "Seats: " + car.getSeats());
                                Log.d("CarListActivity", "Trunk: " + car.getTrunk());
                                Log.d("CarListActivity", "Kind: " + car.getKind());
                                carList.add(car);
                            } catch (Exception e) {
                                Log.e("CarListActivity", "Lỗi khi ánh xạ document: " + document.getId(), e);
                            }
                        }
                        carAdapter.notifyDataSetChanged();
                        if (carList.isEmpty()) {
                            Log.d("CarListActivity", "Danh sách xe trống!");
                            Toast.makeText(this, "Không tìm thấy xe nào!", Toast.LENGTH_SHORT).show();
                        } else {
                            Log.d("CarListActivity", "Tổng số xe: " + carList.size());
                        }
                    } else {
                        Log.e("CarListActivity", "Lỗi khi lấy dữ liệu: ", task.getException());
                        Toast.makeText(this, "Đã xảy ra lỗi: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("CarListActivity", "Lỗi: " + e.getMessage(), e);
                    Toast.makeText(this, "Đã xảy ra lỗi: " + e.getMessage(), Toast.LENGTH_LONG).show();
                });
    }
}