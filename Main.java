import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                BhagavadGitaApp app = new BhagavadGitaApp();
                app.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
                System.err.println("Error starting application: " + e.getMessage());
            }
        });
    }
}