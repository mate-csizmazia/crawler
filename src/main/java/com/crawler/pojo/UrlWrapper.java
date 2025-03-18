package com.crawler.pojo;

public record UrlWrapper (
        String topLevelDomain,
        String secondaryLevelDomain,
        String url
) {
    public UrlWrapper {
        if (topLevelDomain == null || secondaryLevelDomain == null || url == null) {
            throw new IllegalArgumentException("All fields must be non-null");
        }
    }
}
