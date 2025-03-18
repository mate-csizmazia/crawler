package com.crawler.service;

import com.crawler.pojo.UrlWrapper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public class CrawlerService {
    private static final Logger LOG = Logger.getLogger(CrawlerService.class.getName());

    //fine tune crawling with these values
    private static final int NUM_OF_THREADS = 64;
    private static final int THREAD_TIMEOUT_MS = 300;

    private final UrlWrapper urlWrapper;
    private final ExecutorService executor ;
    private final Set<String> allUrls = ConcurrentHashMap.newKeySet();
    private final List<String> visitedUrls = Collections.synchronizedList(new ArrayList<>());

    public CrawlerService(UrlWrapper urlWrapper) {
        this.urlWrapper = urlWrapper;
        executor = Executors.newFixedThreadPool(NUM_OF_THREADS);
    }

    public Set<String> begin() {
        crawl(urlWrapper.url());
        cleanup();
        return allUrls;
    }

    private void crawl(String url) {
        final HtmlParser htmlParser = new HtmlParser(urlWrapper);
        System.out.println("Crawling in my skin: " + url + " by thread: " + Thread.currentThread().getName());

        if (!shouldVisit(url)) {
            return;
        }

        handleUpdate(url);

        htmlParser.getLinks(url).forEach(link -> {
            if(shouldVisit(link)) {
                //small timeout helps with not getting timeout errors
                try {
                    Thread.sleep(THREAD_TIMEOUT_MS);
                } catch (InterruptedException e) {
                    LOG.warning("Thread sleep interrupted: " + e.getMessage());
                }
                executor.submit(() -> crawl(link));
            }
        });
    }

    private void cleanup() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    private synchronized void handleUpdate(String url) {
        visitedUrls.add(url);
        allUrls.add(url);
    }

    private synchronized boolean shouldVisit(String url) {
        return !visitedUrls.contains(url);
    }
}
