package com.raffenio.christmascountdown.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.MonthDay;

@Service
public class CountdownService {

    private static final MonthDay CHRISTMAS = MonthDay.of(12, 25);

    public long daysUntilChristmas() {
        LocalDate today = LocalDate.now();
        LocalDate nextChristmas = CHRISTMAS.atYear(today.getYear());

        if (!today.isBefore(nextChristmas)) {
            nextChristmas = CHRISTMAS.atYear(today.getYear() + 1);
        }

        return today.until(nextChristmas, java.time.temporal.ChronoUnit.DAYS);
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
        return String.format(
                "%s ¡Faltan %d días para Navidad! 🎅🎁 #CuentaRegresiva #DiasParaHoHoHo #Navidad #Christmas #HolidayCountdown #RaffenioDev",
                emoji, days
        );
    }

    private String selectEmoji(long days) {
        if (days > 100) return "❄️";
        if (days > 30)  return "🎄";
        if (days > 7)   return "🎄✨";
        return "🎄🎅✨";
    }
}