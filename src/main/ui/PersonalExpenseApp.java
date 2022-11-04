package ui;

import model.*;
import persistence.JsonReader;
import persistence.JsonWriter;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

// Represents the PersonalExpenseApp that has an Accessor Expense and ExpenseList as attributes.
// Allows the user to interact with the console to add Expenses and view summaries.
public class PersonalExpenseApp {
    private static final String JSON_STORE = "./data/expenseList.json";
    protected ExpenseList expenseList; //List that stores Expenses
    protected final Scanner input;
    private Expense accessDefaultValues; //Expense that is used to access the getters and setters of the Expense Class
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;
    private int borrowLendTracker;
    //private BorrowLendApp borrowLendApp;

    /*
     * REQUIRES:
     * MODIFIES:
     * EFFECTS: Initializes all the attributes and calls runPersonalExpenseApp(), which runs the App
     */
    public PersonalExpenseApp() {
        expenseList = new ExpenseList();
        input = new Scanner(System.in);
        accessDefaultValues = new Expense("Random", 0,"Accessor",1,1);
        jsonWriter = new JsonWriter(JSON_STORE);
        jsonReader = new JsonReader(JSON_STORE);
        borrowLendTracker = 0;
        //borrowLendApp = new BorrowLendApp();
        runPersonalExpenseApp();
    }

    /*
     * REQUIRES:
     * MODIFIES:
     * EFFECTS: Displays the WelcomeScreen/ Tutorial, then continue to run the App until the user inputs exit (0).
     */
    public void runPersonalExpenseApp() {
        int answer = initialScreen();
        //do you want to load or not, then welcome (if they don't want to load)
        if (answer == 1) {
            loadExpenseList();
        } else if (answer == 2) {
            answer = welcome();
        }

        while (answer != 0) {
            answer = menu();
        }

        exitView();
    }

    //Effects: Asks the user if they want to load a previous Personal Expense List from file or create a new one.
    //          Returns the int associated with the option they picked.
    public int initialScreen() {
        System.out.println("Welcome to your Personal Expense Manager\n");
        System.out.println("What would you like to?:");
        System.out.println("(1) Load your Personal Expense List from file.");
        System.out.println("(2) Create a new Personal Expense List."); //should ask for new userName
        System.out.println("(0) Exit.");
        String question = "Choose an option: ";
        System.out.print(question);
        int choice = inputChecker(2, question);
        return choice;
    }

