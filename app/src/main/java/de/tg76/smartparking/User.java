package de.tg76.smartparking;

/**
 * User class - Creates user and holds attributes of user
 */
public class User {

    //Variable to hold value of user
    String name, username, password, email;

    //Constructor when user register
    public User (String name, String email,String username, String password){
        this.name = name;
        this.username = username;
        this.password = password;
        this.email = email;
    }

    //Constructor when user logs on
    public User (String username, String password){
        this.username = username;
        this.password = password;
        this.email = "";
        this.name = "";
    }
}
