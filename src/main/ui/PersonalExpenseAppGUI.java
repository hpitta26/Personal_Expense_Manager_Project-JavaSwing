package ui;

import model.BorrowLend;
import model.Expense;
import model.ExpenseList;
import ui.exceptions.InvalidInputException;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

// Represents the PersonalExpenseApp
// Allows the user to interact with the GUI to add Expenses and view summaries.
public class PersonalExpenseAppGUI extends JFrame implements ActionListener {
    private JList list1;
    private JList list2;
    private ExpenseList expenseList = new ExpenseList();
    private String[] testList;
    private int enterCount = 1;

    Container screen;
    JPanel monthPanel;
    JPanel expenseListPanel;
    JPanel borrowLendPanel;
    JPanel textFieldAndButton;

    DefaultTableModel tableEditor;
    JTable expenseTable;
    JTextField textField;
    JComboBox<String> comboBox;
    JLabel questionLabel; //replace this with HintText
    InputChecker inputChecker;

    JLabel dayOutputTest;
    JLabel descriptionOutputTest;
    JLabel priceOutputTest;
    JLabel categoryOutputTest;
    JLabel monthOutputTest;

    private String category;
    private double price;
    private String description;
    private int month;
    private int day;


    //EFFECTS: initializes the GUI
    public PersonalExpenseAppGUI() {
        super("Personal Expense Manager");
        setSize(1000,800);
        //setLocation(1, 1);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        screen = this.getContentPane();
        screen.setLayout(new BorderLayout(0, 0));

        createMonthPanel();

        createExpenseListPanel();

        createBorrowLendPanel();
        inputChecker = new InputChecker(textField, questionLabel);

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
        addJTable(); //Adds ExpenseListTable
        helpCreateTextFieldAndComboBox(); //Adds ComboBox and TextField

        screen.add(expenseListPanel, BorderLayout.CENTER); //Add expenseListPanel to screen.CENTER
    }

    //EFFECTS: Creates the JTable that will display the Expenses to the User
    //MODIFIES: this
    public void addJTable() {
        //create and customize JTable here
        GridBagConstraints gbc2 = new GridBagConstraints();
        String[] colNames = new String[] {"Month/Day", "Description", "Price", "Category"};
        tableEditor = new DefaultTableModel(colNames, 0);
        expenseTable = new JTable(tableEditor);
        expenseTable.setShowGrid(true);
        expenseTable.setGridColor(new Color(000));
        expenseTable.getTableHeader().setFont(new Font("Monospaced", Font.PLAIN, 13));
        expenseTable.setRowHeight(20);
        JScrollPane scrollPaneExpenses = new JScrollPane(expenseTable);
        scrollPaneExpenses.setPreferredSize(new Dimension(730, 570));
        gbc2.gridy = 0;
        expenseListPanel.add(scrollPaneExpenses, gbc2); //Add Expense List @ row 0
        //Implement table with different column widths, day = much shorter
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
        textField = new JTextField("Hello");
        textField.setBorder(new EtchedBorder());
        textField.setPreferredSize(new Dimension(730, 30));
        textField.setActionCommand("TextFieldEnter");
        textField.addActionListener(this);
        textFieldAndButton.add(textField);

//        JButton button1 = new JButton("Enter");
//        //button1.setBorder(new EtchedBorder());
//        button1.setPreferredSize(new Dimension(100,30));
//        button1.setActionCommand("EnterButton");
//        button1.addActionListener(this); //actionPerformed is called everytime the button is clicked
//        textFieldAndButton.add(button1);
    }

