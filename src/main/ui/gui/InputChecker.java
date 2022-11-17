package ui.gui;

import ui.exceptions.InvalidInputException;

import javax.swing.*;

//Class used to check if the User's Input is valid for each category
public class InputChecker {
    JTextField textField;
    JLabel questionLabel;
    //String userInput;

    //EFFECTS: Initializes InputChecker with a reference of the App's TextField and Label
    //MODIFIES:
    public InputChecker(JTextField textField, JLabel label) {
        //this.textField = textField;
        this.questionLabel = label;
        this.textField = textField;
    }

    //EFFECTS: returns True or False if userInput is valid else throws Exception
    public boolean trueFalseChecker() throws InvalidInputException {
        String userInput = textField.getText();
        if (!userInput.equalsIgnoreCase("t") & !userInput.equalsIgnoreCase("f")) {
            throw new InvalidInputException("Invalid ");
        }
        boolean trueOrFalse = (userInput.equalsIgnoreCase("t")) ? true : false;
        return trueOrFalse;
    }

    //EFFECTS: returns day if userInput is a valid day else throws Exception
    public int dayInputChecker() throws InvalidInputException {
        String userInput = textField.getText();
        int day;
        try {
            day = Integer.parseInt(userInput);
            if (day < 1 | day > 31) {
                throw new InvalidInputException("Invalid day.");
            }
        } catch (Exception e) {
            throw new InvalidInputException("Invalid day.");
        }
        return day;
    }

    //EFFECTS: returns month if userInput is a valid month else throws Exception
    public int monthInputChecker() throws InvalidInputException {
        String userInput = textField.getText();
        int month;
        try {
            month = Integer.parseInt(userInput);
            if (month < 1 | month > 12) {
                throw new InvalidInputException("Invalid day.");
            }
        } catch (Exception e) {
            throw new InvalidInputException("Invalid day.");
        }
        return month;
    }

    //EFFECTS: returns price if userInput is a valid price else throws Exception
    public double priceInputChecker() throws InvalidInputException {
        String userInput = textField.getText();
        double price;
        try {
            price = Double.parseDouble(userInput);
            if (price <= 0) {
                throw new InvalidInputException("Invalid price.");
            }
        } catch (Exception e) {
            throw new InvalidInputException("Invalid price.");
        }
        return price;

    }

}
