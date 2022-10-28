package persistence;

import org.json.JSONObject;

//Taken from the JsonSerializationDemo code
public interface Writable {
    //Effects: Will return this as a Json Object
    JSONObject toJson();
}
