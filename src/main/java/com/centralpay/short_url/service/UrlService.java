package com.centralpay.short_url.service;


import com.centralpay.short_url.exception.UrlNotFoundException;
import com.centralpay.short_url.model.Url;
import com.centralpay.short_url.repository.UrlRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class UrlService {

    @Autowired
    private UrlRepository urlRepository;

    public String shortenUrl(String originalUrl) {
        /*** Method to generate and store a shortened URL for the given original URL ***/
        try {
            Url url = new Url(originalUrl, generateShortUrl());
            urlRepository.save(url);
            return url.getShortUrl();
        } catch (Exception e) {
            throw new RuntimeException("Error while creating the short URL: " + e.getMessage());
        }
    }

    public String getOriginalUrl(String shortUrl) {
        /*** Method to retrieve the original URL using the shortened URL ***/
        Url url = urlRepository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new UrlNotFoundException("Short URL not found: " + shortUrl));
        url.setVisitCount(url.getVisitCount() + 1);
        urlRepository.save(url);
        return url.getOriginalUrl();
    }

    private String generateShortUrl() {
        /*** Method to generate a unique shortened URL ***/
        try {
            String shortUrl;
            do {
                shortUrl = UUID.randomUUID().toString().substring(0, 8);
            } while (urlRepository.findByShortUrl(shortUrl).isPresent());
            return shortUrl;
        } catch (Exception e) {
            throw new RuntimeException("Error while generating the short URL: " + e.getMessage());
        }
    }

    public int getVisitCount(String shortUrl) {
        /*** Method to get the visit count for a given shortened URL ***/
        Url url = urlRepository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new UrlNotFoundException("Short URL not found: " + shortUrl));
        return url.getVisitCount();
    }

    public List<String> getShortUrlsForOriginalUrl(String originalUrl) {
        /*** Method to get all shortened URLs associated with a given original URL ***/
        List<String> shortUrls = urlRepository.findByOriginalUrl(originalUrl)
                .stream()
                .map(Url::getShortUrl)
                .collect(Collectors.toList());

        if (shortUrls.isEmpty()) {
            throw new UrlNotFoundException("No short URLs found for the original URL: " + originalUrl);
        }

        return shortUrls;
    }

    public Url getUrlDetails(String shortUrl) {
        /*** Method to get the details of a URL  ***/
        return urlRepository.findByShortUrl(shortUrl)
                .orElseThrow(() -> new UrlNotFoundException("Short URL not found: " + shortUrl));
    }
}