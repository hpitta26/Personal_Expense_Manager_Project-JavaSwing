package ui.exceptions;

//Class that represents Exception that is thrown/caught if the user types an Invalid Input
public class InvalidInputException extends Exception {
    public InvalidInputException(String msg) {
        super(msg);
    }
}