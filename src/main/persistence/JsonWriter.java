package persistence;

import model.ExpenseList;
import org.json.JSONObject;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

//Used JsonSerializationDemo as a template

//Object to write JSONObjects to a file
public class JsonWriter {
    private String destination;
    private PrintWriter writer;
    private static final int TAB = 4;

    //Effects: sets destination to the directory of the file that is inputted
    public JsonWriter(String destination) {
        this.destination = destination;
    }

    //Effects: opens the writer so it can be used to write to files, throws FileNotFoundException in the case
    //         the destination cannot be written to.
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }

    //Modifies: This
    //Effects: Writes an ExpenseList as a JSONObject to the destination file
    public void write(ExpenseList expenseList) {
        JSONObject json = expenseList.toJson();
        saveToFile(json.toString(TAB));
    }

    //Modifies: This
    //Effects: Closes the writer
    public void close() {
        writer.close();
    }

    //Modifies: This
    //Effects: writes String to file
    public void saveToFile(String json) {
        writer.print(json);
    }

}
