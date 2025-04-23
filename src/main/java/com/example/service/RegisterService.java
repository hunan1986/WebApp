package com.example.service;

import com.example.model.Users;
import com.example.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class RegisterService {

    @Autowired
    private UserRepo repo;

    PasswordEncoder passwordEncoder;

    public String registerHandler(String username, String password, String confirm_password) {

        Users user = repo.findByUsername(username);

        if (username.isEmpty()) {
            return "null_username";
        }
        if (password.isEmpty()) {
            return "null_password";
        }
        if (confirm_password.isEmpty()) {
            return "null_confirm_password";
        }
        if (user != null) {
            return "exists";
        }
        if (!username.matches("[a-zA-Z0-9]*")) {
            return "num_lett";
        }
        if (password.length() < 8) {
            return "pass_not_8char";
        }
        //todo move specialCharacters to property file
        String specialCharacters = " !#$%&'()*+,-./:;<=>?@[]^_`{|}~";
        String str2[] = password.split("");
        boolean pass_check = false;
        for (String s : str2) {
            if (specialCharacters.contains(s)) {
                pass_check = true;
                break;
            }
        }
        if (!(password.matches(".*[A-Z].*") && password.matches(".*[a-z].*") && password.matches(".*[0-9].*") && pass_check)) {
            return "pass_cont_issue";
        }
        if (!password.equals(confirm_password)) {
            return "pass_not_match";
        }

        this.passwordEncoder = new BCryptPasswordEncoder(12);
        int user_id = repo.getMaxId() + 1;
        String bcrypt_password = this.passwordEncoder.encode(password);
        Users userNew = new Users(user_id, username, bcrypt_password);
        repo.save(userNew);

        return "pass";
    }
}