    public JPanel helpAddTestLabels() {
        JPanel testLabels = new JPanel();
        testLabels.setLayout(new FlowLayout());
        dayOutputTest = new JLabel("Day: "); //Change this to a slider for each month
        dayOutputTest.setPreferredSize(new Dimension(130, 20));

        descriptionOutputTest = new JLabel("Description: "); //Change this to a slider for each month
        descriptionOutputTest.setPreferredSize(new Dimension(130, 20));

        priceOutputTest = new JLabel("Price: "); //Change this to a slider for each month
        priceOutputTest.setPreferredSize(new Dimension(130, 20));

        categoryOutputTest = new JLabel("Category: "); //Change this to a slider for each month
        categoryOutputTest.setPreferredSize(new Dimension(130, 20));

        monthOutputTest = new JLabel("Month: "); //Change this to a slider for each month
        monthOutputTest.setPreferredSize(new Dimension(130, 20));
        testLabels.add(dayOutputTest);
        testLabels.add(descriptionOutputTest);
        testLabels.add(priceOutputTest);
        testLabels.add(categoryOutputTest);
        testLabels.add(monthOutputTest);
        return testLabels;
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
        gbc.gridy = 0;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        blTitle.setBorder(new EtchedBorder());
        borrowLendPanel.add(blTitle, gbc);

        DefaultListModel<BorrowLend> listModel = createListModel();
        list2 = new JList(listModel);
        JScrollPane scrollPaneBL = new JScrollPane(list2);
        scrollPaneBL.setPreferredSize(new Dimension(250, 400));
        gbc.gridy = 1;
        borrowLendPanel.add(scrollPaneBL, gbc);
        screen.add(borrowLendPanel, BorderLayout.EAST); //Add EAST
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

    //EFFECTS: Adds expenses to the ExpenseList for testing
    //MODIFIES: this
    public void addExpenses() {
        //expenseList = new ExpenseList();
        Expense e1 = new Expense("school", 50, "hi", 10, 12);
        Expense e2 = new Expense("school", 50, "hi", 10, 13);
        Expense e3 = new Expense("school", 50, "hi", 10, 14);
        Expense e4 = new Expense("school", 50, "hi", 10, 15);
        expenseList.addExpense(e1);
        expenseList.addExpense(e2);
        expenseList.addExpense(e3);
        expenseList.addExpense(e4);
    }

    //EFFECTS: Performs an action everytime the button is clicked
    //MODIFIES: this
    @Override
    public void actionPerformed(ActionEvent action) {
        //BL = 6 attributes, Expense = 5.
        String defaultString = "Current Input: ";
        if (action.getActionCommand().equals("TextFieldEnter")) {
            textFieldActionHelper();
        }

        if (enterCount == 6) {
            //call remove expense before adding
            try {
                boolean answer = helpRemoveObject(true);
                if (answer) {
                    expenseList.addExpense(new Expense(category, price, description, month, day));
                    Object[] attributes = new Object[] {month + "/" + day, description, price, category};
                    tableEditor.addRow(attributes);
                }
                resetTestLabels();
                enterCount = 1;
            } catch (InvalidInputException e) {
                //nothing
            }
        }

    }

    public void textFieldActionHelper() {

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

    public void dayHelper() {
        try {
            day = inputChecker.dayInputChecker();
            questionLabel.setText("Current Input: Description");
            dayOutputTest.setText(Integer.toString(day));
            enterCount++;
        } catch (InvalidInputException e) {
            questionLabel.setText("Current Input: Day (Previous Input was Invalid)");
        }
        textField.setText("");
    }

    public void descriptionHelper() {
        description = textField.getText();
        questionLabel.setText("Current Input: Price");
        descriptionOutputTest.setText(description);
        enterCount++;
        textField.setText("");
    }

    public void priceHelper() {
        try {
            price = inputChecker.priceInputChecker();
            questionLabel.setText("Current Input: Category");
            priceOutputTest.setText(Double.toString(price));
            enterCount++;
        } catch (InvalidInputException e) {
            questionLabel.setText("Current Input: Price (Previous Input was Invalid)");
        }
        textField.setText("");
    }

    public void categoryHelper() {
        category = textField.getText();
        questionLabel.setText("Current Input: Month");
        categoryOutputTest.setText(category);
        enterCount++;
        textField.setText("");
    }

    public void monthHelper() {
        try {
            month = inputChecker.monthInputChecker();
            //questionLabel.setText("Current Input: Day");
            questionLabel.setText("Are you sure you want to add this Expense (T)/(F)?");
            monthOutputTest.setText(Integer.toString(month));
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
            questionLabel.setText("Current Input: Day");
        } catch (InvalidInputException e) {
            questionLabel.setText("Are you sure you want to add this Expense (T)/(F)?");
            textField.setText("");
            throw new InvalidInputException("Invalid TrueOrFalse");
        }
        textField.setText("");
        return answer;
    }

    public void resetTestLabels() {
        dayOutputTest.setText("Day: ");
        descriptionOutputTest.setText("Description: ");
        priceOutputTest.setText("Price: ");
        monthOutputTest.setText("Month: ");
        categoryOutputTest.setText("Category: ");
    }


    //EFFECTS: runs the application
    public static void main(String[] args) {
        new PersonalExpenseAppGUI();
    }

}
