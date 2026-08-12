package com.promptmaestro.dto;

import java.util.List;

public class AiHashtagsResponse {
    private String imageUrl;
    private List<String> hashtags;

    public AiHashtagsResponse() {}

    public AiHashtagsResponse(String imageUrl, List<String> hashtags) {
        this.imageUrl = imageUrl;
        this.hashtags = hashtags;
    }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public List<String> getHashtags() { return hashtags; }
    public void setHashtags(List<String> hashtags) { this.hashtags = hashtags; }
}
