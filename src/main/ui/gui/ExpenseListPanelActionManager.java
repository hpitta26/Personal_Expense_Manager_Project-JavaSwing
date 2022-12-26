package ui.gui;

import model.Expense;
import model.ExpenseList;
import ui.SummaryPanelGUI;
import ui.exceptions.InvalidInputException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.ArrayList;

//Class used by ExpenseListPanelGUI to manage the GUI's monthGraphs and expenseTable
public class ExpenseListPanelActionManager {


    // ------------------------------------- INSTANCE VARIABLES --------------------------------------------


    //How many times the User has clicked the Button
    private int enterCount;
    private int currentMonth; //only currentMonth Expenses are displayed

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

    private Container screen;
    private SummaryPanelGUI summaryPanelReference;

    private MonthGraphsActionManager monthGraphs;


    // ------------------------------------- CONSTRUCTOR --------------------------------------------


    //EFFECTS: initializes helper class with fields
    //MODIFIES: this
    public ExpenseListPanelActionManager(JLabel label, JLabel[] testLabels, JTextField textField,
                                         ExpenseList expenseList, DefaultTableModel tableEditor, Container screen,
                                         SummaryPanelGUI s) {
        currentMonth = 11; //tracks what month should be displayed (make this a constructor parameter)

        enterCount = 1;
        this.textField = textField;
        this.questionLabel = label;
        this.outputTestLabels = testLabels;
        this.expenseList = expenseList;
        this.expenseTableEditor = tableEditor;
        //this.summaryTableEditor =  summaryEditor;
        inputChecker = new InputChecker(textField, questionLabel);

        this.screen = screen;
        this.summaryPanelReference = s;
        this.monthGraphs = new MonthGraphsActionManager(screen, expenseList, new int[] {11, 10, 9});
    }

    //EFFECTS: updates the expenseList after load is called
    //MODIFIES: this
    public void setExpenseList(ExpenseList newExpenseList) {
        this.expenseList = newExpenseList;
    }

    //EFFECTS: Manages the whole process of prompting the user to enter values for a new Expense
    //MODIFIES:
    public void actionManagerRunner() {
        textFieldActionHelper();

        if (enterCount == 6) {
            //call remove expense before adding
            try {
                boolean answer = helpRemoveObject(true);
                if (answer) {
                    addExpenseToTable();
                    monthGraphs.updateDataSet(getMonthsForGraphs(currentMonth));
                }
                resetPanel();
            } catch (InvalidInputException e) {
                //nothing
            }
        }
    }

    //EFFECTS: updates the month graphs when a button is pressed
    //MODIFIES: this
    public void changeDisplayedMonthOnGraphs(int newMonth) {
        //see if you need to update the instance of monthGraphs
        //monthGraphs.updateDataSet(getMonthsForGraphs(newMonth), false);
        monthGraphs.changeDisplayedMonth(getMonthsForGraphs(newMonth));



        monthGraphs.updateDataSet(getMonthsForGraphs(newMonth));
    }

    public int[] getMonthsForGraphs(int month) {
        if (month == 1) {
            return new int[] {1, 12, 11};
        } else if (month == 2) {
            return new int[] {2, 1, 12};
        } else {
            return new int[] {month, month - 1, month - 2};
        }
    }

    //EFFECTS: Keeps track of which field the User is supposed to be asked about
    //MODIFIES:
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

    //EFFECTS: Prompts the user to enter a correct day for their new Expense
    //MODIFIES: this
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

    //EFFECTS: Prompts the user to enter a description for their new Expense
    //MODIFIES: this
    private void descriptionHelper() {
        description = textField.getText();
        questionLabel.setText("Current Input: Price");
        outputTestLabels[1].setText(description);
        enterCount++;
        textField.setText("");
    }

    //EFFECTS: Prompts the user to enter a correct price for their new Expense
    //MODIFIES: this
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

    //EFFECTS: Prompts the user to enter a category for their new Expense
    //MODIFIES: this
    private void categoryHelper() {
        category = textField.getText();
        questionLabel.setText("Current Input: Month");
        outputTestLabels[3].setText(category);
        enterCount++;
        textField.setText("");
    }

    //EFFECTS: Prompts the user to enter a correct month for their new Expense
    //MODIFIES: this
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

    //EFFECTS: Resets the Labels and Updates the App when a new Expense is added, only updatesApp if there is a new
    // Expense
    //MODIFIES: this
    public void resetPanel() {

        questionLabel.setText("Current Input: Day");
        enterCount = 1;
        outputTestLabels[0].setText("Day: ");
        outputTestLabels[1].setText("Description: ");
        outputTestLabels[2].setText("Price: ");
        outputTestLabels[3].setText("Category: ");
        outputTestLabels[4].setText("Month: Only 11");

    }


    //THE FOLLOWING METHODS CHANGE THE EXPENSE TABLE


    //EFFECTS: Used to insert Expenses at the correct position, during runtime & update summary table and graphs
    //MODIFIES:
    public void addExpenseToTable() {
        //used to add expenses to the table, can be used after loading the app to restore GUI state
        expenseList.addExpense(new Expense(category, price, description, month, day));
        //Object[] attributes = new Object[] {month + "/" + day, description, price, category};

        if (currentMonth == month) {
//            insertCustomRowHelper(attributes, month);
            Object[] attributes = new Object[] {day, description,"$" + price, category};
            insertCustomRowHelper(attributes);

            summaryPanelReference.updateSummaryTable(currentMonth); //updates the summaryTable
        }

        //expenseTableEditor.addRow(attributes); //change later to insert below the day in a month.
    }

    public void addExpenseRestore() {

    }

    //EFFECTS: Restores the state of the App when loaded from file, inserts expenses in the correct position
    //MODIFIES:
    public void addExpenseRestore(String cat, double p, String des, int d) {

        Object[] attributes = new Object[] {d, des,"$" + p, cat};
        insertCustomRowHelper(attributes);
    }


    //EFFECTS: Restores the state of the App when loaded from file, inserts expenses in the correct position
    //MODIFIES:
    public void loadExpenseTable(ExpenseList newExpenseList) {
        setExpenseList(newExpenseList); //updates expenseList reference

        if (newExpenseList != null) {
            ArrayList<Expense> monthExpenses = newExpenseList.getExpenseListForTargetMonth(currentMonth);
            for (Expense e : monthExpenses) {

                Object[] attributes = new Object[] {e.getDay(), e.getDescription(), "$" + e.getPrice(),
                        e.getCategory()};
                insertCustomRowHelper(attributes);
            }
        }


        monthGraphs.loadMonthGraphs(newExpenseList, getMonthsForGraphs(currentMonth));

    }

    //EFFECTS: Only inserts expense if it's in the current Month
    //MODIFIES: this
    public void insertCustomRowHelper(Object[] y) {
        Integer day = (Integer) y[0];

        for (int i = 0; i < expenseTableEditor.getRowCount(); i++) {
            if (expenseTableEditor.getValueAt(i, 0) == day) {
                //row = i;
                y[0] = "";
                expenseTableEditor.insertRow(i + 1, y);
                break;
            }
        }
    }

    public void updateCurrentMonth(int newMonth) {
        currentMonth = newMonth;
    }

}

