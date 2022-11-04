package ui;

import model.BorrowLend;
import model.Expense;

//Represents the BorrowLendApp within the PersonalExpenseApp
public class BorrowLendApp extends PersonalExpenseApp {
    private int tracker = 0;

    public void run() {
        if (tracker == 0) {
            System.out.println("Welcome to the BorrowLend App!\nHere you can track who you Borrowed or Lent money to.");
            tracker++;
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
    }

    public int borrowLendMenu() {
        System.out.println("\nBorrow/Lend Menu:");
        System.out.println("(1) View Borrow/Lend List.");
        System.out.println("(2) Add new Borrow/Lend Object.");
        System.out.println("(0) Exit.");
        String question = "Choose an option: ";
        System.out.print(question);
        int choice = inputChecker(2, question);
        return choice;
    }

    public void helpViewBorrowLendList() {
        System.out.println("Your current Borrow/Lend List is:");
        for (BorrowLend b : expenseList.getBorrowLendList()) {
            System.out.println(b.toString());
        }
        System.out.println("");
    }

    public void helpAddBorrowLend() {
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

    private void helpAddBorrowLendSecond(String name, double amount, String description) {
        int month;
        int day;
        boolean borrowLend;
        System.out.print("Month: ");
        month = dayMonthAddHelper(true);
        System.out.print("Day: ");
        input.nextLine();
        day = dayMonthAddHelper(false);
        System.out.print("Borrow or Lend (T)/(F)?: ");
        borrowLend = trueFalseHelper("Borrow or Lend (T)/(F)?: ");
        expenseList.addBorrowLend(new BorrowLend(name, amount, description, month, day, borrowLend));
        System.out.println("BorrowLend added successfully!\n");
        helpRemoveObject(false);
    }

    //Remove Borrow Lend //Remove last, or specify a specific one





}
