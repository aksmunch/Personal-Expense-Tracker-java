public class Expense extends ExpenseBase {

    public Expense(String category, double amount, String date) {
        this.category = category;
        this.amount = amount;
        this.date = date;
    }

    // Method Overloading
    public String format() {
        return date + " | " + category + " | " + amount;
    }

    public String format(String separator) {
        return date + separator + category + separator + amount;
    }

    @Override
    public void display() {
        System.out.println(format());
    }

    public double getAmount() {
        return amount;
    }

    // Parsing an Expense from saved file
    public static Expense fromString(String line) {
        String[] parts = line.split("\\s*\\|\\s*");
        if (parts.length != 3) return null;
        try {
            return new Expense(parts[1], Double.parseDouble(parts[2]), parts[0]);
        } catch (NumberFormatException ex) {
            return null;
        }
    }
}
