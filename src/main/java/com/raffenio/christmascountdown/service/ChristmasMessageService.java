package com.raffenio.christmascountdown.service;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class ChristmasMessageService {

    private static final Logger log = LoggerFactory.getLogger(ChristmasMessageService.class);
    private static final int MAX_MESSAGE_LENGTH = 120;

    private final List<String> messages = new ArrayList<>();
    private final Random random = new Random();

    @PostConstruct
    public void loadMessages() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                new ClassPathResource("christmas-messages.txt").getInputStream(), StandardCharsets.UTF_8))) {
            reader.lines()
                    .map(String::trim)
                    .filter(line -> !line.isEmpty())
                    .forEach(messages::add);
            log.info("Cargadas {} frases navideñas", messages.size());
        } catch (Exception e) {
            log.error("No se pudo cargar christmas-messages.txt: {}", e.getMessage());
        }
    }

    public String randomMessage() {
        if (messages.isEmpty()) return "";
        String msg = messages.get(random.nextInt(messages.size()));
        return msg.length() > MAX_MESSAGE_LENGTH ? msg.substring(0, MAX_MESSAGE_LENGTH) : msg;
    }
}
