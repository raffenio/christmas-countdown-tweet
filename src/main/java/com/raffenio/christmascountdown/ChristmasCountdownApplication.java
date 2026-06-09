package com.raffenio.christmascountdown;

import com.raffenio.christmascountdown.service.CountdownService;
import com.raffenio.christmascountdown.service.TwitterService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ChristmasCountdownApplication {

    private static final Logger log = LoggerFactory.getLogger(ChristmasCountdownApplication.class);

    public static void main(String[] args) {
        SpringApplication.run(ChristmasCountdownApplication.class, args);
    }

    @Bean
    public ApplicationRunner sendTweetAndExit(TwitterService twitterService,
                                               CountdownService countdownService,
                                               ApplicationContext context) {
        return args -> {
            String message = countdownService.buildTweetMessage();
            log.info("Mensaje a publicar: {}", message);
            twitterService.postTweet(message);

            log.info("Tweet enviado. Cerrando aplicación.");
            int exitCode = SpringApplication.exit(context, () -> 0);
            System.exit(exitCode);
        };
    }
}
