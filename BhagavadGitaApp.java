import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class BhagavadGitaApp extends JFrame {
    private JList<String> chapterList;
    private JList<String> verseList;
    private JTextArea verseDisplay;
    private DefaultListModel<String> chapterModel;
    private DefaultListModel<String> verseModel;
    private List<String[]> versesData;
    private List<String> bookmarks;
    private List<String> favorites;
    private List<String> comments;
    private int currentChapterIndex = -1;
    private int currentVerseIndex = -1;
    private JProgressBar progressBar;
    private int totalVersesRead = 0;

    public BhagavadGitaApp() {
        // Main frame settings
        setTitle("Bhagavad Gita");
        setSize(900, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        // Load data from Excel file
        loadCSVData("C:\\Users\\KIIT0001\\Desktop\\java\\Shree_Bhagvad_Gita_App\\src\\resources\\bhagavad_gita.csv.csv");

        bookmarks = new ArrayList<>();
        favorites = new ArrayList<>();
        comments = new ArrayList<>();

        JPanel sidebar = createSidebarPanel();
        add(sidebar, BorderLayout.WEST);

        JPanel verseDisplayPanel = createVerseDisplayPanel();
        add(verseDisplayPanel, BorderLayout.CENTER);

        JPanel listPanel = createChapterVersePanel();
        listPanel.setPreferredSize(new Dimension(300, 0));
        add(listPanel, BorderLayout.EAST);

        setVisible(true);
    }

    private JPanel createSidebarPanel() {
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(128, 0, 0)); // Maroon background
        sidebar.setLayout(new GridLayout(6, 1, 5, 5));
        sidebar.setBorder(new EmptyBorder(10, 10, 10, 10));

        JButton bookmarksBtn = createSidebarButton("Bookmarks (🔖)");
        JButton favoritesBtn = createSidebarButton("Favorites (⭐)");
        JButton commentsBtn = createSidebarButton("Comments");
        JButton progressBtn = createSidebarButton("Progress");
        JButton audioBtn = createSidebarButton("Audio (🔊)");

        bookmarksBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                displaySidebarContent(bookmarks, "Bookmarks");
            }
        });

        favoritesBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                displaySidebarContent(favorites, "Favorites");
            }
        });

        commentsBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                displayComments();
            }
        });

        progressBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                displayProgress();
            }
        });

        audioBtn.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                JOptionPane.showMessageDialog(null, "Coming Soon!");
            }
        });

        sidebar.add(bookmarksBtn);
        sidebar.add(favoritesBtn);
        sidebar.add(commentsBtn);
        sidebar.add(progressBtn);
        sidebar.add(audioBtn);

        return sidebar;
    }

    private JPanel createVerseDisplayPanel() {
        JPanel verseDisplayPanel = new JPanel(new BorderLayout());
        verseDisplay = new JTextArea() {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                super.paintComponent(g2);
                g2.dispose();
            }
        };

        verseDisplay.setLineWrap(true);
        verseDisplay.setWrapStyleWord(true);
        verseDisplay.setEditable(false);
        verseDisplay.setFont(new Font("Mangal", Font.BOLD, 18));
        verseDisplay.setOpaque(false);
        verseDisplay.setForeground(Color.WHITE);

        JScrollPane displayScroll = new JScrollPane(verseDisplay);
        displayScroll.setOpaque(false);
        displayScroll.getViewport().setOpaque(false);
        displayScroll.setBorder(BorderFactory.createTitledBorder("Verse Translation"));
        displayScroll.setPreferredSize(new Dimension(600, 0));

        progressBar = new JProgressBar(0, 100);
        progressBar.setStringPainted(true);
        progressBar.setValue(0);
        progressBar.setForeground(Color.GREEN);

        JPanel progressPanel = new JPanel();
        progressPanel.setLayout(new BorderLayout());
        progressPanel.add(progressBar, BorderLayout.CENTER);
        verseDisplayPanel.add(displayScroll, BorderLayout.CENTER);
        verseDisplayPanel.add(progressPanel, BorderLayout.SOUTH);

        return verseDisplayPanel;
    }

    private void displayComments() {
        StringBuilder commentDisplay = new StringBuilder("Comments:\n");
        for (String comment : comments) {
            commentDisplay.append(comment).append("\n");
        }
        verseDisplay.setText(commentDisplay.toString());
    }

    private void displayProgress() {
        int totalVerses = versesData.size();
        int readVerses = totalVersesRead;
        double percentage = (double) readVerses / totalVerses * 100;
        progressBar.setValue((int) percentage);
        verseDisplay.setText("You have read " + readVerses + " out of " + totalVerses + " verses.");
    }

    private void displaySidebarContent(List<String> content, String title) {
        verseDisplay.setText("");
        chapterList.clearSelection();
        verseList.clearSelection();

        chapterModel.clear();
        verseModel.clear();

        for (String item : content) {
            chapterModel.addElement(item);
        }

        chapterList.setModel(chapterModel);
        verseList.setModel(verseModel);
        verseDisplay.setText("Selected " + title + ":");
    }

    private JButton createSidebarButton(String title) {
        JButton button = new JButton(title);
        button.setFocusPainted(false);
        button.setBackground(new Color(153, 0, 0));
        button.setForeground(Color.WHITE);
        button.setFont(new Font("SansSerif", Font.BOLD, 14));
        button.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        return button;
    }

    private JPanel createChapterVersePanel() {
        JPanel listPanel = new JPanel(new BorderLayout());
        listPanel.setBackground(new Color(230, 220, 220));
        chapterModel = new DefaultListModel<>();
        for (String chapter : getChapterNames()) {
            chapterModel.addElement(chapter);
        }
        chapterList = new JList<>(chapterModel);
        chapterList.setFont(new Font("SansSerif", Font.PLAIN, 14));
        chapterList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        chapterList.setBackground(new Color(245, 230, 230));
        chapterList.setForeground(new Color(64, 0, 0));

        JScrollPane chapterScroll = new JScrollPane(chapterList);
        chapterScroll.setBorder(BorderFactory.createTitledBorder("Chapters"));

        verseModel = new DefaultListModel<>();
        verseList = new JList<>(verseModel);
        verseList.setFont(new Font("SansSerif", Font.PLAIN, 14));
        verseList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        verseList.setBackground(new Color(245, 230, 230));
        verseList.setForeground(new Color(64, 0, 0));
        JScrollPane verseScroll = new JScrollPane(verseList);
        verseScroll.setBorder(BorderFactory.createTitledBorder("Verses"));

        listPanel.add(chapterScroll, BorderLayout.NORTH);
        listPanel.add(verseScroll, BorderLayout.CENTER);

        chapterList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 2) {
                    loadVersesForChapter(chapterList.getSelectedIndex());
                }
            }
        });

        verseList.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                if (e.getClickCount() == 1) {
                    displayVerse(verseList.getSelectedIndex());
                }
            }
        });

        return listPanel;
    }


    private void loadCSVData(String filePath) {
        versesData = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] values = line.split(","); // Assumes comma-separated values
                if (values.length >= 4) { // Ensure the row has all required columns
                    String chapter = values[0].trim();
                    String verse = values[1].trim();
                    String hindi = values[2].trim();
                    String english = values[3].trim();
                    versesData.add(new String[]{chapter, verse, hindi, english});
                }
            }
        } catch (IOException e) {
            System.err.println("Error reading CSV file: " + e.getMessage());
        }
    }


    private List<String> getChapterNames() {
        List<String> chapters = new ArrayList<>();
        for (String[] verseData : versesData) {
            String chapter = verseData[0];
            if (!chapters.contains(chapter)) {
                chapters.add(chapter);
            }
        }
        return chapters;
    }

    private void loadVersesForChapter(int chapterIndex) {
        verseModel.clear();
        String selectedChapter = getChapterNames().get(chapterIndex);
        for (String[] verseData : versesData) {
            if (verseData[0].equals(selectedChapter)) {
                verseModel.addElement("Verse " + verseData[1]);
            }
        }
    }

    private void displayVerse(int verseIndex) {
        String selectedChapter = getChapterNames().get(chapterList.getSelectedIndex());
        int verseCount = 0;
        for (String[] verseData : versesData) {
            if (verseData[0].equals(selectedChapter)) {
                if (verseCount == verseIndex) {
                    verseDisplay.setText("Hindi Translation:\n" + verseData[2] + "\n\nEnglish Translation:\n" + verseData[3]);
                    totalVersesRead++; // Increment read verses count
                    break;
                }
                verseCount++;
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new BhagavadGitaApp());
    }
}