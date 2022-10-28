package model;

import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

//Used JsonSerializationDemo as template for toJson() methods

//Represents an Expense that has 5 attributes: category, price (dollars), description, month, and day
public class Expense implements Comparable<Expense>, Writable {
    private static int countNumOfCustomCategories = 0; //tracks # of custom categories created (max is 3)
    private static List<String> defaultCategories =
            new ArrayList<>(Arrays.asList("Groceries", "Dorm", "School", "Restaurant", "Entertainment", "Random"));
    //represents the default categories initially given to the user, the user can only create 3 custom categories
    private String category;        //Expense category
    private double price;           //Expense price
    private String description;     //Expense description (what the user bought)
    private int month;              //Expense month (what month the user bought the item)
    private int day;                //Expense day (what day the user bought the item)

    /*
     * REQUIRES: price >= 0, 0 < month < 13, 0 < day < 31
     * EFFECTS: The expense category is checked to see if it is a default category, if it is not then
     *          that category is added to the default list and countNumOfCustomCategories is incremented.
     *          If the price is negative then the price is set to zero, otherwise it is set the value specified by the
     *          user. Day and month are checked to see if they are less than 1 or higher than 31 and 12 respectively,
     *          if they are, they are set to 1. Otherwise, they are set to the value specified by the user.
     */
    public Expense(String category, double price, String description, int month, int day) {
        this.category = checkIfCategoryIsNotDefault(category);
        //will print a message if it set the category to Random, will ask if you want to swap it.
        setPrice(price);
        this.description = description;
        setMonth(month);
        setDay(day);
    }

    /*
     * REQUIRES: Valid category name (not 1, or iauhsdiuahsiudh)
     * MODIFIES: this
     * EFFECTS: Takes in a category and checks if that category is already in the List defaultCategories.
     *          If it is it returns the category passed in and does not change anything. If it isn't then it checks
     *          if countNumOfCustomCategories < 3 is true. If it is then it adds the category to the List
     *          defaultCategories and returns the category. If it's false then the maximum number of custom categories
     *          has already been reached and the Expense's category is set to "Random".
     */
    public String checkIfCategoryIsNotDefault(String category) {
        for (int i = 0; i < defaultCategories.size(); i++) {
            if (category.equalsIgnoreCase(defaultCategories.get(i))) {
                return category;
            }
        }
        if (countNumOfCustomCategories < 3) { //max amount of custom categories is 3
            countNumOfCustomCategories++;
            String formatCat = "";
            if (category.length() > 1) {
                formatCat = category.substring(0, 1).toUpperCase() + category.substring(1);
            } else {
                formatCat = category.toUpperCase();
            }
            defaultCategories.add(formatCat);
            //adds custom category to default so if the user repeats that category it will not be counted as custom
            return formatCat;
        }
        return "Random";
    }

    /*
     * REQUIRES:
     * MODIFIES:
     * EFFECTS: Overrides Comparable's method compareTo(Object o) so that a List of Expenses can be sorted solely based
     *          on their dates.
     *          Takes in an Expense Object as a parameter and compares this Expense to the one passed in. Expenses
     *          are compared by month and day so (month=10,day=9 > month=9,day=10). If this Expense has a later date
     *          than the one passed in the method returns 1, if it has an earlier date it returns -1. If they have the
     *          same date it returns 0;
     */
    @Override //Sorts Expenses by their date
    public int compareTo(Expense expense) {
        if (getMonth() > expense.getMonth()) {
            return 1;
        } else if (getMonth() < expense.getMonth()) {
            return -1;
        } else {
            if (getDay() > expense.getDay()) {
                return 1;
            } else if (getDay() < expense.getDay()) {
                return -1;
            }
        }
        return 0;
    }

    public int getMonth() {
        return this.month;
    }

    public void setMonth(int month) {
        if (month < 1 | month > 12) {
            this.month = 1;
        } else {
            this.month = month;
        }
    }

    public int getDay() {
        return this.day;
    }

    public void setDay(int day) {
        if (day < 1 | day > 31) {
            this.day = 1;
        } else {
            this.day = day;
        }
    }

    public double getPrice() {
        return this.price;
    }

    public void setPrice(double price) {
        if (price < 0) {
            this.price = 0;
        } else {
            this.price = price;
        }
    }

    public String getCategory() {
        return this.category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getCountNumOfCustomCategories() {
        return countNumOfCustomCategories;
    }

    public List<String> getDefaultCategories() {
        return defaultCategories;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Category",category);
        json.put("Price",price);
        json.put("Description",description);
        json.put("Month",month);
        json.put("Day",day);
        return json;
    }
}
