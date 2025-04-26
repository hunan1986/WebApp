package com.example.config.properties;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "application")
public class WebAppProperties {

    private Bcrypt bcrypt;
    private String specialCharacters;

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
