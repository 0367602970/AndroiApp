package com.example.tltt_application.viewmodels;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.tltt_application.repository.AuthRepo;

public class AuthViewModel extends ViewModel {
    private AuthRepo repository;
    private MutableLiveData<AuthResult> authResult;

    public AuthViewModel() {
        repository = new AuthRepo();
        authResult = new MutableLiveData<>();
    }

    public LiveData<AuthResult> getAuthResult() {
        return authResult;
    }

    public void register(String name, String phone, String password) {
        repository.registerUser(name, phone, password, (success, message) -> {
            authResult.postValue(new AuthResult(success, message));
        });
    }

    public void login(String phone, String password) {
        repository.loginUser(phone, password, (success, message) -> {
            authResult.postValue(new AuthResult(success, message));
        });
    }

    public static class AuthResult {
        private boolean success;
        private String message;

        public AuthResult(boolean success, String message) {
            this.success = success;
            this.message = message;
        }

        public boolean isSuccess() {
            return success;
        }

        public String getMessage() {
            return message;
        }
    }
}
