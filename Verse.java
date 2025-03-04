public class Verse {
    private String chapter;
    private String verseNumber;
    private String sanskritText;
    private String hindiTranslation;
    private String englishTranslation;
    private boolean bookmarked;

    public Verse(String chapter, String verseNumber, String sanskritText,
                 String hindiTranslation, String englishTranslation) {
        this.chapter = chapter;
        this.verseNumber = verseNumber;
        this.sanskritText = sanskritText;
        this.hindiTranslation = hindiTranslation;
        this.englishTranslation = englishTranslation;
        this.bookmarked = false;
    }

    // Getters
    public String getChapter() { return chapter; }
    public String getVerseNumber() { return verseNumber; }
    public String getSanskritText() { return sanskritText; }
    public String getHindiTranslation() { return hindiTranslation; }
    public String getEnglishTranslation() { return englishTranslation; }
    public boolean isBookmarked() { return bookmarked; }

    // Setters
    public void setChapter(String chapter) { this.chapter = chapter; }
    public void setVerseNumber(String verseNumber) { this.verseNumber = verseNumber; }
    public void setSanskritText(String sanskritText) { this.sanskritText = sanskritText; }
    public void setHindiTranslation(String hindiTranslation) { this.hindiTranslation = hindiTranslation; }
    public void setEnglishTranslation(String englishTranslation) { this.englishTranslation = englishTranslation; }
    public void setBookmarked(boolean bookmarked) { this.bookmarked = bookmarked; }

    @Override
    public String toString() {
        return "Chapter " + chapter + ", Verse " + verseNumber;
    }
}