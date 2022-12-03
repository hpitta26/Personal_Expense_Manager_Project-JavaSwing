package ui.gui;

import model.Event;
import model.EventLog;

import javax.swing.*;
import java.awt.*;

//Represents ExpenseLog JFrame where the ExpenseLog is displayed when the user exits the App
public class ExpenseLogView extends JFrame {
    Container screen;
    JTextArea logTextArea;

    //EFFECTS: Creates a Panel that will contain the ExpenseLog
    //MODIFIES: this
    public ExpenseLogView() {
        super("Event Log");
        setSize(600,800); //ExpenseList Panel 730 + Right Panel 250 (prev width 1000)
        //setLocation(1, 1);
        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        screen = this.getContentPane();
        screen.setLayout(new BorderLayout(0, 0));

        logTextArea = new JTextArea();
        logTextArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(logTextArea);
        add(scrollPane);

        setVisible(false); //allows the contents to be seen, must be at the end of the constructor, or main method
    }

    //Used AlarmSystem as a template
    //EFFECTS: updates the JTextArea and displays the Events in the EventLog
    //MODIFIES: this
    public void updateEventLog(EventLog el) {
        for (Event next : el) {
            logTextArea.setText(logTextArea.getText() + next.toString() + "\n\n");
        }

        repaint();
    }

}