    /*
     * REQUIRES:
     * MODIFIES:
     * EFFECTS: Gives a brief overview of the App, then prompts the user to create their first Expense and adds it
     * to the expenseList. Returns 0 if the user input exit(0) so that the App can close. Otherwise, returns 1.
     */
    public int welcome() {
        System.out.println("");
        createUserName();
        String question = "Add 1st Expense (1), exit (0): ";
        System.out.print("\nNow lets add your first expense!\n" + question);
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

    //Effects: Asks the user for a userName
    public void createUserName() {
        boolean correctFormat = false;
        String userName = "";
        input.nextLine();
        while (!correctFormat) {
            System.out.print("Type your desired User Name (No Spaces): ");
            userName = input.nextLine();
            if (!userName.contains(" ")) {
                System.out.print("Are you happy with User Name \"" + userName + "\" (T)/(F)?: ");
                boolean answer = trueFalseHelper("User Name \"" + userName + "\" (T)/(F)?: ");
                if (answer) {
                    correctFormat = true;
                }
            } else {
                System.out.println("Invalid input");
            }
        }
        expenseList.setUserName(userName);
        System.out.println("User Name set to: " + expenseList.getUserName());
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
        System.out.println("(4) View current default categories & list size.");
        System.out.println("(5) Track Borrowed/Lent money.");
        System.out.println("(0) Exit.");
        String question = "Choose an option: ";
        System.out.print(question);
        int choice = inputChecker(5, question);
        return menuChoiceHelper(choice);
    }

    //Effects: calls a certain method based on the choice of the user
    private int menuChoiceHelper(int choice) {
        if (choice != 0) {
            if (choice == 1) {
                helpAddExpense();
            } else if (choice == 2) {
                helpViewCategorySpendingPercentage();
            } else if (choice == 3) {
                helpCompareMonthlyOrBiweeklyExpenses();
            } else if (choice == 4) {
                helpPrintDefaultCategories();
            } else if (choice == 5) {
                borrowLendRun();
                //borrowLendApp.run();
            }
            return 1;
        }
        return 0;
    }

    //Effects: Asks the user whether they want to save their Personal Expense List before exiting.
    public void exitView() {
        boolean answer;
        input.nextLine(); //clears any extra input
        System.out.print("\nWould you like to save your Personal Expense List before going (T)/(F)?: ");
        answer = trueFalseHelper("Save (T)/(F)?: ");
        if (answer == true) {
            saveExpenseList();
        }
        System.out.print("\nThanks for using Personal Expense Manager\nSee you soon!");
    }

    //Effects: Prints the current default categories and the number of Expenses in the list (list summary)
    public void helpPrintDefaultCategories() {
        System.out.println("\nCurrent Default Categories (9 MAX):");
        List<String> defaultCategories = accessDefaultValues.getDefaultCategories();
        int count = 0;
        for (String s : defaultCategories) {
            if (count < defaultCategories.size() - 1) {
                System.out.print(s + " | ");
            } else {
                System.out.println(s);
            }
            count++;
        }
        System.out.println("There are currently " + expenseList.getExpenseList().size() + " Expenses in your list!\n");
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
        input.nextLine(); //these lines are used to clear any leftover input
        System.out.println("\nSet Expense Attributes:");
        System.out.print("Category: ");
        category = input.nextLine();
        System.out.print("Price: ");
        price = priceInputChecker();
        input.nextLine();
        System.out.print("Description: ");
        description = input.nextLine();
        helpAddExpense2(category, price, description);
    }

    //Modifies: this
    //Effects: Prompts the user for the rest of the Expense attributes, creates an Expense with those attrobutes
    //         and adds it to expenseList.
    private void helpAddExpense2(String category, double price, String description) {
        int month;
        int day;
        System.out.print("Month: ");
        month = dayMonthAddHelper(true);
        System.out.print("Day: ");
        input.nextLine();
        day = dayMonthAddHelper(false);
        expenseList.addExpense(new Expense(category, price, description, month, day));
        System.out.println("Expense added successfully!\n");
        input.nextLine();
        helpRemoveObject(true);
        System.out.println("");
    }

    //Modifies: this
    //Effects: Asks the user if they mistyped the previous Item, if they did it removes that Item from its respective
    //         list.
    public void helpRemoveObject(boolean expenseOrBorrow) {
        String word = expenseOrBorrow ? "Expense" : "Borrow/Lend";
        boolean answer;
        //input.nextLine();
        System.out.print("Did you mistype anything, would you like us to remove the " + word + " (T)/(F)?: ");
        answer = trueFalseHelper("Remove Last " + word + " (T)/(F)?: ");
        if (answer == true) {
            expenseList.removeLastExpense();
            System.out.println(word + " removed successfully!");
        } else {
            System.out.println(word + " was kept!");
        }

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

    //Effects: saves ExpenseList to file
    private void saveExpenseList() {
        try {
            jsonWriter.open();
            jsonWriter.write(expenseList);
            jsonWriter.close();
            System.out.println("Successfully saved to " + JSON_STORE + "!");
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + JSON_STORE);
        }
    }

    //Modifies: this
    //Effects: loads ExpenseList from file, prints error message if file doesn't exist.
    private void loadExpenseList() {
        try {
            System.out.println("\nLoading Personal Expense List from file...");
            expenseList = jsonReader.read(); //loads list
            System.out.println("Successfully loaded from " + JSON_STORE + "!\n");
            System.out.println("Welcome back " + expenseList.getUserName() + "\n");
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + JSON_STORE + "\n");
            //add a return value? To tell the user to create a new expenseList instead.
        }
    }

