package ui;

import model.*;

import java.util.Scanner;

// Represents the PersonalExpenseApp that has an Accessor Expense and ExpenseList as attributes.
// Allows the user to interact with the console to add Expenses and view summaries.
public class PersonalExpenseApp {
    private ExpenseList expenseList; //List that stores Expenses
    private final Scanner input;
    private Expense accessDefaultValues; //Expense that is used to access the getters and setters of the Expense Class

    /*
     * REQUIRES:
     * MODIFIES:
     * EFFECTS: Initializes all the attributes and calls runPersonalExpenseApp(), which runs the App
     */
    public PersonalExpenseApp() {
        expenseList = new ExpenseList();
        input = new Scanner(System.in);
        accessDefaultValues = new Expense("Random", 0,"Accessor",1,1);
        runPersonalExpenseApp();
    }

    /*
     * REQUIRES:
     * MODIFIES:
     * EFFECTS: Displays the WelcomeScreen/ Tutorial, then continue to run the App until the user inputs exit (0).
     */
    public void runPersonalExpenseApp() {
        int answer = welcome();

        while (answer != 0) {
            answer = menu();
        }

        System.out.print("\nThanks for using Personal Expense Manager\nSee you soon!");
    }

    /*
     * REQUIRES:
     * MODIFIES:
     * EFFECTS: Gives a brief overview of the App, then prompts the user to create their first Expense and adds it
     * to the expenseList. Returns 0 if the user input exit(0) so that the App can close. Otherwise, returns 1.
     */
    public int welcome() {
        System.out.println("Welcome to your Personal Expense Manager\n");
        String question = "Add 1st Expense (1), exit (0): ";
        System.out.print("What would you like to do?\n" + question);
        int choice = inputChecker(1, question);
        if (choice == 1) {
            System.out.println("\nGreat!\nExpenses have 5 attributes: Category, Price, Description, Month, and Day.");
            System.out.println("We will provide you with 6 default categories and you can choose up to 3 custom ones.");
            System.out.println("The current default categories are:");
            for (String s : accessDefaultValues.getDefaultCategories()) {
                if (!s.equals("Random")) {
                    System.out.print(s + ", ");
                } else {
                    System.out.println(s + ".\n");
                }
            }
            System.out.print("Log your first Expense!");
            helpAddExpense();
            return 1;
        }
        return 0;
    }

    /*
     * REQUIRES:
     * MODIFIES:
     * EFFECTS: Displays the Main Menu with all the actions that you can do with the App. Takes the user's input and
     * calls the specific method that represents that input. Returns 0 if the user input exit(0) so that the App can
     * close. Otherwise, returns 1.
     */
    public int menu() {
        System.out.println("Menu:");
        System.out.println("(1) Add new Expense.");
        System.out.println("(2) View monthly spending by category (in percent).");
        System.out.println("(3) Compare monthly or biweekly spending periods (3 total).");
        System.out.println("(0) Exit.");
        String question = "Choose an option: ";
        System.out.print(question);
        int choice = inputChecker(3, question);
        if (choice != 0) {
            if (choice == 1) {
                helpAddExpense();
            } else if (choice == 2) {
                helpViewCategorySpendingPercentage();
            } else if (choice == 3) {
                helpCompareMonthlyOrBiweeklyExpenses();
            }
            return 1;
        }
        return 0;
    }

    /*
     * REQUIRES:
     * MODIFIES: this
     * EFFECTS: Displays the AddExpense method, where the user is prompted for all the attributes of an expense. An
     * Expense Object is then created with those attributes and added to the expenseList.
     */
    public void helpAddExpense() {
        String category;
        double price;
        String description;
        int month;
        int day;
        input.nextLine(); //these lines are used to clear any leftover input
        System.out.println("\nSet Expense Attributes:");
        System.out.print("Category: ");
        category = input.nextLine();
        System.out.print("Price: ");
        price = priceInputChecker();
        input.nextLine();
        System.out.print("Description: ");
        description = input.nextLine();
        System.out.print("Month: ");
        month = dayMonthAddHelper(true);
        System.out.print("Day: ");
        input.nextLine();
        day  = dayMonthAddHelper(false);
        expenseList.addExpense(new Expense(category, price, description, month, day));
        System.out.println("Expense added successfully\n");
    }

    /*
     * REQUIRES:
     * MODIFIES:
     * EFFECTS: Displays the ViewCategorySpendingPercentage method, where the user is prompted for a month and a list
     * of the percentages of each category in respect to the Monthly Total Expenses are printed . If a category says 0%
     * then no Expense was had that category during that month.
     */
    public void helpViewCategorySpendingPercentage() {
        int month;
        System.out.print("\nWhich month do you want to view?: ");
        month = dayMonthAddHelper(true);
        double[] categoryPercentage = expenseList.getCategoryPercentagesPerMonth(month);
        int currentDefaultLastIndex = expenseList.getExpenseList().get(0).getDefaultCategories().size() - 1;
        int counter = 0;
        for (String s : expenseList.getExpenseList().get(0).getDefaultCategories()) {
            if (counter != currentDefaultLastIndex) {
                System.out.print(s + " | ");
            } else {
                System.out.println(s);
            }
            counter++;
        }
        for (int i = 0; i < categoryPercentage.length; i++) {
            if (i != categoryPercentage.length - 1) {
                System.out.print(categoryPercentage[i] + "% | ");
            } else {
                System.out.println(categoryPercentage[i] + "%\n");
            }
        }
    }

