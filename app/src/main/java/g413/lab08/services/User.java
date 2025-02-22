package g413.lab08.services;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("id")
    public int id;
    @SerializedName("login")
    public String login;
    @SerializedName("password")
    public String password;
}
