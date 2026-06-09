package com.raffenio.christmascountdown.scheduler;

import com.raffenio.christmascountdown.service.CountdownService;
import com.raffenio.christmascountdown.service.TwitterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@ConditionalOnProperty(name = "tweet.schedule.enabled", havingValue = "true")
public class TweetScheduler {

    private static final Logger log = LoggerFactory.getLogger(TweetScheduler.class);

    private final CountdownService countdownService;
    private final TwitterService twitterService;

    public TweetScheduler(CountdownService countdownService, TwitterService twitterService) {
        this.countdownService = countdownService;
        this.twitterService = twitterService;
    }

    // Ejecuta todos los días a las 3:00 AM (zona horaria configurada en application.properties)
    @Scheduled(cron = "${tweet.schedule.cron:0 0 3 * * *}", zone = "${tweet.schedule.timezone:America/Mexico_City}")
    public void sendDailyChristmasCountdown() {
        log.info("Iniciando tarea programada - {}", LocalDateTime.now());

        String message = countdownService.buildTweetMessage();
        log.info("Mensaje a publicar: {}", message);

        twitterService.postTweet(message);
    }
}