    /*
     * REQUIRES:
     * MODIFIES:
     * EFFECTS: Displays the helpCompareMonthlyOrBiweeklyExpenses method, where the user is prompted for a month and a
     * boolean that represents Monthly or Biweekly comparison. A List of total Expenses of terms
     * [target, target+1, target+2] are printed.
     */
    public void helpCompareMonthlyOrBiweeklyExpenses() {
        int month;
        boolean isMonthly;
        System.out.println("\nCompare 3 periods after the target month.");
        System.out.print("Which month do you want to start with?: ");
        month = dayMonthAddHelper(true);
        input.nextLine();
        String question = "Monthly (T), or Biweekly (F)?: ";
        System.out.print("Do you want to compare " + question);
        isMonthly = trueFalseHelper(question);
        String monthOrBiweekly = (isMonthly) ? "Month " : "Week ";
        double[] threeTermExpenses = expenseList.getMonthlyOrBiweeklyTotal(month, isMonthly);
        System.out.println("\nLast 3 " + monthOrBiweekly.toLowerCase() + "\bs total spending:");
        for (int i = 0; i < threeTermExpenses.length; i++) {
            if (i != threeTermExpenses.length - 1) {
                System.out.print(monthOrBiweekly + (i + 1) + ": " + threeTermExpenses[i] + " | ");
            } else {
                System.out.println(monthOrBiweekly + (i + 1) + ": " + threeTermExpenses[i] + "\n");
            }
        }
    }

    /*
     * REQUIRES:
     * MODIFIES:
     * EFFECTS: Helper method to make sure that the user inputs "T" or "F" for true and false respectively
     */
    public boolean trueFalseHelper(String question) {
        boolean correctFormat = false;
        boolean firstLoop = true;
        boolean trueOrFalse = false;
        String answer = "";
        while (!correctFormat) {
            if (!firstLoop) {
                System.out.print(question);
            }
            answer = input.nextLine();
            if (!answer.equalsIgnoreCase("t") & !answer.equalsIgnoreCase("f")) {
                System.out.println("Invalid input");
                firstLoop = false;
                continue;
            }
            trueOrFalse = (answer.equalsIgnoreCase("t")) ? true : false;
            correctFormat = true;
        }
        return trueOrFalse;
    }

    /*
     * REQUIRES:
     * MODIFIES:
     * EFFECTS: Helper method to make sure that the user inputs a valid value for Month and Day
     */
    public int dayMonthAddHelper(boolean isMonth) {
        int dayOrMonth = 0;
        if (isMonth) {
            while (dayOrMonth == 0) {
                dayOrMonth = inputChecker(12, "Month: ");
                if (dayOrMonth == 0) {
                    System.out.print("Invalid input\nMonth: ");
                }
            }
        } else {
            while (dayOrMonth == 0) {
                dayOrMonth = inputChecker(31, "Day: ");
                if (dayOrMonth == 0) {
                    System.out.print("Invalid input\nDay: ");
                }
            }
        }
        return dayOrMonth;
    }

    /*
     * REQUIRES:
     * MODIFIES:
     * EFFECTS: Helper method to make sure that the user does not input a negative price
     */
    public double priceInputChecker() {
        boolean correctFormat = false;
        boolean firstLoop = true;
        double price = 0;
        while (!correctFormat) {
            try {
                if (!firstLoop) {
                    System.out.print("Price: ");
                }
                price = input.nextDouble();
                if (price < 0) {
                    System.out.println("Invalid input");
                    firstLoop = false;
                    continue;
                }
                correctFormat = true;
            } catch (Exception e) {
                System.out.println("Invalid input");
                input.nextLine(); //clears line that user inputted
                firstLoop =  false;
            }
        }
        return price;
    }

    /*
     * REQUIRES:
     * MODIFIES:
     * EFFECTS: Helper method to ensure that user inputs a valid value for the App Menu options.
     */
    public int inputChecker(int maxRange, String question) {
        boolean correctInput = false;
        boolean firstLoop = true;
        int inputNum = 0;
        while (!correctInput) {
            try {
                if (!firstLoop) {
                    System.out.print(question);
                }
                inputNum = input.nextInt();
                if (inputNum > maxRange | inputNum < 0) {
                    System.out.println("Invalid input");
                    firstLoop = false;
                    continue;
                }
                correctInput = true;
            } catch (Exception e) {
                System.out.println("Invalid input");
                input.nextLine(); //clears line that user inputted
                firstLoop =  false;
            }
        }
        return inputNum;
    }
}
