package com.example.tltt_application.DAO;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import com.example.tltt_application.objects.User;

@Dao
public interface UserDao {
    @Insert
    long insert(User user);

    @Query("SELECT * FROM users WHERE phoneNumber = :phoneNumber LIMIT 1")
    User getUserByPhoneNumber(String phoneNumber);
}