    //BorrowLend starts, want to transfer this code to BorrowLendApp

    //Effects: Runs the BorrowLend App, which allows users to add Borrow or Lend Objects
    public void borrowLendRun() {
        if (borrowLendTracker == 0) {
            System.out.println("\nWelcome to the Borrow/Lend Section!"
                    + "\nHere you can track who you Borrowed or Lent money to.");
            borrowLendTracker++;
        }
        int answer = 10;
        while (answer != 0) {
            answer = borrowLendMenu();
            switch (answer) {
                case 2:
                    helpAddBorrowLend();
                    break;
                case 1:
                    helpViewBorrowLendList();
                    break;
                case 0:
                    break;
            }
        }
        System.out.println("");
    }

    //Effects: presents the user with the BorrowLend menu
    private int borrowLendMenu() {
        System.out.println("\nBorrow/Lend Menu:");
        System.out.println("(1) View Borrow/Lend List.");
        System.out.println("(2) Add new Borrow/Lend Object.");
        System.out.println("(0) Exit.");
        String question = "Choose an option: ";
        System.out.print(question);
        int choice = inputChecker(2, question);
        return choice;
    }

    //Effects: helper method to print the current BorrowLend List
    private void helpViewBorrowLendList() {
        if (expenseList.getBorrowLendList().isEmpty()) {
            System.out.println("\nYour current Borrow/Lend List is empty!");
        } else {
            System.out.println("\nYour current Borrow/Lend List is:");
            for (BorrowLend b : expenseList.getBorrowLendList()) {
                System.out.println(b.toString());
            }
        }
        //System.out.println("");
    }

    //Modifies: this
    //Effects: Asks user for 1st half of the BorrowLend attributes
    private void helpAddBorrowLend() {
        String name;
        double amount;
        String description;
        input.nextLine(); //these lines are used to clear any leftover input
        System.out.println("\nSet Borrow/Lend Attributes:");
        System.out.print("Other's Name: ");
        name = input.nextLine();
        System.out.print("Amount: ");
        amount = priceInputChecker();
        input.nextLine();
        System.out.print("Description: ");
        description = input.nextLine();
        helpAddBorrowLendSecond(name, amount, description);
    }

    //Modifies: this
    //Effects: Asks user for 2nd half of the BorrowLend attributes and creates adds BorrowLend object to the list
    private void helpAddBorrowLendSecond(String name, double amount, String description) {
        int month;
        int day;
        boolean borrowLend;
        System.out.print("Month: ");
        month = dayMonthAddHelper(true);
        System.out.print("Day: ");
        input.nextLine();
        day = dayMonthAddHelper(false);
        input.nextLine();
        System.out.print("Borrow or Lend (T)/(F)?: ");
        borrowLend = trueFalseHelper("Borrow or Lend (T)/(F)?: ");
        expenseList.addBorrowLend(new BorrowLend(name, amount, description, month, day, borrowLend));
        System.out.println("BorrowLend added successfully!\n");
        helpRemoveObject(false);
    }

}

//Ideas:
//Add return value to loadExpenseList() --> to bring the user to welcome menu if the file does not exist
//Ask the user if they want to change one of the added default categories (if they are unsatisfied with it)
//Print the current default categories //
//Add a name to each Personal Expense List (each one can be printed to different files) //
//Print a table of expenses for a month period?
//Implement LocalDayTime
//Add money lending a burrowing list to ExpenseList --> track who owes you money and who you owe money to //
//Add a target spending limit per month for each category --> notify the user when they are getting close to it
//Give the user the option to delete the previous Expense they added --> if they mistyped something //