package ui.gui;

import model.ExpenseList;
import javax.swing.table.DefaultTableModel;
import java.util.List;


//Class used by SummaryPanelGUI to manage the GUI's summaryTable
public class SummaryPanelActionManager {


    // ------------------------------------- INSTANCE VARIABLES --------------------------------------------


    private ExpenseList expenseList;
    private DefaultTableModel summaryTableEditor;


    // ------------------------------------- CONSTRUCTOR --------------------------------------------


    //EFFECTS: initializes helper class with fields
    //MODIFIES: this
    public SummaryPanelActionManager(ExpenseList expenseList, DefaultTableModel summaryEditor) {

        this.expenseList = expenseList;
        this.summaryTableEditor =  summaryEditor;

    }


    // ------------------------------------- UPDATE TABLE --------------------------------------------


    //EFFECTS: updates the Summary Table
    //MODIFIES: this
    public void updateSummaryTable(int currentMonth) {
        List<String> defaultCategories = expenseList.getExpenseList().get(0).getDefaultCategories();
        double[] percentages = expenseList.getCategoryPercentagesPerMonth(currentMonth);
        double[] totals = expenseList.getCategoryTotalPerMonth(currentMonth);
        double total = 0;

        for (int i = 0; i < defaultCategories.size(); i++) {
            summaryTableEditor.setValueAt(defaultCategories.get(i), i, 0);
        }

        //Summary Table = 11 rows, 3 columns
        for (int i = 0; i < percentages.length; i++) {
            total = total + totals[i];
            summaryTableEditor.setValueAt("$" + totals[i], i, 1);
            summaryTableEditor.setValueAt(percentages[i] + "%", i, 2);
        }

        summaryTableEditor.setValueAt("$" + total, 10, 1);
        summaryTableEditor.setValueAt("100%", 10, 2); //change this to actual percentage


        //change this method to only change the target Category (not all the rows)
    }


    // ------------------------------------- LOAD TABLE --------------------------------------------


    //EFFECTS: Called when a user loads their App from file.
    //MODIFIES: this
    public void loadSummaryTable(ExpenseList newExpenseList, int currentMonth) {
        this.expenseList = newExpenseList; //update expenseList reference
        updateSummaryTable(currentMonth);
    }
}

