package model;

// Represents a general Personal Expense Object, that has default behaviour Expense, BorrowLend...
public abstract class PersonalExpenseGeneral implements Comparable<PersonalExpenseGeneral> {
    private double price;           //Expense price
    private String description;     //Expense description (what the user bought)
    private int month;              //Expense month (what month the user bought the item)
    private int day;                //Expense day (what day the user bought the item)

    public PersonalExpenseGeneral(double price, String description, int month, int day) {
        setPrice(price);
        this.description = description;
        setMonth(month);
        setDay(day);
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
    @Override
    public int compareTo(PersonalExpenseGeneral personalExpense) {
        if (getMonth() > personalExpense.getMonth()) {
            return 1;
        } else if (getMonth() < personalExpense.getMonth()) {
            return -1;
        } else {
            if (getDay() > personalExpense.getDay()) {
                return 1;
            } else if (getDay() < personalExpense.getDay()) {
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

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
