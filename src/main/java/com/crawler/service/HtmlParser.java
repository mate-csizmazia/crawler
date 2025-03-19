package com.crawler.service;

import com.crawler.pojo.UrlWrapper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlParser {
    //set to false if you want to crawl only the specified domain
    final static boolean INCLUDE_SUBDOMAINS = true;

    private final Logger LOG = Logger.getLogger(HtmlParser.class.getName());
    private final UrlWrapper urlWrapper;
    private HttpURLConnection connection;
    private BufferedReader reader;

    public HtmlParser(UrlWrapper urlWrapper) {
        this.urlWrapper = urlWrapper;
    }

    public Set<String> getLinks(String address) {
        Set<String> links = new HashSet<>();
        Pattern pattern = Pattern.compile(getRegex());

        try {
            URL url = URI.create(address).toURL();
            connection = (HttpURLConnection) url.openConnection();
            reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));

            String line;
            while ((line = reader.readLine()) != null) {
                Matcher matcher = pattern.matcher(line);
                if (matcher.find()) {
                    links.add(matcher.group(1));
                }
            }
        } catch (IOException e) {
            LOG.info("Error while reading HTML: " + e.getMessage());
        } finally {
            cleanup();
        }
        return links;
    }

    private void cleanup() {
        if (reader != null) {
            try {
                reader.close();
            } catch (IOException e) {
               LOG.info("Error while closing reader: " + e.getMessage());
            }
        }
        if (connection != null) {
            connection.disconnect();
        }
    }

    private String getRegex() {
        if (INCLUDE_SUBDOMAINS) {
            return String.format(
                "<a\\s+[^>]*href=[\"'](https://(?:[\\w-]+\\.)*%s\\.%s[^\"']*)[\"'][^>]*>",
                urlWrapper.secondaryLevelDomain(),
                urlWrapper.topLevelDomain()
            );
        } else {
            return String.format(
                "<a\\s+[^>]*href=[\"'](https://%s\\.%s[^\"']*)[\"'][^>]*>",
                urlWrapper.secondaryLevelDomain(),
                urlWrapper.topLevelDomain()
            );
        }
    }
}
