package ui;

import model.BorrowLend;
import model.Expense;
import model.ExpenseList;
import persistence.JsonReader;
import persistence.JsonWriter;
import ui.gui.ExpenseTableActionManager;
import ui.gui.InputChecker;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Represents the PersonalExpenseApp
// Allows the user to interact with the GUI to add Expenses and view summaries.
public class PersonalExpenseAppGUI extends JFrame implements ActionListener {
    private JList list1;
    private JList list2;
    private ExpenseList expenseList;
    private String[] testList;
    private int enterCount = 1;

    Container screen;
    JPanel monthPanel;
    JPanel expenseListPanel;
    JPanel borrowLendPanel;
    JPanel textFieldAndButton;

    DefaultTableModel tableEditor; //used to edit the Expense Table
    DefaultTableModel summaryTableEditor; //used to edit the Summary Table
    JTable expenseTable;
    JTextField textField;
    JComboBox<String> comboBox; //Add Expense or BorrowLend
    JLabel questionLabel; //replace this with HintText

    //Helper classes
    InputChecker inputChecker;
    ExpenseTableActionManager actionManagerTable;

    JLabel[] testLabelArray; //day, description, price, category, month

    private static final String JSON_STORE = "./data/expenseListGUI.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private int loadCount;

    //EFFECTS: initializes the GUI
    public PersonalExpenseAppGUI() {
        super("Personal Expense Manager");
        setSize(1000,800);
        //setLocation(1, 1);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        expenseList = new ExpenseList();

        screen = this.getContentPane();
        screen.setLayout(new BorderLayout(0, 0));

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        loadCount = 0;

        createMonthPanel();

        createExpenseListPanel();

        createBorrowLendPanel();
        inputChecker = new InputChecker(textField, questionLabel);
        actionManagerTable =  new ExpenseTableActionManager(questionLabel, testLabelArray, textField, expenseList,
                tableEditor, summaryTableEditor);

        setVisible(true); //allows the contents to be seen, must be at the end of the constructor, or main method
    }

    //EFFECTS: creates monthPanel and places it at screen.NORTH
    //MODIFIES: this
    public void createMonthPanel() {
        monthPanel = new JPanel();
        monthPanel.setLayout(new FlowLayout());
        JLabel monthLabel = new JLabel("Month Slider (Future)"); //Change this to a slider for each month
        monthLabel.setFont(new Font("Monospaced", Font.PLAIN, 16));
        monthLabel.setPreferredSize(new Dimension(300, 30));
        //monthLabel.setPreferredSize(new Dimension(100, 30));
        monthPanel.add(monthLabel);
        monthPanel.setBorder(new EtchedBorder());
        screen.add(monthPanel, BorderLayout.NORTH); //Add monthPanel to screen.NORTH
    }

    //EFFECTS: creates the expenseListPanel and places it at screen.CENTER
    //MODIFIES: this
    public void createExpenseListPanel() {
        expenseListPanel = new JPanel();
        expenseListPanel.setLayout(new GridBagLayout());
        addExpenseJTable(); //Adds ExpenseListTable
        helpCreateTextFieldAndComboBox(); //Adds ComboBox and TextField

        screen.add(expenseListPanel, BorderLayout.CENTER); //Add expenseListPanel to screen.CENTER
    }

    //EFFECTS: Creates the JTable that will display the Expenses to the User
    //MODIFIES: this
    public void addExpenseJTable() {
        //create and customize JTable here
        GridBagConstraints gbc2 = new GridBagConstraints();
        String[] colNames = new String[] {"Month/Day", "Description", "Price", "Category"};
        tableEditor = new DefaultTableModel(colNames, 0);
        expenseTable = new JTable(tableEditor);

        configureJTable();

        JScrollPane scrollPaneExpenses = new JScrollPane(expenseTable);
        scrollPaneExpenses.setPreferredSize(new Dimension(730, 570));
        gbc2.gridy = 0;
        expenseListPanel.add(scrollPaneExpenses, gbc2); //Add Expense List @ row 0
    }

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

