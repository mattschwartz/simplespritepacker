package com.barelyconscious.spritepacker;

import lombok.AllArgsConstructor;

import java.awt.image.BufferedImage;

@AllArgsConstructor
public class PackedSprite {
    public String name;
    public int x;
    public int y;
    public int width;
    public int height;
    public BufferedImage sprite;
}
