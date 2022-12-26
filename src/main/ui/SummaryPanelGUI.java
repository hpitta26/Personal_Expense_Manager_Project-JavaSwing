package ui;

import model.BorrowLend;
import model.Expense;
import model.ExpenseList;
import ui.gui.ExpenseListPanelActionManager;
import ui.gui.SummaryPanelActionManager;

import javax.swing.*;
import javax.swing.border.EtchedBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


//Class that is used to Manage the JPanel with the summaryTable and borrowLendList
public class SummaryPanelGUI implements ActionListener {


    // ---------------------------------- INSTANCE VARIABLES --------------------------------------------

    private ExpenseList expenseList;

    private JPanel summaryAndBorrowLendPanel;

    private JList borrowLendScrollPaneList;
    private DefaultTableModel summaryTableEditor; //used to edit the Summary Table

    private JButton saveButton;
    private JButton loadButton;

    //Helper classes
    private SummaryPanelActionManager summaryTableActionManager;

    //Main JFrame reference
    private MainJFrameGUI mainJFrame; //called by save and load buttons


    // ------------------------------------- CONSTRUCTOR --------------------------------------------


    public SummaryPanelGUI(Container screen, ExpenseList expenseList, MainJFrameGUI parent) {
        //pass in the Load and Save buttons later
        this.expenseList = expenseList;
        this.mainJFrame = parent;



        createNewBorrowLendPanel();

        //initialize Action Manager
        summaryTableActionManager = new SummaryPanelActionManager(expenseList, summaryTableEditor);

        screen.add(summaryAndBorrowLendPanel, BorderLayout.EAST); //Add "this" to screen.CENTER
    }


    // ------------------------------------ BORROW LEND PANEL --------------------------------------------


    //EFFECTS: Creates the borrowLendPanel and places it at screen.EAST
    //MODIFIES: this
    public void createNewBorrowLendPanel() {
        summaryAndBorrowLendPanel = new JPanel();
        summaryAndBorrowLendPanel.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        JLabel blTitle = new JLabel("Borrow/ Lend");
        blTitle.setFont(new Font("Monospaced", Font.PLAIN, 13));
        blTitle.setSize(250, 50);
        gbc.gridy = 0; //Adds BorrowLend Title @ row 0
        gbc.fill = GridBagConstraints.HORIZONTAL;
        blTitle.setBorder(new EtchedBorder());
        summaryAndBorrowLendPanel.add(blTitle, gbc);

        DefaultListModel<BorrowLend> listModel = createListModel();
        borrowLendScrollPaneList = new JList(listModel); //Creates new list with the ListModel
        JScrollPane scrollPaneBL = new JScrollPane(borrowLendScrollPaneList);
        scrollPaneBL.setPreferredSize(new Dimension(250, 300));
        gbc.gridy = 1; //Adds BorrowLend Panel @ row 1
        summaryAndBorrowLendPanel.add(scrollPaneBL, gbc);

        addSummaryJTable(); //Adds summaryTable @ row 2

        addLoadSaveButtons(); //Adds LOAD and SAVE buttons @ row 3

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


    // ------------------------------------ SUMMARY TABLE --------------------------------------------


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
        summaryAndBorrowLendPanel.add(summaryTitle, gbc2); //Creates a gap between Panels

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
        summaryAndBorrowLendPanel.add(scrollPaneSummary, gbc2); //Add Summary @ row 3
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


    // ---------------------------------- LOAD & SAVE BUTTONS --------------------------------------------


    //EFFECTS: Creates Load and Save buttons and adds them to the BorrowLendPanel in screen.EAST
    //MODIFIES: this
    public void addLoadSaveButtons() {
        GridBagConstraints gbc = new GridBagConstraints();
        JPanel loadSave = new JPanel();
        loadSave.setPreferredSize(new Dimension(250, 50));
        loadSave.setLayout(new FlowLayout());

        JLabel summaryTitle = new JLabel(" ");
        summaryTitle.setSize(250, 50);
        gbc.gridy = 4;
        summaryAndBorrowLendPanel.add(summaryTitle, gbc); //Creates a gap between Panels

        saveButton = new JButton("Save");
        saveButton.setActionCommand("SaveButton");
        saveButton.addActionListener(this);

        loadButton = new JButton("Load");
        loadButton.setActionCommand("LoadButton");
        loadButton.addActionListener(this);

        gbc.gridy = 5;
        loadSave.add(saveButton);
        loadSave.add(loadButton);
        summaryAndBorrowLendPanel.add(loadSave, gbc);
    }

    // ---------------------------------- RUNTIME FUNCTIONS --------------------------------------------

    //EFFECTS: changes the month displayed by this panel, when a JButton is clicked
    //MODIFIES: this
    public void updateSummaryTable(int newMonth) {

        summaryTableActionManager.updateSummaryTable(newMonth);
    }

    //EFFECTS: updates the expenseList reference and updates the summaryTable based on that ExpenseList
    //MODIFIES: this
    public void loadApplication(ExpenseList newExpenseList, int currentMonth) {
        this.expenseList = newExpenseList; //updates the expenseList reference
        summaryTableActionManager.loadSummaryTable(newExpenseList, currentMonth);

    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getActionCommand().equals("SaveButton")) {
            //call MainJFrameGUI's save function
            mainJFrame.saveExpenseList();
        }
        if (e.getActionCommand().equals("LoadButton")) {
            //call MainJFrameGUI's load function
            mainJFrame.loadExpenseList();
        }
    }
}
