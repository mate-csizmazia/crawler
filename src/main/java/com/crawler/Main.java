package com.crawler;

import com.crawler.mapper.UrlMapper;
import com.crawler.pojo.UrlWrapper;
import com.crawler.service.CrawlerService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        //specify domain to be crawled
        final String DOMAIN_URL = "orf.at";

        final UrlMapper mapper = new UrlMapper();

        UrlWrapper urlWrapper = mapper.toUrlWrapper(DOMAIN_URL);
        final CrawlerService crawlerService = new CrawlerService(urlWrapper);
        List<String> result = crawlerService.begin().stream().sorted().toList();

        System.out.printf("Found %d unique URLs\n", result.size());
        result.forEach(System.out::println);
    }
}