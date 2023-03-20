package ui;

import model.Expense;
import model.ExpenseList;
import ui.gui.ExpenseListPanelActionManager;
import ui.gui.InputChecker;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

//Class that is used to Manage the expenseListPanel and the textFields
public class ExpenseListPanelGUI implements ActionListener {


    // ------------------------------------ INSTANCE VARIABLES --------------------------------------------


    private ExpenseList expenseList;

    private JPanel expenseListPanel;
    private JPanel textFieldAndButton;

    private DefaultTableModel tableEditor; //used to edit the Expense Table
    private JTable expenseTable;

    private JTextField textField;
    private JComboBox<String> comboBox; //Add Expense or BorrowLend
    private JLabel questionLabel; //replace this with HintText
    private JLabel[] testLabelArray; //day, description, price, category, month

    //Helper classes
    private InputChecker inputChecker;
    private ExpenseListPanelActionManager tableActionManager;

    //Other Panels' Reference
    private SummaryPanelGUI summaryPanelReference;


    // ------------------------------------ CONSTRUCTOR --------------------------------------------


    public ExpenseListPanelGUI(Container screen, ExpenseList expenseList, SummaryPanelGUI s) {
        this.expenseList = expenseList;
        this.summaryPanelReference = s;

        createNewPanel();

        inputChecker = new InputChecker(textField, questionLabel);
        tableActionManager =  new ExpenseListPanelActionManager(questionLabel, testLabelArray, textField, expenseList,
                tableEditor, screen, s);

        screen.add(expenseListPanel, BorderLayout.CENTER); //Add expenseListPanel to screen.CENTER
    }

    //EFFECTS: Creates the master JPanel that holds the borrowLend, and summaryTable JPanel
    //MODIFIES: this
    public void createNewPanel() {
        expenseListPanel = new JPanel();
        expenseListPanel.setLayout(new GridBagLayout());

        addExpenseJTable(); //Adds ExpenseListTable
        helpCreateTextFieldAndComboBox(); //Adds ComboBox and TextField

    }


    // ------------------------------------ EXPENSE TABLE --------------------------------------------


    //EFFECTS: Creates the JTable that will display the Expenses to the User
    //MODIFIES: this
    public void addExpenseJTable() {
        //create and customize JTable here
        GridBagConstraints gbc2 = new GridBagConstraints();
        String[] colNames = new String[] {"Month/Day", "Description", "Price", "Category"};
        tableEditor = new DefaultTableModel(colNames, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false;
            } //no cells are editable right now
        };
        expenseTable = new JTable(tableEditor);

        configureJTable();