    //EFFECTS: helper method to help add the ComboBox and TextField to the expenseListPanel
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
        JLabel monthOutputTest = new JLabel("Month: Only 11");

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


    //EFFECTS: Creates the borrowLendPanel and places it at screen.EAST
    //MODIFIES: this
    public void createBorrowLendPanel() {
        borrowLendPanel = new JPanel();
        borrowLendPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JLabel blTitle = new JLabel("Borrow/ Lend");
        blTitle.setFont(new Font("Monospaced", Font.PLAIN, 13));
        blTitle.setSize(250, 50);
        gbc.gridy = 0; //Adds BorrowLend Title @ row 0
        gbc.fill = GridBagConstraints.HORIZONTAL;
        blTitle.setBorder(new EtchedBorder());
        borrowLendPanel.add(blTitle, gbc);

        DefaultListModel<BorrowLend> listModel = createListModel();
        list2 = new JList(listModel);
        JScrollPane scrollPaneBL = new JScrollPane(list2);
        scrollPaneBL.setPreferredSize(new Dimension(250, 300));
        gbc.gridy = 1; //Adds BorrowLend Panel @ row 1
        borrowLendPanel.add(scrollPaneBL, gbc);

        addSummaryJTable(); //Adds summaryTable @ row 2

        addLoadSaveButtons();

        screen.add(borrowLendPanel, BorderLayout.EAST); //Add EAST

    }

    public void addLoadSaveButtons() {
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel loadSave = new JPanel();
        loadSave.setPreferredSize(new Dimension(250, 50));
        loadSave.setLayout(new FlowLayout());

        JLabel summaryTitle = new JLabel(" ");
        summaryTitle.setSize(250, 50);
        gbc.gridy = 4;
//        gbc.fill = GridBagConstraints.HORIZONTAL;
//        gbc.anchor = GridBagConstraints.SOUTH;
//        gbc.weighty = 5;
        borrowLendPanel.add(summaryTitle, gbc); //Creates a gap between Panels

        JButton saveButton = new JButton("Save");
        saveButton.setActionCommand("SaveButton");
        saveButton.addActionListener(this);
        JButton loadButton = new JButton("Load");
        loadButton.setActionCommand("LoadButton");
        loadButton.addActionListener(this);
        gbc.gridy = 5;
        loadSave.add(saveButton);
        loadSave.add(loadButton);
        borrowLendPanel.add(loadSave, gbc);
    }

    //EFFECTS: Creates the JTable that will display the MonthExpenseSummary to the User
    //MODIFIES: this
    public void addSummaryJTable() {
        //create and customize JTable here
        GridBagConstraints gbc2 = new GridBagConstraints();

        JLabel summaryTitle = new JLabel(" ");
        //summaryTitle.setFont(new Font("Monospaced", Font.PLAIN, 13));
        summaryTitle.setSize(250, 50);
        gbc2.gridy = 2;
        gbc2.fill = GridBagConstraints.HORIZONTAL;
        //summaryTitle.setBorder(new EtchedBorder());
        borrowLendPanel.add(summaryTitle, gbc2); //Creates a gap between Panels

        String[] colNames = new String[] {"Category", "Total", "Percent"};
        summaryTableEditor = new DefaultTableModel(colNames, 0) {
            @Override
            public boolean isCellEditable(int row, int col) {
                return false; //Only Custom Category Cells can be edited
            }
        };
        JTable summaryTable = new JTable(summaryTableEditor);
        summaryTable.setShowGrid(true);
        summaryTable.setGridColor(new Color(000));
        summaryTable.getTableHeader().setFont(new Font("Monospaced", Font.PLAIN, 13));
        summaryTable.setRowHeight(20);
        JScrollPane scrollPaneSummary = new JScrollPane(summaryTable);
        scrollPaneSummary.setPreferredSize(new Dimension(250, 243));
        gbc2.gridy = 3;
        addInitialCategoryNamesToTable(); //adds default values to the table
        borrowLendPanel.add(scrollPaneSummary, gbc2); //Add Summary @ row 3
    }

