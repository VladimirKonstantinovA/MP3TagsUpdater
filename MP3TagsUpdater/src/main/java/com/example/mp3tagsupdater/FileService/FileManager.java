package com.example.mp3tagsupdater.FileService;

import com.example.mp3tagsupdater.FileService.FileUpdater.FileUpdaterManager;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class FileManager {
    private  ArrayList<File> files = new ArrayList<>();
    private String[] args;

    public FileManager(String[] args) {
        this.args = args;
    }

    private File[] getFiles(String path, String extensionFilter) {
        Pattern pattern = Pattern.compile(extensionFilter);

        FilenameFilter filenameFilter = (dir, name) -> {
            String fullPath = dir.toString() + "/" + name;
            File file = new File(fullPath);
            if (file.isDirectory()) {
                File[] findFiles = getFiles(file.getPath(), extensionFilter);
                files.addAll(Arrays.stream(findFiles).collect(Collectors.toCollection(ArrayList::new)));
                return false;
            }

            Matcher matcher = pattern.matcher(fullPath);
            boolean math = matcher.find();
            return math;
        };
        File[] findFiles = new File(path).listFiles(filenameFilter);
        return findFiles;
    }

    private void runSearchFile(String path, String extensionFilter) {
        File[] findFiles = getFiles(path, extensionFilter);
        files.addAll(Arrays.stream(findFiles).collect(Collectors.toCollection(ArrayList::new)));
    }

    public void run() {
        switch (args.length) {
            case 0:
                System.out.println("No args");
                return;
            case 1:
                System.out.println("No extension filter for sound");
                return;
            case 2:
                System.out.println("No extension filter for image");
                return;

            default:
                break;
        }

        runSearchFile(args[0], args[2]);
        ArrayList<File> imageFiles = new ArrayList<>(files);

        files.clear();
        runSearchFile(args[0], args[1]);
        ArrayList<File> soundFiles = new ArrayList<>(files);

        for (File file : soundFiles) {
            String filePath = file.getParent();
            File imageFile = imageFiles.stream().filter(soundFile -> soundFile.getParent().equalsIgnoreCase(filePath)).findFirst().orElse(null);
            String imagePath = "";
            if (!(imageFile == null)) {
                imagePath = imageFile.getPath();
            }
            FileUpdaterManager fileUpdaterManager = new FileUpdaterManager(file, imagePath);
            fileUpdaterManager.updateFile();
        }
    }
}
