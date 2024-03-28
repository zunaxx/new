package com.example.bilingualb10;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@SpringBootApplication
@Controller
public class BilingualB10Application {

    public static void main(String[] args) {
        SpringApplication.run(BilingualB10Application.class, args);
    }

    @GetMapping
    public String greetings(){
        return "index";
    }
}