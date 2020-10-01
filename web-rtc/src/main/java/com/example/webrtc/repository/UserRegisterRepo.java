package com.example.webrtc.repository;

import com.example.webrtc.entity.UserRegister;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRegisterRepo extends JpaRepository<UserRegister, Long> {
    @Query(value = "select * from user_register", nativeQuery = true)
    List<UserRegister> listDataUser(UserRegister userRegister);
}
