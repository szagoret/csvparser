package com.csvparser.gui;

public enum FileStatus {

    INIT("/images/file_inactive.png"),
    READY_FOR_PARSE("/images/ready_to_parse.png"),
    READY_FOR_WS("/images/ready_to_ws.png"),
    IN_PROGRESS("/images/loading.gif"),
    PROCESSED("/images/done.png"),
    ERROR("/images/error.png");

    private final String image;

    FileStatus(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }
}