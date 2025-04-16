package com.example.tltt_application.View;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.tltt_application.Adapter.CarAdapter;
import com.example.tltt_application.ViewModel.CarListViewModel;
import com.example.tltt_application.databinding.ActivityCarListBinding;
import com.example.tltt_application.objects.Car;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import java.util.ArrayList;
import java.util.List;

public class CarListActivity extends AppCompatActivity {

    private ActivityCarListBinding binding;
    private CarListViewModel viewModel;
    private CarAdapter carAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCarListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Khởi tạo ViewModel
        viewModel = new ViewModelProvider(this).get(CarListViewModel.class);

        // Khởi tạo RecyclerView
        carAdapter = new CarAdapter(this);
        binding.recyclerViewCars.setLayoutManager(new LinearLayoutManager(this));
        binding.recyclerViewCars.setAdapter(carAdapter);
        binding.recyclerViewCars.setHasFixedSize(true);

        //Lấy dữ liệu từ ViewModel
        viewModel.getCarList().observe(this, carList -> {
            if (carList != null && !carList.isEmpty()) {
                carAdapter.updateCarList(carList);
            } else {
                Toast.makeText(this, "Không tìm thấy xe nào!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}