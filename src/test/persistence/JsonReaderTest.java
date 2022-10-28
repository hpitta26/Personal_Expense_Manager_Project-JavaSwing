package persistence;

import model.Expense;
import model.ExpenseList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

//Used JsonSerializationDemo as a template
class JsonReaderTest extends JsonTest{

    @Test
    void testReaderNonExistentFile() {
        JsonReader reader = new JsonReader("./data/noSuchFile.json");
        try {
            ExpenseList expenseList = reader.read();
            fail("IOException expected");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testReaderEmptyExpenseList() {
        JsonReader reader = new JsonReader("./data/testReaderEmptyExpenseList.json");
        try {
            ExpenseList expenseList = reader.read();
            assertEquals(0, expenseList.getExpenseList().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralExpenseList() {
        JsonReader reader = new JsonReader("./data/testReaderGeneralExpenseList.json");
        try {
            ExpenseList expenseList = reader.read();
            List<Expense> expenses = expenseList.getExpenseList();
            assertEquals(2, expenses.size());
            checkExp("School", 10.0, "hello", 10, 10, expenses.get(0));
            checkExp("Dorm", 20.0, "hi", 10, 17, expenses.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }
}