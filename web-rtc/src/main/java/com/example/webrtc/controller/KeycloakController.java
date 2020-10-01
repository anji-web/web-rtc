package com.example.webrtc.controller;

import com.example.webrtc.entity.UserCredentials;
import com.example.webrtc.entity.UserRegister;
import com.example.webrtc.repository.UserCredentialsRepo;
import com.example.webrtc.repository.UserRegisterRepo;
import com.example.webrtc.service.KeycloakService;
import com.example.webrtc.service.UserService;
import org.keycloak.KeycloakPrincipal;
import org.keycloak.adapters.RefreshableKeycloakSecurityContext;
import org.keycloak.representations.AccessToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/keycloak-api")
@CrossOrigin
public class KeycloakController {

    private static final Logger logger = LoggerFactory.getLogger(KeycloakController.class);

    @Autowired
    private KeycloakService keycloakService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRegisterRepo userRegisterRepo;

    @Autowired
    private UserCredentialsRepo userCredentialsRepo;

    @PostMapping("/create")
    public ResponseEntity<?> createUser(@RequestBody UserRegister userRegister){
        try {
            UserRegister newUser = userRegisterRepo.save(userRegister);
            keycloakService.createUserResource(userRegister);
            return new ResponseEntity<UserRegister>(newUser,HttpStatus.CREATED);
        }catch (Exception e){
            logger.error("User cannot created " + e.getMessage());
            return new ResponseEntity<UserRegister>(HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/token")
    public ResponseEntity<?> getTokenUsingCredentials(@RequestBody UserCredentials userCredentials) throws Exception{
        String response = null;
        String username = userCredentials.getUsername();
        String password = userCredentials.getPassword();
        UserCredentials user = null;
        try {
            if (username != null && password != null){
                user = userService.getEmailAndPass(username, password);
                response = keycloakService.getToken(userCredentials);
            }
            if (user == null){
               throw new Exception("Bad credentials");
            }
        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseEntity<>( "Generated token is failed", HttpStatus.BAD_REQUEST);

        }

        return new ResponseEntity<>(response, HttpStatus.OK);

    }

    @GetMapping("/list/{username}")
    public ResponseEntity<UserCredentials> getUserId(@PathVariable String username){
        UserCredentials user = userService.getUserByUsername(username);
        return ResponseEntity.ok().body(user);
    }

}
