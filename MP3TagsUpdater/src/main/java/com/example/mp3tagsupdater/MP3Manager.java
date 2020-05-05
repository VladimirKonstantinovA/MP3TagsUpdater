package com.example.mp3tagsupdater;

import com.example.mp3tagsupdater.FileService.FileUpdater.PathLevels;
import com.example.mp3tagsupdater.FileService.ImageProperty;
import org.jaudiotagger.audio.exceptions.InvalidAudioFrameException;
import org.jaudiotagger.audio.exceptions.ReadOnlyFileException;
import org.jaudiotagger.audio.mp3.MP3File;
import org.jaudiotagger.tag.FieldDataInvalidException;
import org.jaudiotagger.tag.TagException;
import org.jaudiotagger.tag.datatype.Artwork;
import org.jaudiotagger.tag.id3.AbstractID3v2Tag;
import org.jaudiotagger.tag.id3.ID3v1Tag;
import org.jaudiotagger.tag.id3.ID3v23Tag;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.regex.Pattern;

public class MP3Manager {

    public static MP3File getMP3File(File file) {
        try {
            return new MP3File(file);
        }
        catch (IOException | TagException | ReadOnlyFileException | InvalidAudioFrameException ex) {
            System.out.println("Error: unable get MP3 file from file <" + file.getPath() + ">^: " + ex.getMessage());
            return null;
        }
    }

    private static String getInfoForTag(String path, PathLevels tagLevel) {
        String[] pathLevels = path.split(Pattern.quote(File.separator));
        if (tagLevel.getLevel() > pathLevels.length) {
            return "";
        }
        return pathLevels[pathLevels.length - tagLevel.getLevel()];
    }

    public static void setText(MP3File mp3File, String filePath) {
            ID3v1Tag tags_v1;
            if (mp3File.hasID3v1Tag()) {
                tags_v1 = mp3File.getID3v1Tag();
            } else {
                tags_v1 = new ID3v1Tag();
            }
            String songName = getInfoForTag(filePath, PathLevels.Song);
            songName = songName.substring(0, songName.lastIndexOf("."));
            tags_v1.setTitle(songName);
            tags_v1.setArtist(getInfoForTag(filePath, PathLevels.Artist));
            tags_v1.setAlbum(getInfoForTag(filePath, PathLevels.Album));
            mp3File.setID3v1Tag(tags_v1);

    }

    public static void setCover(MP3File mp3File, String imagePath) {
            AbstractID3v2Tag v2tag = mp3File.getID3v2Tag();
            ID3v23Tag tags_v23;
            if (v2tag instanceof ID3v23Tag) {
                tags_v23 = (ID3v23Tag) v2tag;
            }
            else {
                tags_v23 =  new ID3v23Tag(v2tag);
            }
        if (!(imagePath.isBlank())) {
            File resizedFile = null;
            BufferedImage image;
            try {
                image = ImageIO.read(new File(imagePath));
            } catch (IOException ex) {
                System.out.println("Error: unable get image from file <" + imagePath + ">^: " + ex.getMessage());
                return;
            }
            if (image.getWidth()> ImageProperty.WIDTH || image.getHeight()> ImageProperty.HEIGHT) {
                BufferedImage scaledBI = new BufferedImage(ImageProperty.WIDTH, ImageProperty.HEIGHT, BufferedImage.TYPE_INT_RGB);
                Graphics2D graphics2D = scaledBI.createGraphics();
                graphics2D.setComposite(AlphaComposite.Src);
                graphics2D.drawImage(image, 0, 0, ImageProperty.WIDTH, ImageProperty.HEIGHT,null);
                graphics2D.dispose();
                String resizedName = imagePath.substring(0, imagePath.lastIndexOf(".")) + ImageProperty.POSTFIX + imagePath.substring(imagePath.lastIndexOf("."));
                resizedFile = new File(resizedName);
                try {
                    ImageIO.write(scaledBI, ImageProperty.FORMAT, resizedFile);
                } catch (IOException ex) {
                    System.out.println("Error: unable save resized image to file <" + resizedFile + ">^: " + ex.getMessage());
                    return;
                }
            }
            Artwork artwork = null;
            try {
                artwork = Artwork.createArtworkFromFile(resizedFile);
            } catch (IOException ex) {
                System.out.println("Error: unable create artwork from file <" + resizedFile + ">^: " + ex.getMessage());
            }
            try {
                tags_v23.addField(artwork);
            } catch (FieldDataInvalidException ex) {
                System.out.println("Error: unable add field to tag: " + ex.getMessage());
                return;
            }
            if ((resizedFile!= null) && resizedFile.exists()) {
                if (!(resizedFile.delete())) System.out.println("Error: unable delete resized image <" + resizedFile.getPath() + ">");
            }
        }
        mp3File.setTag(tags_v23);

    }
}
