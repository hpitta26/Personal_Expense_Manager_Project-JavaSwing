package ui.gui;

import model.Expense;
import model.ExpenseList;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CategoryPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.chart.title.LegendTitle;
import org.jfree.chart.title.TextTitle;
import org.jfree.chart.ui.HorizontalAlignment;
import org.jfree.chart.ui.RectangleEdge;
import org.jfree.chart.ui.RectangleInsets;
import org.jfree.chart.ui.VerticalAlignment;
import org.jfree.data.category.DefaultCategoryDataset;
import ui.exceptions.InvalidInputException;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.text.NumberFormat;

//Class used to manage the GUI's Graphs, Expense Table, and Summary Table
public class ExpenseTableActionManager {

    //How many times the User has clicked the Button
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

    private JPanel summaryGraphPanel;
    private DefaultCategoryDataset monthDataForGraphs; //data used for Graphs
    private DefaultCategoryDataset monthTwoDataForGraphs; //data used for Graphs
    private DefaultCategoryDataset monthThreeDataForGraphs; //data used for Graphs

    private Container screen;

    JFreeChart monthOneGraph;
    JFreeChart monthTwoGraph;
    JFreeChart monthThreeGraph;

    //EFFECTS: initializes helper class with fields
    //MODIFIES: this
    public ExpenseTableActionManager(JLabel label, JLabel[] testLabels, JTextField textField, ExpenseList expenseList,
                                     DefaultTableModel tableEditor, DefaultTableModel summaryEditor, Container screen) {
        enterCount = 1;
        this.textField = textField;
        this.questionLabel = label;
        this.outputTestLabels = testLabels;
        this.expenseList = expenseList;
        this.expenseTableEditor = tableEditor;
        this.summaryTableEditor =  summaryEditor;
        inputChecker = new InputChecker(textField, questionLabel);

        this.screen = screen;
        //currentExpense = new Expense("School", 0, "",1, 1);
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
                }
                resetPanel();
            } catch (InvalidInputException e) {
                //nothing
            }
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
        if (enterCount == 6) {
            updateApplication();
        }

        questionLabel.setText("Current Input: Day");
        enterCount = 1;
        outputTestLabels[0].setText("Day: ");
        outputTestLabels[1].setText("Description: ");
        outputTestLabels[2].setText("Price: ");
        outputTestLabels[3].setText("Category: ");
        outputTestLabels[4].setText("Month: Only 11");
    }

    //EFFECTS: Used to insert Expenses at the correct position, during runtime
    //MODIFIES:
    public void addExpenseToTable() {
        //used to add expenses to the table, can be used after loading the app to restore GUI state
        expenseList.addExpense(new Expense(category, price, description, month, day));
        //Object[] attributes = new Object[] {month + "/" + day, description, price, category};
        Object[] attributes = new Object[] {day, description,"$" + price, category};
        insertCustomRowHelper(attributes, month);
        //expenseTableEditor.addRow(attributes); //change later to insert below the day in a month.
    }

    //EFFECTS: Restores the state of the App when loaded from file, inserts expenses in the correct position
    //MODIFIES:
    public void addExpenseRestore(String cat, double p, String des, int mon, int d) {
        //Object[] attributes = new Object[] {mon + "/" + d, des, p, cat};
        Object[] attributes = new Object[] {d, des,"$" + p, cat};
        //expenseTableEditor.addRow(attributes);
        insertCustomRowHelper(attributes, mon);
    }

    //EFFECTS: Inserts Expense at the correct row, if not in November inserts at the bottom
    //MODIFIES: this
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

    //EFFECTS: updates the Summary Table and also the Graphs
    //MODIFIES: this
    public void updateApplication() {
        double[] percentages = expenseList.getCategoryPercentagesPerMonth(11);
        double[] totals = expenseList.getCategoryTotalPerMonth(11);
        double total = 0;

        //Summary Table = 11 rows, 3 columns
        for (int i = 0; i < percentages.length; i++) {
            total = total + totals[i];
            summaryTableEditor.setValueAt("$" + totals[i], i, 1);
            summaryTableEditor.setValueAt(percentages[i] + "%", i, 2);
        }

        summaryTableEditor.setValueAt("$" + total, 10, 1);
        summaryTableEditor.setValueAt("100%", 10, 2); //change this to actual percentage

        updateDataSet(new int[] {11, 10, 9}); //updates the graphs

        //change this method to only change the target Category (not all the rows)
    }

    //EFFECTS: updates the expenseList after load is called
    //MODIFIES: this
    public void setExpenseList(ExpenseList newExpenseList) {
        this.expenseList = newExpenseList;
    }

    //EFFECTS: creates the summary graphs and adds it to screen.WEST
    //MODIFIES: this
    public void createSummaryGraphsPanel(int[] months) {
        summaryGraphPanel = new JPanel(new FlowLayout());
        summaryGraphPanel.setPreferredSize(new Dimension(250, 750));

        generateGraph("November", true, 1, 260, 0); //height 260
        generateGraph("October", false, 0, 200, 1); //height 200
        generateGraph("September", false, 0, 200, 2); //height 200

        screen.add(summaryGraphPanel, BorderLayout.WEST);
    }

    //EFFECTS: configures the graphs to the correct layout and adds then to the summaryChartPanel
    //MODIFIES: this
    public void generateGraph(String titleMonth, boolean isFirstGraph, int titleIndex, int height, int graphNum) {
        //Makes sure that the correct graph and DefaultCategoryDataset are being used
        JFreeChart monthGraph;

        if (graphNum == 0) {
            monthDataForGraphs = new DefaultCategoryDataset();
            generateDefaultGraphValues(monthDataForGraphs);
            monthGraph = ChartFactory.createBarChart("", "", "",
                    monthDataForGraphs, PlotOrientation.VERTICAL, isFirstGraph, true, false);
        } else if (graphNum == 1) {
            monthTwoDataForGraphs = new DefaultCategoryDataset();
            generateDefaultGraphValues(monthTwoDataForGraphs);
            monthGraph = ChartFactory.createBarChart("", "", "",
                    monthTwoDataForGraphs, PlotOrientation.VERTICAL, isFirstGraph, true, false);
        } else {
            monthThreeDataForGraphs = new DefaultCategoryDataset();
            generateDefaultGraphValues(monthThreeDataForGraphs);
            monthGraph = ChartFactory.createBarChart("", "", "",
                    monthThreeDataForGraphs, PlotOrientation.VERTICAL, isFirstGraph, true, false);
        }

        configureChartLayout(monthGraph, isFirstGraph, titleIndex, titleMonth); //configure the layout

        ChartPanel chartPanel = new ChartPanel(monthGraph);
        chartPanel.setPreferredSize(new Dimension(250, height)); //prev width 250
        //chartPanel.setBorder(new EtchedBorder());

        summaryGraphPanel.add(chartPanel); //pass in the Summary Graph panel will be instantiated with a new Panel and
        // put on screen
    }

    //EFFECTS: configures the graph layouts
    //MODIFIES: this
    public void configureChartLayout(JFreeChart monthGraph, boolean isFirstGraph, int titleIndex, String titleMonth) {
        Color trans = new Color(0xFF, 0xFF, 0xFF, 0);
        monthGraph.setBackgroundPaint(trans);
        monthGraph.getPlot().setBackgroundPaint(trans);
        monthGraph.addSubtitle(titleIndex,new TextTitle(titleMonth,
                new Font("Monospaced", Font.PLAIN, 14), new Color(0,0,0),
                RectangleEdge.TOP, HorizontalAlignment.CENTER,
                VerticalAlignment.TOP, RectangleInsets.ZERO_INSETS));

        if (isFirstGraph) {
            //Configure the Legend
            LegendTitle legend = monthGraph.getLegend();
            legend.setPosition(RectangleEdge.TOP);
            legend.setBackgroundPaint(trans);
            legend.setMargin(0, 30, 20, 20);
        }

        //Format y-axis numbers (no decimals and currency value)
        CategoryPlot chartPlot = monthGraph.getCategoryPlot();
        NumberFormat currency = NumberFormat.getCurrencyInstance();
        currency.setMaximumFractionDigits(0);
        currency.setMinimumFractionDigits(0);
        NumberAxis rangeAxis = (NumberAxis) chartPlot.getRangeAxis();
        rangeAxis.setNumberFormatOverride(currency);

        //Sets max width for bars
        BarRenderer br = (BarRenderer) chartPlot.getRenderer();
        br.setMaximumBarWidth(0.07);
    }

    //EFFECTS: sets the default graph values, when App is initially ran
    //MODIFIES: this
    public void generateDefaultGraphValues(DefaultCategoryDataset data) {
        data.setValue(0, "Groceries", "       ");
        data.setValue(0, "Dorm", "       ");
        data.setValue(0, "School", "       ");
        data.setValue(0, "Restaurant", "       ");
        data.setValue(0, "Entertainment", "       ");
        data.setValue(0, "Random", "       ");
    }

    //EFFECTS: updates all the data inside the graphs
    //MODIFIES: this
    public void updateDataSet(int[] months) {
        double[] monthOneTotals = checkIfListIsNull(months[0]);
        double[] monthTwoTotals = checkIfListIsNull(months[1]);
        double[] monthThreeTotals = checkIfListIsNull(months[2]);
        monthDataForGraphs.clear();
        for (int i = 0; i < monthOneTotals.length; i++) {
            monthDataForGraphs.setValue(monthOneTotals[i],
                    expenseList.getExpenseList().get(0).getDefaultCategories().get(i), "       ");
        }
        monthTwoDataForGraphs.clear();
        for (int i = 0; i < monthTwoTotals.length; i++) {
            monthTwoDataForGraphs.setValue(monthTwoTotals[i],
                    expenseList.getExpenseList().get(0).getDefaultCategories().get(i), "       ");
        }
        monthThreeDataForGraphs.clear();
        for (int i = 0; i < monthThreeTotals.length; i++) {
            monthThreeDataForGraphs.setValue(monthThreeTotals[i],
                    expenseList.getExpenseList().get(0).getDefaultCategories().get(i), "       ");
        }
    }

    //EFFECTS: checks whether the list of that month is null or not
    public double[] checkIfListIsNull(int monthCheck) {
        double[] totals;
        try {
            totals = expenseList.getCategoryTotalPerMonth(monthCheck);
        } catch (Exception e) {
            totals = new double[] {0,0,0,0,0,0};
        }
        return totals;
    }
}
