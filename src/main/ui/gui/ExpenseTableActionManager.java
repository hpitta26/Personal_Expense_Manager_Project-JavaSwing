package ui.gui;

import model.Expense;
import model.ExpenseList;
import ui.exceptions.InvalidInputException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

//Class used to manage the GUI's Expense JTable Panel
public class ExpenseTableActionManager {
    private int enterCount;
    //Expense Attributes
    private int month;
    private int day;
    private String description;
    private double price;
    private String category;
    //boolean borrowLend; //will be added later

    private InputChecker inputChecker;
    private JLabel questionLabel;
    private JLabel[] outputTestLabels; //contains all the outputTestLabels
    private JTextField textField;

    private ExpenseList expenseList;
    private DefaultTableModel expenseTableEditor;

    private DefaultTableModel summaryTableEditor;

    public ExpenseTableActionManager(JLabel label, JLabel[] testLabels, JTextField textField, ExpenseList expenseList,
                                     DefaultTableModel tableEditor, DefaultTableModel summaryEditor) {
        enterCount = 1;
        this.textField = textField;
        this.questionLabel = label;
        this.outputTestLabels = testLabels;
        this.expenseList = expenseList;
        this.expenseTableEditor = tableEditor;
        this.summaryTableEditor =  summaryEditor;
        inputChecker = new InputChecker(textField, questionLabel);
        //currentExpense = new Expense("School", 0, "",1, 1);
    }

    public void actionManagerRunner() {
        textFieldActionHelper();

        if (enterCount == 6) {
            //call remove expense before adding
            try {
                boolean answer = helpRemoveObject(true);
                if (answer) {
                    addExpenseToTable();
//                    expenseList.addExpense(new Expense(category, price, description, month, day));
//                    Object[] attributes = new Object[] {month + "/" + day, description, price, category};
//                    expenseTableEditor.addRow(attributes); //change later to insert below the day in a month.
                }
                resetPanel();
            } catch (InvalidInputException e) {
                //nothing
            }
        }
    }

    private void textFieldActionHelper() {
        switch (enterCount) {
            case 1:
                //Ask for Day
                dayHelper();
                break;
            case 2:
                //Ask for Description
                descriptionHelper();
                break;
            case 3:
                //Ask for Price
                priceHelper();
                break;
            case 4:
                //Ask for Category
                categoryHelper();
                break;
            case 5:
                //Ask for Month
                monthHelper();
                break;
        }
    }

    private void dayHelper() {
        try {
            day = inputChecker.dayInputChecker();
            questionLabel.setText("Current Input: Description");

            outputTestLabels[0].setText(Integer.toString(day));
            enterCount++;
        } catch (InvalidInputException e) {
            questionLabel.setText("Current Input: Day (Previous Input was Invalid)");
        }
        textField.setText("");
    }

    private void descriptionHelper() {
        description = textField.getText();
        questionLabel.setText("Current Input: Price");
        outputTestLabels[1].setText(description);
        enterCount++;
        textField.setText("");
    }

    private void priceHelper() {
        try {
            price = inputChecker.priceInputChecker();
            questionLabel.setText("Current Input: Category");
            outputTestLabels[2].setText(Double.toString(price));
            enterCount++;
        } catch (InvalidInputException e) {
            questionLabel.setText("Current Input: Price (Previous Input was Invalid)");
        }
        textField.setText("");
    }

    private void categoryHelper() {
        category = textField.getText();
        questionLabel.setText("Current Input: Month");
        outputTestLabels[3].setText(category);
        enterCount++;
        textField.setText("");
    }

    private void monthHelper() {
        try {
            month = inputChecker.monthInputChecker();
            //questionLabel.setText("Current Input: Day");
            questionLabel.setText("Are you sure you want to add this Expense (T)/(F)?");
            outputTestLabels[4].setText(Integer.toString(month));
            enterCount++;
        } catch (InvalidInputException e) {
            questionLabel.setText("Current Input: Month (Previous Input was Invalid)");
        }
        textField.setText("");
    }

    //Modifies: this
    //Effects: Asks the user if they mistyped the previous Item, if they did it removes that Item from its respective
    //         list.
    public boolean helpRemoveObject(boolean expenseOrBorrow) throws InvalidInputException {
        boolean answer;
        try {
            answer = inputChecker.trueFalseChecker();
            //questionLabel.setText("Current Input: Day");
        } catch (InvalidInputException e) {
            questionLabel.setText("Are you sure you want to add this Expense (T)/(F)?");
            textField.setText("");
            throw new InvalidInputException("Invalid TrueOrFalse");
        }
        textField.setText("");
        return answer;
    }

    public void resetPanel() {
        updateSummaryTable();

        questionLabel.setText("Current Input: Day");
        enterCount = 1;
        outputTestLabels[0].setText("Day: ");
        outputTestLabels[1].setText("Description: ");
        outputTestLabels[2].setText("Price: ");
        outputTestLabels[3].setText("Category: ");
        outputTestLabels[4].setText("Month: Only 11");
    }

    //EFFECTS:
    public void addExpenseToTable() {
        //used to add expenses to the table, can be used after loading the app to restore GUI state
        expenseList.addExpense(new Expense(category, price, description, month, day));
        //Object[] attributes = new Object[] {month + "/" + day, description, price, category};
        Object[] attributes = new Object[] {day, description,"$" + price, category};
        insertCustomRowHelper(attributes, month);
        //expenseTableEditor.addRow(attributes); //change later to insert below the day in a month.
    }

    public void insertCustomRowHelper(Object[] y, int mon) {
        Integer day = (Integer) y[0];
        if (mon == 11) {
            for (int i = 0; i < expenseTableEditor.getRowCount(); i++) {
                if (expenseTableEditor.getValueAt(i, 0) == day) {
                    //row = i;
                    y[0] = "";
                    expenseTableEditor.insertRow(i + 1, y);
                    break;
                }
            }
        } else {
            y[0] = mon + "/" + day;
            expenseTableEditor.addRow(y);
        }
    }

    public void addExpenseRestore(String cat, double p, String des, int mon, int d) {
        //Object[] attributes = new Object[] {mon + "/" + d, des, p, cat};
        Object[] attributes = new Object[] {d, des,"$" + p, cat};
        //expenseTableEditor.addRow(attributes);
        insertCustomRowHelper(attributes, mon);
    }

    public void updateSummaryTable() { //11 rows, 3 columns
        double[] percentages = expenseList.getCategoryPercentagesPerMonth(11);
        double[] totals = expenseList.getCategoryTotalPerMonth(11);
        double total = 0;

        for (int i = 0; i < percentages.length; i++) {
            total = total + totals[i];
            summaryTableEditor.setValueAt("$" + totals[i], i, 1);
            summaryTableEditor.setValueAt(percentages[i] + "%", i, 2);
        }

        summaryTableEditor.setValueAt("$" + total, 10, 1);
        summaryTableEditor.setValueAt("100%", 10, 2); //change this to actual percentage
        //change this method to only change the target Category (not all the rows)
    }

    //EFFECTS: updates the expenseList after load is called
    //MODIFIES: this
    public void setExpenseList(ExpenseList newExpenseList) {
        this.expenseList = newExpenseList;
    }
}
