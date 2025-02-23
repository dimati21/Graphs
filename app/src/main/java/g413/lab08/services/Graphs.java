package g413.lab08.services;
import com.google.gson.annotations.SerializedName;

public class Graphs {
    @SerializedName("id")
    String id;
    @SerializedName("name")
    String name;

    public String toString(){
        return name;
    }

    public String getId(){
        return id;
    }


}
