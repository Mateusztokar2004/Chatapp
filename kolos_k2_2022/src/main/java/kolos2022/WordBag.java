package kolos2022;

import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

class WordBag {

    private List<String> words = new ArrayList<>();
    private final Random rand = new Random();

    /** wczytuje slowa.txt z classpathu */
    void populate() {
        try (InputStream in = getClass().getResourceAsStream("/slowa.txt")) {
            if (in == null) {
                System.err.println("[WordBag] brak slowa.txt w resources!");
                return;
            }
            try (BufferedReader br = new BufferedReader(new InputStreamReader(in))) {
                words = br.lines().collect(Collectors.toList());
            }
            System.out.println("[WordBag] załadowano " + words.size() + " słów");
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    String get() {
        if (words.isEmpty()) return "BRAK_SŁÓW";
        return words.get(rand.nextInt(words.size()));
    }
}