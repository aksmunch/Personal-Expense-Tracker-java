import java.io.IOException;
import java.util.List;

public interface FileOperations {
    void save(List<Expense> expenses) throws IOException;
    List<Expense> load() throws IOException;
}
