package com.example.tltt_application.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.tltt_application.R;
import com.example.tltt_application.objects.Car;

import java.util.List;

public class CarAdapter extends RecyclerView.Adapter<CarAdapter.CarViewHolder> {
    private Context context;
    private List<Car> carList;

    public CarAdapter(Context context, List<Car> carList) {
        this.context = context;
        this.carList = carList;
    }

    @NonNull
    @Override
    public CarViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_car, parent, false);
        return new CarViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CarViewHolder holder, int position) {
        Car car = carList.get(position);

        // Hiển thị hình ảnh xe bằng Glide
        Glide.with(context)
                .load(car.getImageUrl())
                .into(holder.carImage);

        // Hiển thị các thông tin khác
        holder.carPrice.setText(formatPrice(car.getPrice()));

        holder.carName.setText(car.getName() != null ? car.getName() : "N/A");
        holder.carTidy.setText(car.getTidy() != null ? car.getTidy() : "N/A");  // Thay type bằng tidy
        holder.carKind.setText(car.getKind() != null ? car.getKind() : "N/A");  // Thay range bằng kind
        holder.carSeats.setText(car.getSeats() != null ? car.getSeats() : "N/A");
        holder.carTrunk.setText(car.getTrunk() != null ? car.getTrunk() : "N/A");
    }

    // Phương thức định dạng giá thuê
    private String formatPrice(int price) {
        // Định dạng số với dấu chấm phân cách hàng nghìn
        return "Chỉ từ " + String.format("%,d", price) + " VNĐ/Ngày";
    }

    @Override
    public int getItemCount() {
        if (carList != null && carList.size() > 0)
            return carList.size();
        else
            return 0;
    }

    public static class CarViewHolder extends RecyclerView.ViewHolder {
        ImageView carImage;
        TextView carPrice, carName, carKind, carTidy, carSeats, carTrunk;

        public CarViewHolder(@NonNull View itemView) {
            super(itemView);
            carImage = itemView.findViewById(R.id.car_image);
            carPrice = itemView.findViewById(R.id.car_price);
            carName = itemView.findViewById(R.id.car_name);
            carKind = itemView.findViewById(R.id.car_kind);
            carTidy = itemView.findViewById(R.id.car_tidy);
            carSeats = itemView.findViewById(R.id.car_seats);
            carTrunk = itemView.findViewById(R.id.car_trunk);
        }
    }
}
