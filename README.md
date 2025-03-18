# Java Domain Crawler

## Overview
The Java Domain Crawler is a multi-threaded web crawler designed to traverse and collect URLs from a specified domain. It uses a thread pool to manage concurrent crawling tasks and ensures that all threads complete before returning the results.

## Features
- Multi-threaded crawling with configurable thread pool size
- Dynamic URL matching based on the specified domain
- Graceful shutdown and cleanup of threads
- Logging of crawling activities

## Requirements
- Java 21 or higher
- Maven

## Installation
1. Clone the repository:
    ```sh
    git clone https://github.com/mate-csizmazia/crawler.git
    cd crawler
    ```

2. Build the project using Maven:
    ```sh
    mvn clean install
    ```

## Usage
1. Specify the domain to be crawled in the `Main.java` file:
    ```java
    final String DOMAIN_URL = "orf.at";
    ```

2. Run the application:
    ```sh
    mvn exec:java -Dexec.mainClass="com.crawler.Main"
    ```

3. The application will output the unique URLs found within the specified domain.

## Configuration
- **Thread Pool Size**: Adjust the number of threads in the pool by modifying the `NUM_OF_THREADS` constant in `CrawlerService.java`.
- **Thread Timeout**: Adjust the timeout between thread executions by modifying the `THREAD_TIMEOUT_MS` constant in `CrawlerService.java`.

## Classes

### `Main`
The entry point of the application. It initializes the `UrlMapper` and `CrawlerService`, and starts the crawling process.

### `UrlMapper`
Maps a domain string to a `UrlWrapper` object, which contains the top-level domain, secondary-level domain, and the full URL.

### `UrlWrapper`
A simple POJO that holds the top-level domain, secondary-level domain, and the full URL.

### `CrawlerService`
Manages the crawling process using a thread pool. It handles the crawling logic, URL visiting checks, and thread management.

### `HtmlParser`
Fetches and parses HTML content to extract links based on a dynamic regex pattern that matches the specified domain.

## Example
Here is an example of how to use the crawler:

```java
package com.crawler;

import com.crawler.mapper.UrlMapper;
import com.crawler.pojo.UrlWrapper;
import com.crawler.service.CrawlerService;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        final String DOMAIN_URL = "example.com";

        final UrlMapper mapper = new UrlMapper();
        UrlWrapper urlWrapper = mapper.toUrlWrapper(DOMAIN_URL);
        final CrawlerService crawlerService = new CrawlerService(urlWrapper);
        List<String> result = crawlerService.begin().stream().sorted().toList();

        System.out.printf("Found %d unique URLs\n", result.size());
        result.forEach(System.out::println);
    }
}