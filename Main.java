import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        ExpenseTracker tracker = new ExpenseTracker();
        SwingUtilities.invokeLater(() -> {
            MainWindow mw = new MainWindow(tracker);
            mw.setVisible(true);
        });
    }
}
