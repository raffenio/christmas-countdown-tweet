package com.raffenio.christmascountdown.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.v1.Status;

@Service
public class TwitterService {

    private static final Logger log = LoggerFactory.getLogger(TwitterService.class);

    private final Twitter twitter;

    public TwitterService(Twitter twitter) {
        this.twitter = twitter;
    }

    public void postTweet(String message) {
        try {
            Status status = twitter.v1().tweets().updateStatus(message);
            log.info("Tweet publicado correctamente. ID: {}", status.getId());
            log.info("Contenido: {}", status.getText());
        } catch (TwitterException e) {
            log.error("Error al publicar el tweet: {} (código {})", e.getMessage(), e.getStatusCode(), e);
        }
    }
}
