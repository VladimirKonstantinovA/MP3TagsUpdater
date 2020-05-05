package com.example.mp3tagsupdater.FileService.FileUpdater;

import com.example.mp3tagsupdater.MP3Manager;
import org.jaudiotagger.audio.mp3.MP3File;
import java.io.File;

public class FileUpdaterManager {

    private File file;
    private String imagePath;

    public FileUpdaterManager(File file, String imagePath) {
        this.file = file;
        this.imagePath = imagePath;
    }

    public void updateFile() {
        String filePath = file.getPath();
        String originalFileName = filePath.substring(0, filePath.lastIndexOf(".")) + ".original" + filePath.substring(filePath.lastIndexOf("."));
        MP3File mp3File = MP3Manager.getMP3File(file);
        if (mp3File == null) return;
        MP3Manager.setText(mp3File, filePath);
        MP3Manager.setCover(mp3File, imagePath);

        try {
            mp3File.save(file);
            File originalFile = new File(originalFileName);
            if (originalFile.exists()) {
                if (!(originalFile.delete())) System.out.println("Error: unable delete original file <" + originalFile.getPath() + "> ");
            }

        } catch (Exception ex) {
            System.out.println("Error update " + filePath + " : " + ex.getMessage());
        }

    }
}
