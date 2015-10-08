package de.tg76.smartparking;

/**
 * Created by adm_toto on 04/10/2015.
 * User details stored on a local file
 */
public class User {

    //Variable to hold value of user
    String name, username, password, email;

    //Constructor
    public User (String name, String email,String username, String password){
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    //Constructor
    public User (String username, String password){
        this.username = username;
        this.password = password;
        this.email = "";
        this.name = "";
    }
}
