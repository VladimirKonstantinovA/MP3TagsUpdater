package com.example.mp3tagsupdater;

import com.example.mp3tagsupdater.FileService.FileManager;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MP3TagsUpdaterApplication {

    public static void main(String[] args) {
        SpringApplication.run(MP3TagsUpdaterApplication.class, args);
        FileManager fileManager = new FileManager(args);
        fileManager.run();
    }

}
