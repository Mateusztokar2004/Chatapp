package client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.text.Normalizer;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Controller {

    @FXML private ListView<String> wordList;
    @FXML private TextField  filterField;
    @FXML private Label      wordCountLabel;

    private final List<WordEntry> all = new ArrayList<>();

    /* wywoływane automatycznie po załadowaniu FXML */
    @FXML private void initialize() {
        filterField.textProperty().addListener((obs, o, n) -> refreshList());
        new Thread(this::connectAndRead, "ReaderThread").start();
    }

    /* ------------ sieć ------------ */
    private void connectAndRead() {
        try (Socket socket = new Socket("localhost", 5000);
             BufferedReader br = new BufferedReader(
                     new InputStreamReader(socket.getInputStream(), StandardCharsets.UTF_8))) {

            String word;
            while ((word = br.readLine()) != null) {
                WordEntry entry = new WordEntry(word, LocalTime.now());
                synchronized (all) { all.add(entry); }

                String     wCopy = word;   // kopie finalne
                WordEntry eCopy  = entry;

                Platform.runLater(() -> {
                    wordCountLabel.setText(String.valueOf(all.size()));
                    if (passesFilter(wCopy))
                        insertEntry(eCopy);
                });
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /* ------------ UI pomocnicze ------------ */
    private void refreshList() {
        wordList.getItems().clear();
        synchronized (all) {
            all.stream()
                    .filter(e -> passesFilter(e.word()))
                    .sorted(ENTRY_CMP)
                    .map(WordEntry::toString)
                    .forEach(wordList.getItems()::add);
        }
    }

    private void insertEntry(WordEntry e) {
        if (!passesFilter(e.word())) return;
        wordList.getItems().add(e.toString());
        wordList.getItems().sort(String::compareTo);   // utrzymujemy porządek ASCII
    }

    private boolean passesFilter(String word) {
        String f = filterField.getText();
        return f == null || f.isEmpty() ||
                normalize(word).startsWith(normalize(f));
    }

    /* Sortowanie ASCII + bez polskich znaków */
    private static final Comparator<WordEntry> ENTRY_CMP =
            Comparator.comparing(e -> normalize(e.word()));

    private static String normalize(String s) {
        return Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{M}", "")        // usunięcie akcentów
                .toLowerCase();
    }
}
