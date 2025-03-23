package com.centralpay.short_url.controller;

import com.centralpay.short_url.exception.ErrorResponse;
import com.centralpay.short_url.exception.UrlNotFoundException;
import com.centralpay.short_url.model.Url;
import com.centralpay.short_url.service.UrlService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api")
public class UrlController {

    @Autowired
    private UrlService urlService;

    /**
     * Creates a short URL from an original URL.
     *
     * @param request a dict containing the original URL to be shortened
     * @return A ResponseEntity containing the generated short URL or an error message
     */
    @PostMapping("/shorten")
    public ResponseEntity<?> shortenUrl(@RequestBody Map<String, String> request) {
        try {
            String originalUrl = request.get("originalUrl");
            if (originalUrl == null || originalUrl.isEmpty()) {
                return ResponseEntity.badRequest().body(new ErrorResponse("400", "The original URL is required"));
            }
            String shortUrl = urlService.shortenUrl(originalUrl);
            return ResponseEntity.ok(shortUrl);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("500", "An error occurred while shortening the URL: " + e.getMessage()));
        }
    }

    /**
     * Redirects a short URL to its original URL.
     *
     * @param shortUrl The short URL to be redirected
     * @return A ResponseEntity with a redirection to the original URL or an error message
     */
    @GetMapping("/{shortUrl}")
    public ResponseEntity<?> redirectToOriginalUrl(@PathVariable String shortUrl) {
        try {
            String originalUrl = urlService.getOriginalUrl(shortUrl);
            return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(originalUrl)).build();
        } catch (UrlNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("404", ex.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("500", "An error occurred while redirecting: " + e.getMessage()));
        }
    }

    /**
     * Retrieves all short URLs associated with a given original URL.
     *
     * @param originalUrl The original URL
     * @return A ResponseEntity containing a list of short URLs or an error message
     */
    @GetMapping("/shorturls")
    public ResponseEntity<?> getShortUrlsForOriginalUrl(@RequestParam String originalUrl) {
        try {
            List<String> shortUrls = urlService.getShortUrlsForOriginalUrl(originalUrl);
            if (shortUrls.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body(new ErrorResponse("404", "No short URLs found for the original URL: " + originalUrl));
            }

            return ResponseEntity.ok(shortUrls);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("500", "An error occurred while retrieving short URLs: " + e.getMessage()));
        }
    }

    /**
     * Retrieves the visit count for a specific short URL.
     *
     * @param shortUrl The short URL
     * @return A ResponseEntity containing the visit count or an error message
     */
    @GetMapping("/visitCount/{shortUrl}")
    public ResponseEntity<?> getVisitCount(@PathVariable String shortUrl) {
        try {
            int visitCount = urlService.getVisitCount(shortUrl);
            return ResponseEntity.ok(visitCount);
        } catch (UrlNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("404", ex.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("500", "An error occurred while retrieving visit count: " + e.getMessage()));
        }
    }

    /**
     * Retrieves details of a short URL, including the original URL and visit count.
     *
     * @param shortUrl The short URL
     * @return A ResponseEntity containing the URL details or an error message
     */
    @GetMapping("/details/{shortUrl}")
    public ResponseEntity<?> getUrlDetails(@PathVariable String shortUrl) {
        try {
            Url url = urlService.getUrlDetails(shortUrl);
            return ResponseEntity.ok(url);
        } catch (UrlNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new ErrorResponse("404", ex.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("500", "An error occurred while retrieving URL details: " + e.getMessage()));
        }
    }

    /**
     * Handles unexpected exceptions and returns a generic error response.
     *
     * @param ex The exception that occurred
     * @return A ResponseEntity containing a generic error message
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGenericException(Exception ex) {
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new ErrorResponse("500", "An unexpected error occurred: " + ex.getMessage()));
    }
}