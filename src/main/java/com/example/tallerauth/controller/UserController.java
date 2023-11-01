package com.example.tallerauth.controller;

import com.example.tallerauth.model.dto.LoginUserDTO;
import com.example.tallerauth.model.dto.SignupUserDTO;
import com.example.tallerauth.model.entity.User;
import com.example.tallerauth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@RestController
public class UserController {

    private UserRepository repository;

    @Autowired
    public void setUserRepository(UserRepository repository) {
        this.repository = repository;
    }



    @GetMapping("esp32test")
    public ResponseEntity<?> esp32test(){
        return ResponseEntity.status(200).body(new Date().toString());
    }

    @PostMapping("esp32testPOST")
    public ResponseEntity<?> esp32testPOST(@RequestBody String data){
        System.out.println(data);
        return ResponseEntity.status(200).body(data);
    }



    @GetMapping("users/all")
    public ResponseEntity<?> listUsers() {


        return ResponseEntity.status(200).body(
                repository.findAll()
        );

    }


    @PostMapping("users/create")
    public ResponseEntity<?> createUser(@RequestBody SignupUserDTO user) {
        var usersWithSameUsername = repository.getUserByUsername(user.getName());
        var usersWithSameEmail = repository.getUserByEmail(user.getEmail());
        if (usersWithSameEmail.size() > 0 || usersWithSameUsername.size() > 0) {
            return ResponseEntity.status(400).body("Usuario ya existe en dB");
        } else {
            User userEntity = new User(
                    UUID.randomUUID().toString(),
                    user.getName(), user.getEmail(), user.getPassword());
            repository.save(userEntity);
            return ResponseEntity.status(200).body(user);
        }
    }

    @PostMapping("users/login")
    public ResponseEntity<?> login(@RequestBody LoginUserDTO user) {
        var users = repository.findUserByEmailAndPassword(user.getEmail(), user.getPassword());
        if (users.size() > 0) {
            user.setId(users.get(0).getId());
            return ResponseEntity.status(200).body(user);
        } else {
            return ResponseEntity.status(400).body("Login invalido");
        }
    }


}
