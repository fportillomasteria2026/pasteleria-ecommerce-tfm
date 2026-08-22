package com.promptmaestro.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "product_image")
public class ProductImage {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String imageUrl;

    private String title;

    @Column(length = 1000)
    private String description;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
        name = "product_image_hashtag",
        joinColumns = @JoinColumn(name = "product_image_id"),
        inverseJoinColumns = @JoinColumn(name = "hashtag_id")
    )
    private Set<Hashtag> hashtags = new HashSet<>();

    public ProductImage() {}

    public ProductImage(Long id, String imageUrl, String title, String description, Set<Hashtag> hashtags) {
        this.id = id;
        this.imageUrl = imageUrl;
        this.title = title;
        this.description = description;
        this.hashtags = hashtags != null ? hashtags : new HashSet<>();
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
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
    public Set<Hashtag> getHashtags() { return hashtags; }
    public void setHashtags(Set<Hashtag> hashtags) { this.hashtags = hashtags; }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private Long id;
        private String imageUrl;
        private String title;
        private String description;
        private Set<Hashtag> hashtags;
        public Builder id(Long id) { this.id = id; return this; }
        public Builder imageUrl(String imageUrl) { this.imageUrl = imageUrl; return this; }
        public Builder title(String title) { this.title = title; return this; }
        public Builder description(String description) { this.description = description; return this; }
        public Builder hashtags(Set<Hashtag> hashtags) { this.hashtags = hashtags; return this; }
        public ProductImage build() { return new ProductImage(id, imageUrl, title, description, hashtags); }
    }
}
