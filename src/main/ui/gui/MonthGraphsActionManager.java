package ui.gui;

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

import javax.swing.*;
import java.awt.*;
import java.text.NumberFormat;

//Class used by ExpenseListPanelActionManager to manage the GUI's monthGraphs
public class MonthGraphsActionManager {


    // ------------------------------------- INSTANCE VARIABLES --------------------------------------------


    private Container screen;

    private JPanel summaryGraphPanel;
    private DefaultCategoryDataset monthDataForGraphs; //data used for Graphs
    private DefaultCategoryDataset monthTwoDataForGraphs; //data used for Graphs
    private DefaultCategoryDataset monthThreeDataForGraphs; //data used for Graphs

    //private Container screen;
    private ExpenseList expenseList;

    private String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August",
            "September", "October", "November", "December"};

    private JFreeChart monthOneGraph;
    private JFreeChart monthTwoGraph;
    private JFreeChart monthThreeGraph;


    // ------------------------------------- CONSTRUCTOR --------------------------------------------


    public MonthGraphsActionManager(Container screen, ExpenseList expenseList, int[] months) {
        //this.screen = screen;
        this.expenseList = expenseList;
        createSummaryGraphsPanel(months);

        this.screen = screen;

        screen.add(summaryGraphPanel, BorderLayout.WEST);
    }


    // ------------------------------------- CREATE GRAPHS --------------------------------------------


    //EFFECTS: creates the summary graphs and adds it to screen.WEST
    //MODIFIES: this
    public void createSummaryGraphsPanel(int[] months) {
        summaryGraphPanel = new JPanel(new FlowLayout());
        summaryGraphPanel.setPreferredSize(new Dimension(250, 750));

        generateGraph(monthOneGraph, "November", true, 1, 260,
                0); //height 260
        generateGraph(monthTwoGraph, "October", false, 0, 200,
                1); //height 200
        generateGraph(monthThreeGraph, "September", false, 0, 200,
                2); //height 200

//        screen.add(summaryGraphPanel, BorderLayout.WEST);
    }

    //EFFECTS: configures the graphs to the correct layout and adds then to the summaryChartPanel
    //MODIFIES: this
    public void generateGraph(JFreeChart monthGraph, String titleMonth, boolean isFirstGraph, int titleIndex,
                              int height, int graphNum) {
        //Makes sure that the correct graph and DefaultCategoryDataset are being used
        //JFreeChart monthGraph;

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


    // ------------------------------------- UPDATE GRAPHS --------------------------------------------


    //EFFECTS: updates all the data inside the graphs
    //MODIFIES: this
    public void updateDataSet(int[] months) {
        double[] monthOneTotals = checkIfListIsNull(months[0]);
        double[] monthTwoTotals = checkIfListIsNull(months[1]);
        double[] monthThreeTotals = checkIfListIsNull(months[2]);

        if (expenseList.getExpenseList().size() == 0) {
            return;
        }

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

    //EFFECTS: Changes the month graphs that are displayed when a MonthButton is clicked
    //MODIFIES: this
    public void changeDisplayedMonth(int[] newMonths) {
        summaryGraphPanel.removeAll();
        generateGraph(monthOneGraph, monthNames[newMonths[0] - 1], true, 1, 260,
                0); //height 260
        generateGraph(monthTwoGraph, monthNames[newMonths[1] - 1], false, 0, 200,
                1); //height 200
        generateGraph(monthThreeGraph, monthNames[newMonths[2] - 1], false, 0, 200,
                2); //height 200

        summaryGraphPanel.revalidate();
        summaryGraphPanel.repaint();
    }

    //EFFECTS: checks whether the list of that month is null or not
    private double[] checkIfListIsNull(int monthCheck) {
        double[] totals;
        try {
            totals = expenseList.getCategoryTotalPerMonth(monthCheck);
        } catch (Exception e) {
            totals = new double[] {0,0,0,0,0,0};
        }
        return totals;
    }


    // ------------------------------------- LOAD GRAPHS --------------------------------------------


    //EFFECTS: updates the monthGraphs when the user loads an expenseList from file
    //MODIFIES: this
    public void loadMonthGraphs(ExpenseList newExpenseList, int[] newMonths) {

        this.expenseList = newExpenseList; //updates the expenseList reference
        updateDataSet(newMonths);

    }
}
