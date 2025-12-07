import java.io.*;
import java.util.*;

public class ExpenseFileManager implements FileOperations {
    private final File file = new File("expenses.txt");

    @Override
    public void save(List<Expense> expenses) throws IOException {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            for (Expense e : expenses) {
                bw.write(e.format());
                bw.newLine();
            }
        }
    }

    @Override
    public List<Expense> load() throws IOException {
        List<Expense> list = new ArrayList<>();
        if (!file.exists()) return list;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                Expense e = Expense.fromString(line);
                if (e != null) list.add(e);
            }
        }
        return list;
    }
}
