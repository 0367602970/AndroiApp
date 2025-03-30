package com.example.tltt_application.Fragment;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.tltt_application.LoginActivity;
import com.example.tltt_application.R;
import com.example.tltt_application.databinding.FragmentAccountBinding;


public class AccountFragment extends Fragment {
    private FragmentAccountBinding binding;

    public AccountFragment() {}
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAccountBinding.inflate(inflater, container, false);

        // Xử lý đăng xuất
        logout();

        return binding.getRoot();
    }

    public void logout() {
        binding.btnLogout.setOnClickListener(v -> {
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("LoginPrefs", getActivity().MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.apply();

            Intent intent = new Intent(getActivity(), LoginActivity.class); // Sử dụng getActivity() thay vì HomeFragment.this
            startActivity(intent);
            getActivity().finish(); // Gọi finish() trên Activity
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // Giải phóng binding
    }
}