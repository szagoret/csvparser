package com.csvparser.util;

import javax.swing.*;

public class ImageUtil {

    public static ImageIcon createImageIcon(String path) {
        java.net.URL imgURL = ImageUtil.class.getResource(path);
        if (imgURL != null) {
            return new ImageIcon(imgURL);
        } else {
            System.err.println("Couldn't find file: " + path);
            return null;
        }
    }
}
