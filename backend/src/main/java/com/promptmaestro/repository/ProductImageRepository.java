package com.promptmaestro.repository;

import com.promptmaestro.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {

    @Query("SELECT DISTINCT pi FROM ProductImage pi JOIN pi.hashtags h WHERE h.name IN :hashtags")
    List<ProductImage> findByHashtags(@Param("hashtags") List<String> hashtags);

    List<ProductImage> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(String title, String description);
}
