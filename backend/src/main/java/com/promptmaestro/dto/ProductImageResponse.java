package com.promptmaestro.dto;

import java.time.LocalDateTime;
import java.util.List;

public class ProductImageResponse {
    private Long id;
    private String imageUrl;
    private String title;
    private String description;
    private LocalDateTime createdAt;
    private List<String> hashtags;

    public ProductImageResponse() {}

    public ProductImageResponse(Long id, String imageUrl, String title, String description, LocalDateTime createdAt, List<String> hashtags) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
        this.createdAt = createdAt;
        this.hashtags = hashtags;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    public LocalDateTime getCreatedAt() { return createdAt; }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt = createdAt; }
    public List<String> getHashtags() { return hashtags; }
    public void setHashtags(List<String> hashtags) { this.hashtags = hashtags; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String imageUrl;
        private String title;
        private String description;
        private LocalDateTime createdAt;
        private List<String> hashtags;
        public Builder id(Long id) { this.id = id; return this; }
        public Builder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder createdAt(LocalDateTime createdAt) { this.createdAt = createdAt; return this; }
        public Builder hashtags(List<String> hashtags) { this.hashtags = hashtags; return this; }
        public ProductImageResponse build() { return new ProductImageResponse(id, imageUrl, title, description, createdAt, hashtags); }
    }
}
