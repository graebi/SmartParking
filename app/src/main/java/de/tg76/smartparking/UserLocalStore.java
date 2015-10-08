package de.tg76.smartparking;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by adm_toto on 04/10/2015.
 * This class stores user data into a file locally
 */
public class UserLocalStore {

    //Static variable for holding user data
    public static final String SP_NAME = "userDetails";
    SharedPreferences userLocalDatabase;

    //Constructor
    //Get context of current state of the SP_NAME file which stores the user details
    public  UserLocalStore(Context context){
        userLocalDatabase = context.getSharedPreferences(SP_NAME,0);
    }

    //Function to update SharedPreference user data
    public void storeUserData(User user){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putString("name",user.name);
        spEditor.putString("email",user.email);
        spEditor.putString("username",user.username);
        spEditor.putString("password",user.password);
        spEditor.commit();
    }

    //Function getting data of user which is logged in - returns user
    public  User getLoggedInUser(){
        String name = userLocalDatabase. getString("name", "");
        String email = userLocalDatabase.getString("email", "");
        String username = userLocalDatabase.getString("username", "");
        String password = userLocalDatabase.getString("password","");

        //Creating new user and return
        User storedUser = new User(name, email,username,password );
        return storedUser;
    }

    //Function to set loggedIn if user is logged in
    public void setUserLoggedIn(boolean loggedIn){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.putBoolean("loggedIn",loggedIn);
        spEditor.commit();
    }

    //Function if user is logged in or out to application
    public boolean getUserLoggedIn(){
        if(userLocalDatabase.getBoolean("loggedIn",false) == true){
            return true;
        }else{
            return false;
        }
    }
    //Function to clear user data
    public void clearUserData(){
        SharedPreferences.Editor spEditor = userLocalDatabase.edit();
        spEditor.clear();
        spEditor.commit();
    }

}
