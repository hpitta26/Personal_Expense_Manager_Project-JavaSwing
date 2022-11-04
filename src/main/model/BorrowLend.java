package model;

//Used JsonSerializationDemo as template for toJson() methods

import org.json.JSONObject;
import persistence.Writable;

//Represents an amount that the user borrowed/lent to another person, so the user can track who owes them/ who they owe
public class BorrowLend extends PersonalExpenseGeneral implements Writable {
    private String name;
    private boolean borrowLend;

    //Requires: amount >= 0, 0 < month < 13, 0 < day < 31
    //Effects: Creates a new BorrowLend object with parameters specified by the user
    public BorrowLend(String name, double amount, String description, int month, int day, boolean borrowLend) {
        super(amount, description, month, day);
        this.name = name;
        this.borrowLend = borrowLend;
    }

    //Effects: String representation of BorrowLend object
    @Override
    public String toString() {
        String borrowLend;
        if (getBorrowLend() == true) {
            borrowLend = "Borrowed $" + getPrice() + " from " + name + " on " + getMonth() + "/" + getDay();
        } else {
            borrowLend = "Lent $" + getPrice() + " to " + name + " on " + getMonth() + "/" + getDay();
        }
        return borrowLend;
    }

    public boolean getBorrowLend() {
        return this.borrowLend;
    }

    public void setBorrowLend(boolean borrowLend) {
        this.borrowLend = borrowLend;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("Name",name);
        json.put("Amount",getPrice());
        json.put("Description",getDescription());
        json.put("Month",getMonth());
        json.put("Day",getDay());
        json.put("BorrowLend",borrowLend);
        return json;
    }

}
