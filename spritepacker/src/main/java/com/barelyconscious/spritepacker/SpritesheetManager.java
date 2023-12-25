package com.barelyconscious.spritepacker;

import com.google.gson.Gson;
import lombok.AllArgsConstructor;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SpritesheetManager {

    private final Gson gson = new Gson();

    public void saveSpritesheetLookup(List<PackedSprite> sprites, String spritesheetFilepath) throws IOException {
        List<SourceSprite> sourceSprites = new ArrayList<>();
        for (var sprite : sprites) {
            sourceSprites.add(new SourceSprite(
                sprite.name,
                new Frame(sprite.x, sprite.y, sprite.width, sprite.height),
                false,
                false,
                new SpriteSourceSize(sprite.x, sprite.y, sprite.width, sprite.height),
                new SourceSize(sprite.width, sprite.height),
                new Pivot(0.5, 0.5)
            ));

            try (BufferedWriter writer = new BufferedWriter(new FileWriter(
                spritesheetFilepath.replace(".png", ".json")))) {
                writer.write(gson.toJson(sourceSprites));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @AllArgsConstructor
    private static class SourceSprite {
        private final String filename;
        private final Frame frame;
        private final boolean rotated;
        private final boolean trimmed;
        private final SpriteSourceSize spriteSourceSize;
        private final SourceSize sourceSize;
        private final Pivot pivot;
    }

    @AllArgsConstructor
    private static class Frame {
        private final int x;
        private final int y;
        private final int w;
        private final int h;
    }

    @AllArgsConstructor
    private static class SpriteSourceSize {
        private final int x;
        private final int y;
        private final int w;
        private final int h;
    }

    @AllArgsConstructor
    private static class SourceSize {
        private final int w;
        private final int h;
    }

    @AllArgsConstructor
    private static class Pivot {
        private final double x;
        private final double y;
    }
}
