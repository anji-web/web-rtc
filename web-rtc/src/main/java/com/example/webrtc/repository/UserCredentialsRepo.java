package com.example.webrtc.repository;

import com.example.webrtc.entity.UserCredentials;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserCredentialsRepo extends JpaRepository<UserCredentials, Long> {
    @Query(
            value = "SELECT u.username FROM user_credentials u WHERE u.username = ?1", nativeQuery = true)
    UserCredentials findByEmailAndPassword(String username);

    public UserCredentials findByUsernameAndPassword(String username, String password);
}
