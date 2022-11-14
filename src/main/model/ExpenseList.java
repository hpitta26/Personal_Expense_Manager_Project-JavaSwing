package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Used JsonSerializationDemo as template for toJson() methods

//Represents a List of Expenses that you can add Expenses to, get Monthly Category Percentages, and Compare Monthly
// or Biweekly spending.
public class ExpenseList implements Writable {
    private String userName = ""; //Create a name variable each ExpenseList
    private List<Expense> expenseList = new ArrayList<>(); //List that stores the Expenses
    private List<BorrowLend> borrowLendList = new ArrayList<>();
    private List<Double> dayMonthTracker =
            new ArrayList<>(Arrays.asList(1.0,2.0,3.0,4.0,5.0,6.0,7.0,8.0,9.0,10.0,11.0,12.0,13.0)); //tracks months.
    /*
     * dayMonthTracker provides a way to quickly find the indexes of Expenses based on their month.day, this helps
     * when the ExpenseList is too long, and you do not want to for-loop through the whole List since it will be very
     * slow.
     */

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        return this.userName;
    }

    //Modified: this
    //Effects: Adds a borrowLend Object to borrowLendList
    public void addBorrowLend(BorrowLend borrowLend) {
        borrowLendList.add(borrowLend);
        borrowLendList.sort(null);
    }

    /*
     * REQUIRES:
     * MODIFIES: this
     * EFFECTS: Adds an Expense to the expenseList, then sorts it based on the date so that the Expenses are in order.
     *          Adds the date of that Expense to the dayMonthTracker so that we can keep track of the Expense's index.
     */
    public void addExpense(Expense expense) {
        expenseList.add(expense);
        //expenseList.sort(null);
        addDayMonth(expense.getMonth(), expense.getDay());
    }

    /*
     * REQUIRES:
     * MODIFIES: this
     * EFFECTS: Takes in a month and a day as a parameter and adds that to the dayMonthTracker in the format: month.day.
     *          This allows me to store both pieces of information in a double. So that I can keep track of the indices
     *          of their corresponding Expenses.
     */
    private void addDayMonth(int month, int day) { //tracks indices per day/month so I dont have to loop to find them
        dayMonthTracker.add(month + ((double)day) / 100);
        //dayMonthTracker.sort(null);
    }

    public List<Expense> getExpenseList() {
        return expenseList;
    }

    public List<Double> getDayMonthTracker() {
        return dayMonthTracker;
    }

    public List<BorrowLend> getBorrowLendList() {
        return borrowLendList;
    }

    //Modifies: this
    //Effects: Removes the last Expense added to expenseList, cannot be after list is sorted
    public void removeLastExpense() {
        expenseList.remove(expenseList.size() - 1);
        dayMonthTracker.remove(dayMonthTracker.size() - 1);
    }

    /*
     * REQUIRES: 0 < month < 13
     * MODIFIES:
     * EFFECTS: Takes in an int month and a boolean which decides if the terms will be Monthly or Biweekly.
     *          If isMonthly is true it returns a double[] of length 3, where the indices are:
     *          {(month)_totalExpenses, (month+1)_totalExpenses, (month+2)_totalExpenses}, if the month ever becomes
     *          12 then the next month will be 1 not 13. If isMonthly is false, then it returns a double[] where
     *          indices are: {(month_days1-14)_totalExpenses, (month_days14-end_ofMonth)_totalExpenses,
     *          ((month+1)_days1-14)_totalExpenses}, if month==12 then the next month is 1 not 13.
     */
    public double[] getMonthlyOrBiweeklyTotal(int month, boolean isMonthly) {
        expenseList.sort(null);
        dayMonthTracker.sort(null);
        double[] threeTermExpenses = new double[3]; //will return 3 terms
        if (isMonthly) {
            for (int i = 0; i < 3; i++) {
                threeTermExpenses[i] = calculateMonthlyExpense(month);
                month = (month == 12) ? 1 : month + 1; //month after 12 is 1, not 13
            }
        } else {
            for (int i = 0; i < 3; i++) {
                threeTermExpenses[i] = biweeklyHelper(month, i);
            }
        }
        return threeTermExpenses; //if index is zero then there aren't any expenses in that period
    }


    /*
     * REQUIRES: 0 < month < 13
     * MODIFIES:
     * EFFECTS: Helper method for getMonthlyOrBiweeklyTotal which calculates the user's total Monthly Expense.
     *          Uses dayMonthTracker to find the index of the first Expense of the month and adds all the Prices until
     *          the end of the month and returns that value.
     */
    private double calculateMonthlyExpense(int month) {
        double sum = 0;
        Double thisMonth = (Double)(double)month;
        Double nextMonth = (Double)(double)(month + 1);
        for (int i = dayMonthTracker.indexOf(thisMonth) + 1; i < dayMonthTracker.indexOf(nextMonth); i++) {
            sum = sum + expenseList.get(i - month).getPrice(); // -month to recalibrate the index
        }
        return sum;
    }

    /*
     * REQUIRES: 0 < month < 13
     * MODIFIES:
     * EFFECTS: Helper method for getMonthlyOrBiweeklyTotal() which calculates the user's total Biweekly Expense.
     *          Uses dayMonthTracker to find the index of the first Expense of the month and only adds the value of the
     *          term specified by the boolean isFirstTerm. If true it adds days 1-14, if false then it adds days
     */
    private double calculateBiweeklyExpenses(int month, boolean isFirstTerm) {
        double sum = 0;
        Double thisMonth = (Double)(double)month;
        Double nextMonth = (Double)(double)(month + 1);
        for (int i = dayMonthTracker.indexOf(thisMonth) + 1; i < dayMonthTracker.indexOf(nextMonth); i++) {
            if (isFirstTerm) {
                if (dayMonthTracker.get(i) < (Double)((double)month + 0.15)) {
                    sum = sum + expenseList.get(i - month).getPrice();
                }
            } else {
                if (dayMonthTracker.get(i) > (Double)((double)month + 0.14)) {
                    sum = sum + expenseList.get(i - month).getPrice();
                }
            }
        }
        return sum;
    }

    /*
     * REQUIRES: 0 < month < 13, 0 =< count =< 3
     * MODIFIES:
     * EFFECTS: Helper method for calculateBiweeklyExpenses(), calls calculateBiweeklyExpenses() based on which
     *          Biweekly term is being calculated. Specifies if it's supposed to calculate the totalExpenses for
     *          days 0-14, 15-end_ofMonth, days 0-14 of the next month.
     */
    private double biweeklyHelper(int month, int count) {
        double sum;
        if (count == 0) {
            sum = calculateBiweeklyExpenses(month, true);
        } else if (count == 1) {
            sum = calculateBiweeklyExpenses(month, false);
        } else {
            int nextMonth = (month == 12) ? 1 : month + 1;
            sum = calculateBiweeklyExpenses(nextMonth, true);
        }
        return sum;
    }


    /*
     * REQUIRES: 0 < month < 13
     * MODIFIES:
     * EFFECTS: Takes in a month and returns a double[] which stores the percentage out of 100 that each category
     *          represents in respect to the total expenses in that month. Uses dayMonthTracker to quickly access
     *          the index of the start of the target month.
     */
    public double[] getCategoryPercentagesPerMonth(int month) {
        expenseList.sort(null);
        dayMonthTracker.sort(null);
        double[] categoryPercentage = new double[expenseList.get(0).getDefaultCategories().size()];
        double sum = 0;
        Double thisMonth = (Double)(double)month;
        Double nextMonth = (Double)(double)(month + 1);
        for (int i = 0; i < categoryPercentage.length; i++) {
            for (int j = dayMonthTracker.indexOf(thisMonth) + 1; j < dayMonthTracker.indexOf(nextMonth); j++) {
                if (categoryComparator(j - month, i)) {
                    sum = sum + expenseList.get(j - month).getPrice(); // -month to recalibrate the index
                }
            }
            sum = Math.round(((sum / calculateMonthlyExpense(month)) * 100) * 100.0) / 100.0; //Rounds to 2 decimals
            categoryPercentage[i] = sum;
            sum = 0;
        }
        return categoryPercentage;
    } //add empty list checker (throws exception when list is empty)

    /*
     * REQUIRES: j =< 0, i =< 0
     * MODIFIES:
     * EFFECTS: Helper method for getCategoryPercentagesPerMonth(), returns true if the category at
     *          expenseList.get(dayIndex) and defaultCategories.get(categoryIndex) are the same. Otherwise, returns
     *          false.
     */
    private boolean categoryComparator(int j, int i) { //j = dayIndex, i = categoryIndex (names too long)
        return expenseList.get(j).getCategory().equalsIgnoreCase(expenseList.get(0).getDefaultCategories().get(i));
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("User Name", userName);
        json.put("Expense List", expenseListToJson());
        json.put("BorrowLend List", borrowLendListToJson());
        //json.put("DayMonth Tracker", dayMonthTrackerToJson());
        return json;
    }

    //Effects: returns the Expenses in expenseList as a JSONArray
    public JSONArray expenseListToJson() {
        JSONArray jsonArray = new JSONArray();
        for (Expense expense : expenseList) {
            jsonArray.put(expense.toJson());
        }
        return jsonArray;
    }

    //Effects: returns the BorrowLends in borrowLendList as a JSONArray
    public JSONArray borrowLendListToJson() {
        JSONArray jsonArray = new JSONArray();
        for (BorrowLend b : borrowLendList) {
            jsonArray.put(b.toJson());
        }
        return jsonArray;
    }

}
