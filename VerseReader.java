import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class VerseReader {
    private Map<String, Verse> verses = new HashMap<>();
    private List<Verse> bookmarkedVerses = new ArrayList<>();

    public void loadVerses(String filename) {
        System.out.println("Attempting to load verses from: " + filename);

        File file = new File(filename);
        if (!file.exists()) {
            System.err.println("File does not exist: " + file.getAbsolutePath());
            throw new RuntimeException("File not found: " + file.getAbsolutePath());
        }

        try (BufferedReader br = new BufferedReader(new FileReader(file, StandardCharsets.UTF_8))) {
            String line;
            int lineNumber = 0;
            br.readLine(); // Skip header line
            while ((line = br.readLine()) != null) {
                lineNumber++;
                try {
                    String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                    if (parts.length >= 7) {
                        Verse verse = new Verse(
                                parts[0].trim(), // Chapter
                                parts[4].trim(), // Verse number
                                parts[5].trim().replace("\"", ""), // Sanskrit text
                                parts[6].trim().replace("\"", ""), // Hindi translation
                                parts[7].trim().replace("\"", "")  // English translation
                        );
                        verses.put(parts[0] + "." + parts[4], verse);
                        System.out.println("Loaded verse: Chapter " + parts[0] + ", Verse " + parts[4]);
                    } else {
                        System.err.println("Invalid line format at line " + lineNumber + ": " + line);
                    }
                } catch (Exception e) {
                    System.err.println("Error parsing line " + lineNumber + ": " + line);
                    e.printStackTrace();
                }
            }
            System.out.println("Total verses loaded: " + verses.size());
        } catch (IOException e) {
            System.err.println("Error reading file: " + filename);
            e.printStackTrace();
            throw new RuntimeException("Error reading file: " + filename, e);
        }

        if (verses.isEmpty()) {
            throw new RuntimeException("No verses were loaded from the file");
        }
    }

    public Verse getVerse(String key) {
        return verses.get(key);
    }

    public Collection<Verse> getAllVerses() {
        return verses.values();
    }

    public void bookmarkVerse(Verse verse) {
        verse.setBookmarked(true);
        if (!bookmarkedVerses.contains(verse)) {
            bookmarkedVerses.add(verse);
        }
    }

    public void removeBookmark(Verse verse) {
        verse.setBookmarked(false);
        bookmarkedVerses.remove(verse);
    }

    public List<Verse> getBookmarkedVerses() {
        return new ArrayList<>(bookmarkedVerses);
    }
}