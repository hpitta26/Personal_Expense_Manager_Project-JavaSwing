package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ExpenseListTest {
    private ExpenseList expenseListTest;

    @BeforeEach
    void runBefore() {
        expenseListTest = new ExpenseList();
        expenseListTest.addExpense(new Expense("Dorm", 90.0, "blanket", 9,14));
        expenseListTest.addExpense(new Expense("Restaurant", 10.0, "blant", 10,2));
        expenseListTest.addExpense(new Expense("Beach", 10.0, "blanket", 10,8));
        expenseListTest.addExpense(new Expense("Farm", 20.0, "blanket", 10,17));
        expenseListTest.addExpense(new Expense("Travel", 90.0, "blanket", 11,6));
        expenseListTest.addExpense(new Expense("Holiday", -5, "blanket", 12,-5));
    }

    @Test
    void testAddExpense() {
        Expense firstIndexExpense = new Expense("Dorm", 90.0, "blanket", 1,1);
        expenseListTest.addExpense(firstIndexExpense);
        assertTrue(expenseListTest.getExpenseList().contains(firstIndexExpense));
    }

    @Test
    void testAddDayMonth() {
        Expense testExpense = new Expense("Dorm", 90.0, "blanket", 1,13);
        expenseListTest.addExpense(testExpense);
        assertTrue(expenseListTest.getDayMonthTracker().contains(1.13));
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

}
