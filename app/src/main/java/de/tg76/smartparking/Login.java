package de.tg76.smartparking;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import javax.security.auth.login.LoginException;

//irgnedow now ein faheler

public class Login extends ActionBarActivity implements View.OnClickListener {

    //Variable to hold value from activity_login.xml form
    Button bLogin;
    EditText etUsername, etPassword;
    TextView tvRegisterLink;

    UserLocalStore userLocalStore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Assign ID to variable from activity_login.xml form
        etUsername = (EditText) findViewById(R.id.etUsername);
        etPassword = (EditText) findViewById(R.id.etPassword);
        bLogin = (Button)findViewById(R.id.bLogin);
        tvRegisterLink = (TextView)findViewById(R.id.tvRegisterLink);

        //Creating listener
        bLogin.setOnClickListener(this);
        tvRegisterLink.setOnClickListener(this);

        userLocalStore = new UserLocalStore(this);

    }

    @Override
    public void onClick(View v) {
        //Check ID value
        switch (v.getId()){
            //By click on button login
            case R.id.bLogin:
                //Update local storage user data
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();

                User user = new User(username, password);

                authenticate(user);
                break;

            //By click on button register => call register activity
            case R.id.tvRegisterLink:
                startActivity(new Intent(this,Register.class));
                break;

        }
    }

    private void authenticate(User user){
            ServerRequests serverRequest = new ServerRequests(this);
            serverRequest.fetchUserDataInBackground(user, new GetUserCallback() {
                @Override
                public void done(User returnedUser) {
                    if (returnedUser == null){
                        showErrorMessage();
                    }else{
                        logUserIn(returnedUser);
                    }
                }
        });
    }

    private void showErrorMessage(){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Login.this);
        dialogBuilder.setMessage("Incorrect uer details");
        dialogBuilder.setPositiveButton("OK", null);
        dialogBuilder.show();
    }

    private void logUserIn(User returnedUser){
        userLocalStore.storeUserData(returnedUser);
        userLocalStore.setUserLoggedIn(true);

        startActivity(new Intent(this, MainActivity.class));
    }

}

