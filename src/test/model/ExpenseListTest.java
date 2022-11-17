package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import persistence.JsonReader;
import persistence.JsonWriter;

import static org.junit.jupiter.api.Assertions.*;

public class ExpenseListTest {
    private ExpenseList expenseListTest;
    private JsonReader reader;
    private JsonWriter writer;

    @BeforeEach
    void runBefore() {
        expenseListTest = new ExpenseList();
        expenseListTest.addExpense(new Expense("Dorm", 90.0, "blanket", 9,14));
        expenseListTest.addExpense(new Expense("Restaurant", 10.0, "blan", 10,2));
        expenseListTest.addExpense(new Expense("Beach", 10.0, "blanket", 10,8));
        expenseListTest.addExpense(new Expense("Farm", 20.0, "blanket", 10,17));
        expenseListTest.addExpense(new Expense("Travel", 90.0, "blanket", 11,6));
        expenseListTest.addExpense(new Expense("Holiday", -5, "blanket", 12,-5));
    }

    @Test
    void testAddRemoveExpense() {
        Expense firstIndexExpense = new Expense("Dorm", 90.0, "blanket", 1,1);
        expenseListTest.addExpense(firstIndexExpense);
        assertTrue(expenseListTest.getExpenseList().contains(firstIndexExpense));
        expenseListTest.removeLastExpense();
        assertFalse(expenseListTest.getExpenseList().contains(firstIndexExpense));
    }

    @Test
    void testAddDayMonth() {
        Expense testExpense = new Expense("Dorm", 90.0, "blanket", 1,13);
        expenseListTest.addExpense(testExpense);
        assertTrue(expenseListTest.getDayMonthTracker().contains(1.13));
    }

    @Test
    void testAddBorrowLend() {
        BorrowLend testBL = new BorrowLend("Bob", 90.0, "hi", 1,13, true);
        expenseListTest.addBorrowLend(testBL);
        assertTrue(expenseListTest.getBorrowLendList().contains(testBL));
        assertEquals(1, expenseListTest.getBorrowLendList().size());
    }

    @Test
    void testGetMonthlyTotal() {
        Expense testExpense1 = new Expense("Dorm", 43.2, "blanket", 1,13);
        Expense testExpense2 = new Expense("Dorm", 56.7, "blanket", 2,13);
        Expense testExpense3 = new Expense("Dorm", 76.4, "blanket", 3,13);
        expenseListTest.addExpense(testExpense1);
        expenseListTest.addExpense(testExpense2);
        expenseListTest.addExpense(testExpense3);
        System.out.println(testExpense1.getPrice());
        double[] expect = {43.2, 56.7, 76.4};
        double[] actual = expenseListTest.getMonthlyOrBiweeklyTotal(1, true);
        for (int i = 0; i < actual.length; i++) {
            assertEquals(true, expect[i] == actual[i]);
        }
    }

    @Test
    void testGetBiweeklyTotal() {
        Expense testExpense1 = new Expense("Dorm", 43.2, "blanket", 4,13);
        Expense testExpense2 = new Expense("Dorm", 56.7, "blanket", 4,23);
        Expense testExpense3 = new Expense("Dorm", 76.4, "blanket", 5,13);
        expenseListTest.addExpense(testExpense1);
        expenseListTest.addExpense(testExpense2);
        expenseListTest.addExpense(testExpense3);
        double[] expect = {43.2, 56.7, 76.4};
        double[] actual = expenseListTest.getMonthlyOrBiweeklyTotal(4, false);
        for (int i = 0; i < actual.length; i++) {
            assertEquals(true, expect[i] == actual[i]);
        }
    }

    @Test
    void testGetCategoryPercentagesPerMonth() {
        double[] actual = expenseListTest.getCategoryPercentagesPerMonth(10);
        double[] expected = {0,0,0,25,0,0,25,50,0};
        for (int i = 0; i < actual.length; i++) {
            assertEquals(true, expected[i] == actual[i]);
        }
    }

    @Test
    void testGetCategoryTotalPerMonth() {
        Expense e = new Expense("Farm", 10.0, "blanket", 10,14);
        expenseListTest.addExpense(e);
        double[] actual = expenseListTest.getCategoryTotalPerMonth(10);
        double[] expected = {0,0,0,10,0,0,10,30,0};
        for (int i = 0; i < actual.length; i++) {
            assertEquals(true, expected[i] == actual[i]);
        }
    }

    @Test
    void testGettersAndSetters() {
        expenseListTest.setUserName("Bob");
        assertEquals("Bob", expenseListTest.getUserName());
    }

    @Test
    void testToJson() {
        reader = new JsonReader("./data/testExpenseListToJson.json");
        writer = new JsonWriter("./data/testExpenseListToJson.json");

        ExpenseList expenseListTemp = new ExpenseList();
        Expense e1 = new Expense("Dorm", 90.0, "blanket", 9,14);
        BorrowLend b1 = new BorrowLend("Bob", 90.0, "hi", 9,14, true);
        expenseListTemp.addExpense(e1);
        expenseListTemp.addBorrowLend(b1);

        try {
            writer.open();
            writer.write(expenseListTemp);
            writer.close();
            ExpenseList expenseListTemp2 = reader.read();
            checkIfExpensesAreEqual(expenseListTemp2);
            checkIfBorrowLendsAreEqual(expenseListTemp2);
        } catch (Exception e) {
            fail("Not supposed to throw an Exception");
        }
    }

    public void checkIfExpensesAreEqual(ExpenseList e) {
        assertEquals(1, e.getExpenseList().size());
        Expense checkExpense = e.getExpenseList().get(0);
        assertEquals("Dorm", checkExpense.getCategory());
        assertEquals(90, checkExpense.getPrice());
        assertEquals("blanket", checkExpense.getDescription());
        assertEquals(9, checkExpense.getMonth());
        assertEquals(14, checkExpense.getDay());
    }

    public void checkIfBorrowLendsAreEqual(ExpenseList e) {
        assertEquals(1, e.getBorrowLendList().size());
        BorrowLend checkBorrowLend = e.getBorrowLendList().get(0);
        assertEquals("Bob", checkBorrowLend.getName());
        assertEquals(90, checkBorrowLend.getPrice());
        assertEquals("hi", checkBorrowLend.getDescription());
        assertEquals(9, checkBorrowLend.getMonth());
        assertEquals(14, checkBorrowLend.getDay());
        assertTrue(checkBorrowLend.getBorrowLend());
    }

}
