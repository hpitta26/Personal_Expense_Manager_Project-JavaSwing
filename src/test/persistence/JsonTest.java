package persistence;

import model.BorrowLend;
import model.Expense;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

//Used JsonSerializationDemo as a template
class JsonTest {
    protected void checkExp(String category, double price, String description, int month, int day, Expense expense) {
        assertEquals(category, expense.getCategory());
        assertEquals(price, expense.getPrice());
        assertEquals(description, expense.getDescription());
        assertEquals(month, expense.getMonth());
        assertEquals(day, expense.getDay());
    }

    protected void checkBL(String name, double amount, String description, int month, int day, boolean
            borrowOrLend, BorrowLend borrowLend) {
        assertEquals(name, borrowLend.getName());
        assertEquals(amount, borrowLend.getPrice());
        assertEquals(description, borrowLend.getDescription());
        assertEquals(month, borrowLend.getMonth());
        assertEquals(day, borrowLend.getDay());
        assertEquals(borrowOrLend, borrowLend.getBorrowLend());
    }

}