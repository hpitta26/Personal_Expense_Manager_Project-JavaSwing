package ui;

import model.Expense;
import model.ExpenseList;
import persistence.JsonReader;
import persistence.JsonWriter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.FileNotFoundException;
import java.io.IOException;

//Class that is used to Manage all the Panels in the GUI
public class MainJFrameGUI extends JFrame implements ActionListener {


    // ---------------------------------- INSTANCE VARIABLES --------------------------------------------


    private Container screen;
    private ExpenseList expenseList;

    private JPanel monthButtonsPanel;
    //private JPanel expenseListPanel;
    private ExpenseListPanelGUI expenseListPanel;
    private SummaryPanelGUI summaryPanel;

    private JPanel jfreeChartPanel;
    private JPanel borrowLendAndSummaryPanel;

    private String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August",
            "September", "October", "November", "December"};
    private JButton[] monthButtons;

    private JLabel testingLabel; //used for testing functionality only

    private static final String JSON_STORE = "./data/mainJFrameGUI.json";
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private int loadCount;

    private int pressedButton;


    // -------------------------------------- CONSTRUCTOR --------------------------------------------


    public MainJFrameGUI() {
        super("Personal Expense Manager");
        setSize(1250,800); //ExpenseList Panel 730 + Right Panel 250 (prev width 1000)
        //setLocation(1, 1);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        expenseList = new ExpenseList();

        screen = this.getContentPane();
        screen.setLayout(new BorderLayout(0, 0));

        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        loadCount = 0;
        pressedButton = 11;

        createMonthButtonsPanel(11);


        summaryPanel = new SummaryPanelGUI(screen, expenseList, this);

        //last Panel to be Created
        expenseListPanel = new ExpenseListPanelGUI(screen, expenseList, summaryPanel);

        setVisible(true);
    }


    // ---------------------------------- MONTH BUTTON PANEL --------------------------------------------


    public void createMonthButtonsPanel(int currentMonth) {
        monthButtonsPanel = new JPanel();
        monthButtonsPanel.setLayout(new FlowLayout());
        monthButtons = new JButton[12];

        for (int i = 0; i < monthButtons.length; i++) {
            JButton temp = new JButton(monthNames[i]);
            temp.setActionCommand(monthNames[i]);
            temp.addActionListener(this);
            if (i == currentMonth - 1) {
                temp.setFont(new Font("Monospaced", Font.BOLD, 13));
            } else {
                temp.setFont(new Font("Monospaced", Font.PLAIN, 13));
            }
            monthButtons[i] = temp;
            monthButtonsPanel.add(temp);
        }

        // for testing implementation
        // testingLabel = new JLabel(monthNames[currentMonth - 1]);
        // monthButtonsPanel.add(testingLabel);


        screen.add(monthButtonsPanel, BorderLayout.NORTH);
    }


    // ---------------------------------- RUNTIME FUNCTIONS --------------------------------------------


    @Override
    public void actionPerformed(ActionEvent e) {

        //int pressedButton = 0;
        for (int i = 0; i < monthNames.length; i++) {
            if (monthNames[i].equals(e.getActionCommand())) {
                pressedButton = i;
            } else {
                monthButtons[i].setFont(new Font("Monospaced", Font.PLAIN, 13));
            }
        }

        //testingLabel.setText(monthNames[pressedButton]); //remove (only for testing)

        //update to this month
        monthButtons[pressedButton].setFont(new Font("Monospaced", Font.BOLD, 13));

        expenseListPanel.changeDisplayedMonth(pressedButton + 1);
        summaryPanel.updateSummaryTable(pressedButton + 1);

    }

    //Effects: saves ExpenseList to file
    public void saveExpenseList() {
        try {
            jsonWriter.open();
            jsonWriter.write(expenseList);
            jsonWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            System.exit(1); //change this, Add JLabel to display if it was added successfully or not
        }
    }

    //Effects: loads ExpenseList from file and restores the App, prints error message if file doesn't exist.
    //Modifies: this
    public void loadExpenseList() {
        if (loadCount == 0) {
            try {
                expenseList = jsonReader.read(); //loads list breaks ExpenseTableActionManager's reference

                expenseListPanel.loadApplication(expenseList); //updates ExpenseTable
                summaryPanel.loadApplication(expenseList, pressedButton + 1);


            } catch (IOException e) {
                //create a JLabel to display some text
            }
        }

        loadCount++;
    }

    public static void main(String[] args) {
        new MainJFrameGUI();
    }
}
