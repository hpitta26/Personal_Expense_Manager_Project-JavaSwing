package persistence;

import model.BorrowLend;
import model.Expense;
import model.ExpenseList;
import org.json.JSONArray;
import org.json.JSONObject;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

//Used JsonSerializationDemo as a template

//Object that reads JSONObjects from a file
public class JsonReader {
    private String source;

    //Effects: Creates the JsonReader with a source directory
    public JsonReader(String source) {
        this.source = source;
    }

    //Effects: Reads ExpenseList from source file, and returns that ExpenseList. Throws IOException if there is an error
    //          reading the file.
    public ExpenseList read() throws IOException {
        String jsonData = readFile(source);
        JSONObject jsonObject = new JSONObject(jsonData);
        return parseExpenseList(jsonObject);
    }

    //Effects: Reads source file as String and returns it
    public String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    //Effects: Sets the read JSONObject equal to ExpenseList, and returns that ExpenseList.
    private ExpenseList parseExpenseList(JSONObject jsonObject) {
        ExpenseList expenseList = new ExpenseList();
        expenseList.setUserName(jsonObject.getString("User Name"));
        addExpenseList(expenseList, jsonObject);
        addBorrowLendList(expenseList, jsonObject);
        return expenseList;
    }

    //Modifies: expenseList
    //Effects: Iterates through ExpenseList's JSONArray attribute to all the individual Expense Objects to the
    //         ExpenseList
    private void addExpenseList(ExpenseList expenseList, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("Expense List");
        for (Object json : jsonArray) {
            JSONObject nextExpense = (JSONObject) json;
            addExpense(expenseList, nextExpense);
        }
    }

    //Modifies: expenseList
    //Effects: Creates an Expense from the attributes in a specific JSONArray index and adds that to the expenseList
    private void addExpense(ExpenseList expenseList, JSONObject jsonObject) {
        String category = jsonObject.getString("Category");
        double price = jsonObject.getDouble("Price");
        String description = jsonObject.getString("Description");
        int month = jsonObject.getInt("Month");
        int day = jsonObject.getInt("Day");
        Expense expense = new Expense(category, price, description, month, day);
        expenseList.addExpense(expense);
    }

    private void addBorrowLendList(ExpenseList expenseList, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("BorrowLend List");
        for (Object json : jsonArray) {
            JSONObject nextBorrowLend = (JSONObject) json;
            addBorrowLend(expenseList, nextBorrowLend);
        }
    }

    private void addBorrowLend(ExpenseList expenseList, JSONObject jsonObject) {
        String name = jsonObject.getString("Name");
        double amount = jsonObject.getDouble("Amount");
        String description = jsonObject.getString("Description");
        int month = jsonObject.getInt("Month");
        int day = jsonObject.getInt("Day");
        boolean borrowOrLend = jsonObject.getBoolean("BorrowLend");
        BorrowLend borrowLend = new BorrowLend(name, amount, description, month, day, borrowOrLend);
        expenseList.addBorrowLend(borrowLend);
    }
}
