package com.example.tltt_application.Repository;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.tltt_application.objects.Car;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class CarRepository {
    private FirebaseFirestore db;
    private MutableLiveData<List<Car>> carListLiveData;

    public CarRepository(){
        db = FirebaseFirestore.getInstance();
        carListLiveData = new MutableLiveData<>();
    }

    public LiveData<List<Car>> getCarList() {
        fetchCars();
        return carListLiveData;
    }

    private void fetchCars() {
        db.collection("cars")
                .get()
                .addOnCompleteListener(task -> {
                    if(task.isSuccessful()) {
                        Log.d("CarRepository", "Lấy dữ liệu thành công");
                        List<Car> carList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            try {
                                Car car = document.toObject(Car.class);
                                Log.d("CarRepository", "Document ID: " + document.getId());
                                Log.d("CarRepository", "ImageUrl: " + car.getImageUrl());
                                Log.d("CarRepository", "ImageUrls: " + car.getImageUrls());
                                Log.d("CarRepository", "Name: " + car.getName());
                                Log.d("CarRepository", "Price: " + car.getPrice());
                                Log.d("CarRepository", "Tidy: " + car.getTidy());
                                Log.d("CarRepository", "Seats: " + car.getSeats());
                                Log.d("CarRepository", "Trunk: " + car.getTrunk());
                                Log.d("CarRepository", "Kind: " + car.getKind());
                                carList.add(car);
                            } catch (Exception e) {
                                Log.e("CarRepository", "Lỗi khi ánh xạ document: " + document.getId(), e);
                            }
                        }
                        carListLiveData.setValue(carList);
                    } else {
                        Log.e("CarRepository", "Lỗi khi lấy dữ liệu: ", task.getException());
                        carListLiveData.setValue(new ArrayList<>());
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("CarRepository", "Lỗi: " + e.getMessage(), e);
                    carListLiveData.setValue(new ArrayList<>());
                });
    }
}
