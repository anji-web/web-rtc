package com.example.webrtc.service;

import com.example.webrtc.entity.UserCredentials;
import com.example.webrtc.repository.UserCredentialsRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private UserCredentialsRepo userCredentialsRepo;

    @Autowired
    private EntityManager entityManager;
//
//    public List<UserCredentials> getUserEmailAndPass(UserCredentials user){
//        String query = "SELECT email, password, usercredentials_id FROM user_credentials WHERE usercredentials_id = :usercredentials_id AND password = :password";
//        return (List<UserCredentials>) entityManager.createQuery(query)
//                    .setParameter("usercredentials_id", user.getId())
//                    .setParameter("password", user.getPassword())
//                    .getResultList();
//    }

    @Transactional
    public UserCredentials getUserByUsername(String username){

        return userCredentialsRepo.findByEmailAndPassword(username);
    }

    public UserCredentials getEmailAndPass(String username, String password){
        return userCredentialsRepo.findByUsernameAndPassword(username, password);
    }


}