    //EFFECTS: Adds the default values to the summaryTable.
    //MODIFIES: this
    public void addInitialCategoryNamesToTable() {
        List<String> defaultCat =
                new ArrayList<>(Arrays.asList("Groceries", "Dorm", "School", "Restaurant", "Entertainment", "Random"));
        for (int i = 0; i < defaultCat.size(); i++) {
            summaryTableEditor.addRow(new Object[] {defaultCat.get(i), "$" + 0.0, 0.0 + "%"});
        }
        summaryTableEditor.addRow(new Object[] {"Custom 1", "$" + 0.0, 0.0 + "%"});
        summaryTableEditor.addRow(new Object[] {"Custom 2", "$" + 0.0, 0.0 + "%"});
        summaryTableEditor.addRow(new Object[] {"Custom 3", "$" + 0.0, 0.0 + "%"});
        summaryTableEditor.addRow(new Object[] {"", "", ""});
        summaryTableEditor.addRow(new Object[] {"Total", "$" + 0.0, 0.0 + "%"});
    }

    //EFFECTS: Creates a ModelList to be passed as a parameter for the Borrow/Lend JList
    //MODIFIES: this
    public DefaultListModel<BorrowLend> createListModel() {
        DefaultListModel<BorrowLend> listModel = new DefaultListModel<>();
        BorrowLend b1 = new BorrowLend("Jack", 10, "hi", 10, 17, true);
        BorrowLend b2 = new BorrowLend("John", 12, "hi", 10, 17, false);
        BorrowLend b3 = new BorrowLend("Mom", 20, "hi", 10, 17, false);
        expenseList.addBorrowLend(b1);
        expenseList.addBorrowLend(b2);
        expenseList.addBorrowLend(b3);

        for (BorrowLend borrowLend : expenseList.getBorrowLendList()) {
            listModel.addElement(borrowLend);
        }

        return listModel;
    }

    //EFFECTS: Adds expenses to the ExpenseList for *testing*
    //MODIFIES: this
    public void addExpenses() {
        Expense e1 = new Expense("school", 50, "hi", 10, 12);
        Expense e2 = new Expense("school", 50, "hi", 10, 13);
        Expense e3 = new Expense("school", 50, "hi", 10, 14);
        Expense e4 = new Expense("school", 50, "hi", 10, 15);
        expenseList.addExpense(e1);
        expenseList.addExpense(e2);
        expenseList.addExpense(e3);
        expenseList.addExpense(e4);
    }

    //EFFECTS: Performs an action everytime an action is performed
    //MODIFIES: this
    @Override
    public void actionPerformed(ActionEvent action) {
        //BL = 6 attributes, Expense = 5.
        if (action.getActionCommand().equals("TextFieldEnter")) {
            actionManagerTable.actionManagerRunner();
        }
        if (action.getActionCommand().equals("ResetButton")) {
            actionManagerTable.resetPanel();
        }
        if (action.getActionCommand().equals("SaveButton")) {
            saveExpenseList();
        }
        if (action.getActionCommand().equals("LoadButton")) {
            loadExpenseList();
        }
    }

    //Effects: saves ExpenseList to file
    private void saveExpenseList() {
        try {
            jsonWriter.open();
            jsonWriter.write(expenseList);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1); //change this, Add JLabel to display if it was added successfully or not
        }
    }

    //Modifies: this
    //Effects: loads ExpenseList from file, prints error message if file doesn't exist.
    private void loadExpenseList() {
        if (loadCount == 0) {
            try {
                expenseList = jsonReader.read(); //loads list breaks ExpenseTableActionManager's reference
                actionManagerTable.setExpenseList(expenseList); //updates the reference
                for (Expense e : expenseList.getExpenseList()) {
                    actionManagerTable.addExpenseRestore(e.getCategory(), e.getPrice(), e.getDescription(),
                            e.getMonth(), e.getDay());
                }
            } catch (IOException e) {
                //create a JLabel to display some text
            }
            actionManagerTable.updateSummaryTable();
        }
        loadCount++;
    }

    //EFFECTS: runs the application
    public static void main(String[] args) {
        new PersonalExpenseAppGUI();
    }

}
