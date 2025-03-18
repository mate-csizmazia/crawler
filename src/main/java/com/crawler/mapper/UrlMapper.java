package com.crawler.mapper;

import com.crawler.pojo.UrlWrapper;

public class UrlMapper {

    public final static String URL_PREFIX = "https://www.";

    public UrlMapper(){}

    public UrlWrapper toUrlWrapper(String domain) {
        String[] domainParts = domain.split("\\.");

        if(domainParts.length < 2) {
            throw new IllegalArgumentException("Domain must have at least two parts");
        }

        String topLevelDomain = domainParts[domainParts.length - 1];
        String secondaryLevelDomain = domainParts[domainParts.length - 2];

        return new UrlWrapper(topLevelDomain, secondaryLevelDomain, URL_PREFIX + domain);
    }


}
