package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class ExpenseTest {
    private Expense testExpense1;
    private Expense testExpense2;
    private Expense testExpense3;
    private Expense testExpense4;
    private Expense testExpense5;
    private Expense testExpenseWrong;


    @BeforeEach
    void runBefore() {
        testExpense1 = new Expense("Dorm", 90.0, "blanket", 9,14);
        testExpense2 = new Expense("Restaurant", 90.0, "blanket", 10,2);
        testExpense3 = new Expense("Beach", 90.0, "blanket", 10,8);
        testExpense4 = new Expense("Farm", 90.0, "blanket", 10,17);
        testExpense5 = new Expense("Travel", 90.0, "blanket", 11,6);
        testExpenseWrong = new Expense("Holiday", -5, "blanket", 13,-5);
    }

    @Test
    void testConstructor() {
        assertEquals("Dorm", testExpense1.getCategory());
        assertEquals(90.0, testExpense1.getPrice());
        assertEquals("blanket", testExpense1.getDescription());
        assertEquals(9, testExpense1.getMonth());
        assertEquals(14, testExpense1.getDay());
    }

    @Test
    void testConstructorInvalidInput() {
        assertEquals(0, testExpenseWrong.getPrice());
        assertEquals(1, testExpenseWrong.getMonth());
        assertEquals(1, testExpenseWrong.getDay());
    }

    @Test
    void testNumOfCustomCategories() {
        assertEquals(3, testExpenseWrong.getCountNumOfCustomCategories());
        assertEquals("Random", testExpenseWrong.getCategory());
        assertEquals("Travel", testExpense5.getCategory());
    }

    @Test
    void testExpenseCompareTo() {
        assertEquals(1, testExpense2.compareTo(testExpense1));
        Expense testExpense6 = new Expense("Restaurant", 90.0, "blanket", 10,2);
        assertEquals(0, testExpense2.compareTo(testExpense6));
        assertEquals(-1, testExpense2.compareTo(testExpense5));
    }






}