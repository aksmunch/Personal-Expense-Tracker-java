import java.util.*;
import java.util.regex.*;

public class ExpenseTracker {
    private List<Expense> expenses = new ArrayList<>();
    private ExpenseFileManager fileManager = new ExpenseFileManager();

    public ExpenseTracker() {
        try {
            expenses = fileManager.load();
        } catch (Exception e) {
            System.out.println("Error loading: " + e.getMessage());
        }
    }

    // Expose expenses for GUI (read-only view)
    public List<Expense> getExpenses() {
        return Collections.unmodifiableList(expenses);
    }

    // Regex validation
    private void validateDate(String date) throws InvalidDataException {
        if (!Pattern.matches("\\d{2}/\\d{2}/\\d{4}", date))
            throw new InvalidDataException("Invalid date format! Use DD/MM/YYYY");
    }

    public void addExpense(String category, double amount, String date) {
        try {
            validateDate(date);
            Expense e = new Expense(category, amount, date);
            expenses.add(e);
            fileManager.save(expenses);

            System.out.println("Expense added.");
        } catch (Exception ex) {
            System.out.println("Error: " + ex.getMessage());
            // Re-throw runtime exception so GUI can show message if desired (optional)
            // throw new RuntimeException(ex);
        }
    }

    public void viewExpenses() {
        System.out.println("\n--- All Expenses ---");
        for (Expense e : expenses) e.display();
    }

    // Linear Search
    public void searchByCategory(String key) {
        System.out.println("\nSearching for: " + key);
        boolean found = false;

        for (Expense e : expenses) {
            if (e.category.equalsIgnoreCase(key)) {
                e.display();
                found = true;
            }
        }

        if (!found) System.out.println("No expense found in that category.");
    }

    // Bubble Sort
    public void sortByAmount() {
        for (int i = 0; i < expenses.size() - 1; i++) {
            for (int j = 0; j < expenses.size() - i - 1; j++) {
                if (expenses.get(j).getAmount() > expenses.get(j + 1).getAmount()) {
                    Collections.swap(expenses, j, j + 1);
                }
            }
        }
        try {
            fileManager.save(expenses); // persist new order
        } catch (Exception e) {
            System.out.println("Error saving after sort: " + e.getMessage());
        }
        System.out.println("Sorted by amount.");
    }

    // Recursive sum of expenses
    public double recursiveTotal(int index) {
        if (index == expenses.size()) return 0;
        return expenses.get(index).getAmount() + recursiveTotal(index + 1);
    }
}
