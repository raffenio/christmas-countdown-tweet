package com.raffenio.christmascountdown.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Service
public class CountdownService {

    private static final MonthDay CHRISTMAS = MonthDay.of(12, 25);
    private static final ZoneId ZONE = ZoneId.of("America/Mexico_City");

    private final ChristmasMessageService messageService;

    public CountdownService(ChristmasMessageService messageService) {
        this.messageService = messageService;
    }

    public long daysUntilChristmas() {
        LocalDate today = LocalDate.now(ZONE);
        LocalDate nextChristmas = CHRISTMAS.atYear(today.getYear());

        if (!today.isBefore(nextChristmas)) {
            nextChristmas = CHRISTMAS.atYear(today.getYear() + 1);
        }

        return ChronoUnit.DAYS.between(today, nextChristmas);
    }

    public String buildTweetMessage() {
        long days = daysUntilChristmas();

        if (days == 0) {
            return "🎄🎅 ¡HOY ES NAVIDAD! ¡Feliz Navidad a todos! ✨🎁 #FelizNavidad #MerryChristmas";
        }

        if (days == 1) {
            return "🎄 ¡Solo falta 1 día para Navidad! 🎅 ¡Mañana es el gran día! ✨🎁 #Navidad #Christmas";
        }

        String emoji = selectEmoji(days);
        String randomMsg = messageService.randomMessage();
        return String.format(
                "%s ¡Faltan %d días para Navidad! 🎅🎁\n\n%s\n\n#CuentaRegresiva #DiasParaHoHoHo #Navidad #Christmas #HolidayCountdown #RaffenioDev",
                emoji, days, randomMsg
        );
    }

    private String selectEmoji(long days) {
        if (days > 100) return "❄️";
        if (days > 30)  return "🎄";
        if (days > 7)   return "🎄✨";
        return "🎄🎅✨";
    }
}
