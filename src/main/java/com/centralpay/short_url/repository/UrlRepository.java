package com.centralpay.short_url.repository;

import com.centralpay.short_url.model.Url;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UrlRepository extends JpaRepository<Url, Long> {
    Optional<Url> findByShortUrl(String shortUrl);
    List<Url> findByOriginalUrl(String originalUrl);
}
