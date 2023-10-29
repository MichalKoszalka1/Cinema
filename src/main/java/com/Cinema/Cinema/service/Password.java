package com.Cinema.Cinema.service;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class Password {
    public static void main(String[]args){
//        Create BCryptPasswordEncoder Test
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        String rawPassword = "Blath1232";
        String encoderPassword = encoder.encode(rawPassword);
        System.out.println(encoderPassword);
    }
}
