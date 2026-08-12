package com.promptmaestro.controller;

import com.promptmaestro.dto.AiHashtagsResponse;
import com.promptmaestro.dto.ProductImageResponse;
import com.promptmaestro.entity.Hashtag;
import com.promptmaestro.entity.ProductImage;
import com.promptmaestro.repository.HashtagRepository;
import com.promptmaestro.repository.ProductImageRepository;
import com.promptmaestro.service.AiService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
public class ProductController {

    private final ProductImageRepository productImageRepository;
    private final HashtagRepository hashtagRepository;
    private final AiService aiService;

    private static final String UPLOAD_DIR = "uploads/";

    public ProductController(ProductImageRepository productImageRepository,
                             HashtagRepository hashtagRepository,
                             AiService aiService) {
        this.productImageRepository = productImageRepository;
        this.hashtagRepository = hashtagRepository;
        this.aiService = aiService;
    }

    @GetMapping("/products")
    public ResponseEntity<List<ProductImageResponse>> getAllProducts() {
        List<ProductImageResponse> products = productImageRepository.findAll().stream()
                .map(this::toResponse)
                .collect(Collectors.toList());
        return ResponseEntity.ok(products);
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<ProductImageResponse>> searchProducts(@RequestParam(required = false) String query) {
        List<ProductImage> results;
        if (query != null && !query.isBlank()) {
            results = productImageRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(query, query);
        } else {
            results = productImageRepository.findAll();
        }
        return ResponseEntity.ok(results.stream().map(this::toResponse).collect(Collectors.toList()));
    }

    @GetMapping("/products/by-hashtags")
    public ResponseEntity<List<ProductImageResponse>> getByHashtags(@RequestParam List<String> tags) {
        List<ProductImage> results = productImageRepository.findByHashtags(tags);
        return ResponseEntity.ok(results.stream().map(this::toResponse).collect(Collectors.toList()));
    }

    @PostMapping("/admin/products/upload")
    public ResponseEntity<AiHashtagsResponse> uploadProduct(
            @RequestParam("image") MultipartFile image,
            @RequestParam(value = "title", required = false) String title,
            @RequestParam(value = "description", required = false) String description) throws IOException {

        String filename = UUID.randomUUID() + "_" + image.getOriginalFilename();
        Path uploadPath = Paths.get(UPLOAD_DIR);
        Files.createDirectories(uploadPath);
        Files.copy(image.getInputStream(), uploadPath.resolve(filename));

        String imageUrl = "/uploads/" + filename;

        List<String> generatedHashtags = aiService.generateHashtags(image);

        ProductImage productImage = ProductImage.builder()
                .imageUrl(imageUrl)
                .title(title)
                .description(description)
                .build();

        for (String tagName : generatedHashtags) {
            Hashtag hashtag = hashtagRepository.findByName(tagName)
                    .orElseGet(() -> hashtagRepository.save(Hashtag.builder().name(tagName).build()));
            productImage.getHashtags().add(hashtag);
        }

        productImageRepository.save(productImage);

        return ResponseEntity.ok(new AiHashtagsResponse(imageUrl, generatedHashtags));
    }

    @DeleteMapping("/admin/products/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        productImageRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private ProductImageResponse toResponse(ProductImage pi) {
        return ProductImageResponse.builder()
                .id(pi.getId())
                .imageUrl(pi.getImageUrl())
                .title(pi.getTitle())
                .description(pi.getDescription())
                .createdAt(pi.getCreatedAt())
                .hashtags(pi.getHashtags().stream().map(Hashtag::getName).collect(Collectors.toList()))
                .build();
    }
}
