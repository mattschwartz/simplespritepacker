package com.barelyconscious.spritepacker;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Main {

    private static final SpritesheetManager SPRITESHEET_MANAGER = new SpritesheetManager();

    public static void main(String[] args) throws IOException {
        String imagesRootDir = "/Users/matt/Documents/GitHub/artwork/worlds/";
        String spritesheetName = "gui";
        String imagesFilepath = imagesRootDir + spritesheetName + "/";
        String spritesheetFilepath = "/Users/matt/Documents/GitHub/stonequest-original/StoneQuest/res/spritesheets/" + spritesheetName + "_spritesheet.png";
        System.out.println("Packing sprites at " + imagesFilepath);

        List<Sprite> sprites = loadSprites(imagesFilepath);
        PackSpritesResult packedSprites = packSprites(sprites);
        saveSpritesheet(
            packedSprites.spritesheetWidth,
            packedSprites.spritesheetHeight,
            packedSprites.sprites,
            spritesheetFilepath);
    }

    static PackSpritesResult packSprites(List<Sprite> sprites) {
        int spritesheetWidth = 256;
        int spritesheetHeight = 0;

        // sort by tallest first
        sprites = sprites.stream().sorted((a, b) -> Integer.compare(b.height, a.height)).toList();
        int maxRowHeight = sprites.get(0).height;

        int maxWidth = sprites.stream().max((a, b) -> Integer.compare(a.width, b.width)).get()
            .width;

        if (maxWidth > spritesheetWidth) {
            spritesheetWidth = maxWidth;
        }
        spritesheetHeight = sprites.get(0).height;

        List<PackedSprite> result = new ArrayList<>();

        int x = 0;
        int y = 0;
        for (var sprite : sprites) {
            boolean isPacked = false;
            while (!isPacked) {
                if (x + sprite.width > spritesheetWidth) {
                    x = 0;
                    y += maxRowHeight;
                    spritesheetHeight += maxRowHeight;
                    maxRowHeight = sprite.height;
                }
                result.add(new PackedSprite(sprite.name, x, y, sprite.width, sprite.height, sprite.sprite));
                x += sprite.width;
                isPacked = true;
            }
        }

        return new PackSpritesResult(spritesheetWidth, spritesheetHeight, result);

    }

    static List<Sprite> loadSprites(String imagesFilepath) throws IOException {
        List<Sprite> sprites = new ArrayList<>();
        try (DirectoryStream<Path> stream = Files.newDirectoryStream(Path.of(imagesFilepath))) {
            for (Path entry : stream) {
                if (Files.isDirectory(entry)
                    || !entry.getFileName().toString().endsWith(".png")
                    || entry.getFileName().toString().endsWith("spritesheet.png")
                ) {
                    continue;
                }

                System.out.println("Loading " + entry.getFileName().toString().replace(".png", ""));
                BufferedImage image = ImageIO.read(entry.toFile());
                sprites.add(new Sprite(
                    entry.getFileName().toString().replace(".png", ""),
                    image.getWidth(),
                    image.getHeight(),
                    image));
            }
        }

        return sprites;
    }

    static void saveSpritesheet(
        int width,
        int height,
        List<PackedSprite> sprites,
        String spritesheetFilepath
    ) throws IOException {
        BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g = outputImage.createGraphics();
        for (var sprite : sprites) {
            g.drawImage(sprite.sprite, sprite.x, sprite.y, null);
        }

        SPRITESHEET_MANAGER.saveSpritesheetLookup(sprites, spritesheetFilepath);

        System.out.println("Saving spritesheet to " + spritesheetFilepath);
        ImageIO.write(outputImage, "png", new File(spritesheetFilepath));
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////
    // TYPES
    ////////////////////////////////////////////////////////////////////////////////////////////////


    static class PackSpritesResult {
        public int spritesheetWidth;
        public int spritesheetHeight;
        public List<PackedSprite> sprites;

        public PackSpritesResult(int spritesheetWidth, int spritesheetHeight, List<PackedSprite> sprites) {
            this.spritesheetWidth = spritesheetWidth;
            this.spritesheetHeight = spritesheetHeight;
            this.sprites = sprites;
        }
    }
}
