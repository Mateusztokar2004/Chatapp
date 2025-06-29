package client;

import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

/** Pojedynczy wpis (czas otrzymania + słowo). */
public record WordEntry(String word, LocalTime time) {

    @Override public String toString() {
        return time.format(DateTimeFormatter.ofPattern("HH:mm:ss")) + " " + word;
    }
}