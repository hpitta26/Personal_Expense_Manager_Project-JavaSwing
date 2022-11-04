package persistence;

import model.BorrowLend;
import model.Expense;
import model.ExpenseList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

//Used JsonSerializationDemo as a template
class JsonWriterTest extends JsonTest {

    @Test
    void testWriterInvalidFile() {
        try {
            ExpenseList expenseList = new ExpenseList();
            JsonWriter writer = new JsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            fail("IOException was expected, file name is invalid");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyWorkroom() {
        try {
            ExpenseList expenseList = new ExpenseList();
            JsonWriter writer = new JsonWriter("./data/testWriterEmptyExpenseList.json");
            writer.open();
            writer.write(expenseList);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterEmptyExpenseList.json");
            expenseList = reader.read();
            assertEquals(0, expenseList.getExpenseList().size());
            assertEquals(0, expenseList.getBorrowLendList().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralWorkroom() {
        try {
            ExpenseList expenseList = new ExpenseList();
            expenseList.setUserName("Jack1");
            expenseList.addBorrowLend(new BorrowLend("Henry", 5, "hi", 10,
                    1, true));
            expenseList.addBorrowLend(new BorrowLend("Sam", 1, "gm", 10,
                    20, false));
            expenseList.addExpense(new Expense("School", 10.0, "hello", 10, 10));
            expenseList.addExpense(new Expense("Dorm", 20.0, "hi", 10, 17));
            JsonWriter writer = new JsonWriter("./data/testWriterGeneralExpenseList.json");
            writer.open();
            writer.write(expenseList);
            writer.close();

            JsonReader reader = new JsonReader("./data/testWriterGeneralExpenseList.json");
            expenseList = reader.read();
            List<Expense> expenses = expenseList.getExpenseList();
            List<BorrowLend> borrowLends = expenseList.getBorrowLendList();
            assertEquals(2, expenses.size());
            assertEquals(2, borrowLends.size());
            checkExp("School", 10.0, "hello", 10, 10, expenses.get(0));
            checkExp("Dorm", 20.0, "hi", 10, 17, expenses.get(1));
            checkBL("Henry", 5, "hi", 10,
                    1, true, borrowLends.get(0));
            checkBL("Sam", 1, "gm", 10,
                    20, false, borrowLends.get(1));

        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

}