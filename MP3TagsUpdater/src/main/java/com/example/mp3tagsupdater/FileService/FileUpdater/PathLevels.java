package com.example.mp3tagsupdater.FileService.FileUpdater;

public enum  PathLevels {
    Album(3),
    Artist(2),
    Song(1);

    final int level;

    public int getLevel() {
        return level;
    }

    PathLevels(int level) {
        this.level = level;
    }
}
