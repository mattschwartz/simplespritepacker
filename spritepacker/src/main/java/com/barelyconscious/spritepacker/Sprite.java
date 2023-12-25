package com.barelyconscious.spritepacker;


import lombok.AllArgsConstructor;

import java.awt.image.BufferedImage;

@AllArgsConstructor
class Sprite implements Comparable<Sprite> {

    public String name;
    public int width;
    public int height;

    public BufferedImage sprite;

    private int area() {
        return width * height;
    }

    @Override
    public int compareTo(Sprite rhs) {
        return Integer.compare(area(), rhs.area());
    }
}
