package com.raffenio.christmascountdown.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import twitter4j.Twitter;

@Configuration
public class TwitterConfig {

    private final TwitterProperties props;

    public TwitterConfig(TwitterProperties props) {
        this.props = props;
    }

    @Bean
    public Twitter twitter() {
        return Twitter.newBuilder()
                .oAuthConsumer(props.getApiKey(), props.getApiSecret())
                .oAuthAccessToken(props.getAccessToken(), props.getAccessTokenSecret())
                .build();
    }
}
