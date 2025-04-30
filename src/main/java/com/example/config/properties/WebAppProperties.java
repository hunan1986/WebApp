package com.example.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@ConfigurationProperties(prefix = "application")
public class WebAppProperties {

    private Bcrypt bcrypt;
    private String specialCharacters;

    @Bean
    public PasswordEncoder getPasswordEncoder() {
        return new BCryptPasswordEncoder(getBcrypt().getStrength());
    }

    public Bcrypt getBcrypt() {
        return bcrypt;
    }

    public void setBcrypt(Bcrypt bcrypt) {
        this.bcrypt = bcrypt;
    }

    public String getSpecialCharacters() {
        return specialCharacters;
    }

    public void setSpecialCharacters(String specialCharacters) {
        this.specialCharacters = specialCharacters;
    }

    public static class Bcrypt {
        private int strength;

        public int getStrength() {
            return strength;
        }

        public void setStrength(int strength) {
            this.strength = strength;
        }
    }
}
