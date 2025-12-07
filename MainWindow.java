import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.List;

public class MainWindow extends JFrame {
    private ExpenseTracker tracker;

    private JTextField txtCategory;
    private JTextField txtAmount;
    private JTextField txtDate;
    private JTextArea outputArea;

    public MainWindow(ExpenseTracker tracker) {
        this.tracker = tracker;
        setTitle("Expense Tracker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 450);
        setLocationRelativeTo(null);
        initUI();
    }

    private void initUI() {
        JPanel topPanel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6,6,6,6);
        gbc.anchor = GridBagConstraints.WEST;

        gbc.gridx = 0; gbc.gridy = 0;
        topPanel.add(new JLabel("Category:"), gbc);
        gbc.gridx = 1;
        txtCategory = new JTextField(12);
        topPanel.add(txtCategory, gbc);

        gbc.gridx = 0; gbc.gridy = 1;
        topPanel.add(new JLabel("Amount:"), gbc);
        gbc.gridx = 1;
        txtAmount = new JTextField(12);
        topPanel.add(txtAmount, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        topPanel.add(new JLabel("Date (DD/MM/YYYY):"), gbc);
        gbc.gridx = 1;
        txtDate = new JTextField(12);
        topPanel.add(txtDate, gbc);

        JPanel btnPanel = new JPanel(new GridLayout(1, 6, 6, 6));
        JButton btnAdd = new JButton("Add");
        JButton btnView = new JButton("View");
        JButton btnSearch = new JButton("Search");
        JButton btnSort = new JButton("Sort");
        JButton btnTotal = new JButton("Total");
        JButton btnExit = new JButton("Exit");

        btnPanel.add(btnAdd);
        btnPanel.add(btnView);
        btnPanel.add(btnSearch);
        btnPanel.add(btnSort);
        btnPanel.add(btnTotal);
        btnPanel.add(btnExit);

        outputArea = new JTextArea();
        outputArea.setEditable(false);
        JScrollPane scroll = new JScrollPane(outputArea);

        Container cp = getContentPane();
        cp.setLayout(new BorderLayout());
        cp.add(topPanel, BorderLayout.NORTH);
        cp.add(btnPanel, BorderLayout.CENTER);
        cp.add(scroll, BorderLayout.SOUTH);
        scroll.setPreferredSize(new Dimension(580, 240));

        // Button actions
        btnAdd.addActionListener(e -> onAdd());
        btnView.addActionListener(e -> refreshView());
        btnSearch.addActionListener(e -> onSearch());
        btnSort.addActionListener(e -> onSort());
        btnTotal.addActionListener(e -> onTotal());
        btnExit.addActionListener(e -> {
            dispose();
            System.exit(0);
        });

        // initial display
        refreshView();
    }

    private void onAdd() {
        String cat = txtCategory.getText().trim();
        String amtStr = txtAmount.getText().trim();
        String date = txtDate.getText().trim();

        if (cat.isEmpty() || amtStr.isEmpty() || date.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Please fill all fields.", "Input error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        double amt;
        try {
            amt = Double.parseDouble(amtStr);
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Invalid amount.", "Input error", JOptionPane.WARNING_MESSAGE);
            return;
        }

        tracker.addExpense(cat, amt, date);
        JOptionPane.showMessageDialog(this, "Expense added.", "OK", JOptionPane.INFORMATION_MESSAGE);
        txtCategory.setText("");
        txtAmount.setText("");
        txtDate.setText("");
        refreshView();
    }

    private void refreshView() {
        List<Expense> list = tracker.getExpenses();
        StringBuilder sb = new StringBuilder();
        sb.append("--- All Expenses ---\n");
        if (list.isEmpty()) sb.append("(none)\n");
        else {
            for (Expense e : list) {
                sb.append(e.format()).append("\n");
            }
        }
        outputArea.setText(sb.toString());
    }

    private void onSearch() {
        String key = JOptionPane.showInputDialog(this, "Enter category to search:", "Search", JOptionPane.QUESTION_MESSAGE);
        if (key == null) return; // cancelled
        List<Expense> list = tracker.getExpenses();
        StringBuilder sb = new StringBuilder();
        sb.append("Search results for '").append(key).append("':\n");
        boolean found = false;
        for (Expense e : list) {
            if (e.category.equalsIgnoreCase(key.trim())) {
                sb.append(e.format()).append("\n");
                found = true;
            }
        }
        if (!found) sb.append("(no matches)\n");
        outputArea.setText(sb.toString());
    }

    private void onSort() {
        tracker.sortByAmount();
        refreshView();
        JOptionPane.showMessageDialog(this, "Sorted by amount.", "Sorted", JOptionPane.INFORMATION_MESSAGE);
    }

    private void onTotal() {
        double total = tracker.recursiveTotal(0);
        JOptionPane.showMessageDialog(this, "Total = " + total, "Total", JOptionPane.INFORMATION_MESSAGE);
    }
}
