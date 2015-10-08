package de.tg76.smartparking;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Register extends AppCompatActivity implements View.OnClickListener{

    //Variable to hold value from activity_register.xml form
    EditText etName, etEmail, etUsername,etPassword;
    Button bRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //Assign ID to variable from activity_register.xml form
        etName = (EditText) findViewById(R.id.etName);
        etEmail = (EditText) findViewById(R.id.etEmail);
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bRegister = (Button)findViewById(R.id.bRegister);

        //Creating listener for button bRegister
        bRegister.setOnClickListener(this);
    }

    //Register customer
    @Override
    public void onClick(View v) {
        //Check ID value
        switch (v.getId()){
            case R.id.bRegister:
                String name = etName.getText().toString();
                String email = etEmail.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                //Create instance of user object
                User user = new User(name,email,username,password);
                //Store user data - calling function
                registerUser(user);
                break;
        }
    }
    //Store user data
    private void registerUser(User user){
        ServerRequests serverRequests = new ServerRequests(this);
        serverRequests.storeUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                 startActivity(new Intent(Register.this, Login.class));
            }
        });
    }
}
