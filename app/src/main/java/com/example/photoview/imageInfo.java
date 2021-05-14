package com.example.photoview;

public class imageInfo {
    private String tags;
    private String webformatURL;
    private String largeImageURL;

    public imageInfo(String tags, String webformatURL, String largeImageURL) {
        super();
        this.tags = tags;
        this.webformatURL = webformatURL;
        this.largeImageURL = largeImageURL;
    }

    public String getTags() {
        return tags;
    }

    public String getWebformatURL() {
        return webformatURL;
    }

    public String getLargeImageURL() {
        return largeImageURL;
    }
}
