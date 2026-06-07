package com.raffenio.christmascountdown;

import com.raffenio.christmascountdown.service.CountdownService;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class CountdownServiceTest {

    private final CountdownService service = new CountdownService();

    @Test
    void daysUntilChristmas_shouldBePositive() {
        long days = service.daysUntilChristmas();
        assertThat(days).isGreaterThanOrEqualTo(0);
        assertThat(days).isLessThanOrEqualTo(365);
    }

    @Test
    void buildTweetMessage_shouldContainChristmasHashtag() {
        String message = service.buildTweetMessage();
        assertThat(message).containsAnyOf("#Navidad", "#FelizNavidad");
    }

    @Test
    void buildTweetMessage_shouldNotExceedTwitterLimit() {
        String message = service.buildTweetMessage();
        assertThat(message.length()).isLessThanOrEqualTo(280);
    }
}