        JScrollPane scrollPaneExpenses = new JScrollPane(expenseTable);
        scrollPaneExpenses.setPreferredSize(new Dimension(730, 570));
        gbc2.gridy = 0;
        expenseListPanel.add(scrollPaneExpenses, gbc2); //Add Expense List @ row 0
    }

    //EFFECTS: Configures the layout of the JTable
    //MODIFIES: this
    public void configureJTable() {
        expenseTable.setShowGrid(true);
        expenseTable.setGridColor(new Color(000));
        expenseTable.getTableHeader().setFont(new Font("Monospaced", Font.PLAIN, 13));
        expenseTable.setRowHeight(20);
        expenseTable.getColumnModel().getColumn(0).setPreferredWidth(100);
        expenseTable.getColumnModel().getColumn(1).setPreferredWidth(400);
        expenseTable.getColumnModel().getColumn(2).setPreferredWidth(100);
        expenseTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        for (int i = 1; i < 31; i++) {
            tableEditor.addRow(new Object[] {i, "", "", ""});
        }
    }


    // ------------------------------------ INPUT PANEL --------------------------------------------


    //EFFECTS: Helper method to help add the ComboBox and TextField to the expenseListPanel
    //MODIFIES: this
    public void helpCreateTextFieldAndComboBox() {
        GridBagConstraints gbc2 = new GridBagConstraints();
        String[] textOptions = new String[] {"Add Expense", "Add Borrow/ Lend"};
        comboBox = new JComboBox<>(textOptions);
        comboBox.setPreferredSize(new Dimension(730, 30));
        comboBox.setBorder(new EtchedBorder());
        gbc2.gridy = 1;
        expenseListPanel.add(comboBox, gbc2); //Add ComboBox @ row 1

        questionLabel = new JLabel("Current Input: Day"); //Change this to a slider for each month
        questionLabel.setFont(new Font("Monospaced", Font.ITALIC, 12));
        questionLabel.setPreferredSize(new Dimension(730, 20));
        gbc2.gridy = 2;
        expenseListPanel.add(questionLabel, gbc2); //Add questionLabel @ row 2 (gives the user the questions)

        helpCreateTextFieldButtonPanel();
        gbc2.gridy = 3;
        expenseListPanel.add(textFieldAndButton, gbc2); //Add TextFieldAndButton @ row 3

        JPanel testLabels = helpAddTestLabels();
        gbc2.gridy = 4;
        expenseListPanel.add(testLabels, gbc2); //Add testLabels @ row 4
    }

    //EFFECTS: helper method to help create a Panel that has the JTextField and an "Enter" JButton
    //MODIFIES: this
    public void helpCreateTextFieldButtonPanel() {
        textFieldAndButton = new JPanel();
        textFieldAndButton.setLayout(new FlowLayout());
        textField = new JTextField("Hello type input here");
        textField.setBorder(new EtchedBorder());
        textField.setPreferredSize(new Dimension(630, 30));
        textField.setActionCommand("TextFieldEnter");
        textField.addActionListener(this);
        textFieldAndButton.add(textField);

        JButton resetButton = new JButton("Reset");
        //button1.setBorder(new EtchedBorder());
        resetButton.setPreferredSize(new Dimension(100,30));
        resetButton.setActionCommand("ResetButton");
        resetButton.addActionListener(this); //actionPerformed is called everytime the button is clicked
        textFieldAndButton.add(resetButton);
    }

    //EFFECTS: creates JPanel for the Labels that will display the user inputs for testing (will change later)
    //MODIFIES: this
    public JPanel helpAddTestLabels() {
        JPanel testLabels = new JPanel();
        testLabels.setLayout(new FlowLayout());
        JLabel dayOutputTest = new JLabel("Day: ");
        JLabel descriptionOutputTest = new JLabel("Description: ");
        JLabel priceOutputTest = new JLabel("Price: ");
        JLabel categoryOutputTest = new JLabel("Category: ");
        JLabel monthOutputTest = new JLabel("Month: ");

        labelStyleSetter(dayOutputTest);
        labelStyleSetter(descriptionOutputTest);
        labelStyleSetter(priceOutputTest);
        labelStyleSetter(categoryOutputTest);
        labelStyleSetter(monthOutputTest);

        testLabels.add(dayOutputTest);
        testLabels.add(descriptionOutputTest);
        testLabels.add(priceOutputTest);
        testLabels.add(categoryOutputTest);
        testLabels.add(monthOutputTest);
        testLabelArray = new JLabel[] {dayOutputTest, descriptionOutputTest, priceOutputTest, categoryOutputTest,
                monthOutputTest};
        return testLabels;
    }

    //EFFECTS: changes the style of the JLabel
    //MODIFIES: this
    public void labelStyleSetter(JLabel label) {
        label.setPreferredSize(new Dimension(143,20));
        label.setBorder(new EtchedBorder());
    }


    // ------------------------------------ RUNTIME FUNCTIONS --------------------------------------------


    //EFFECTS: changes the month displayed by this panel, when a JButton is clicked
    //MODIFIES: this
    public void changeDisplayedMonth(int newMonth) {
        //String[] colNames = new String[] {"Month/Day", "Description", "Price", "Category"};

        updateMonthGraphs(newMonth); //update monthGraphs to new month

        tableEditor.setRowCount(0);
        for (int i = 1; i < 31; i++) {
            tableEditor.addRow(new Object[] {i, "", "", ""});
        }
        tableActionManager.updateCurrentMonth(newMonth);

        ArrayList<Expense> newMonthExpenses = expenseList.getExpenseListForTargetMonth(newMonth);
        for (Expense e : newMonthExpenses) {
            String cat = e.getCategory();
            double p = e.getPrice();
            String des = e.getDescription();
            int d = e.getDay();
            tableActionManager.addExpenseRestore(cat, p, des, d);

        }

    }

    //EFFECTS: helper method used to update the month graphs
    //MODIFIES: this
    public void updateMonthGraphs(int newMonth) {
        tableActionManager.changeDisplayedMonthOnGraphs(newMonth);
    }

    //EFFECTS: helps load the Application from a file, when the load button is clicked
    //MODIFIES: this
    public void loadApplication(ExpenseList newExpenseList) {

        this.expenseList = newExpenseList; //updates expenseList reference
        tableActionManager.loadExpenseTable(newExpenseList);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("TextFieldEnter")) {
            tableActionManager.actionManagerRunner();
        }
        if (e.getActionCommand().equals("ResetButton")) {
            tableActionManager.resetPanel();
        }
    }
    //reset button action listener
    //text field action command
}
