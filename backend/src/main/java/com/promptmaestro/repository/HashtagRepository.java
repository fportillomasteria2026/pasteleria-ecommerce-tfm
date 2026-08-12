package com.promptmaestro.repository;

import com.promptmaestro.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
    Optional<Hashtag> findByName(String name);
    boolean existsByName(String name);
}
