package com.iconanalytics.petcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements  AsyncResponse{

    public static final int TEXT_REQUEST = 1;


    private SharedPreferences mPreferences;
    private String sharedPrefFile =
            "com.iconanalytics.petcare.credentials";  //the cookie file where login details will be stored

    String inputEmail = ""; //giving the email entered in UI a class access in order to be able to use in everywhere in class

    public ServerFetch serverFetch = new ServerFetch();  // the object that will be used asynchronously to get data over newtwork from petcalculator.com API



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        serverFetch.delegate = this;  // needed for async stuff

        mPreferences = getSharedPreferences(sharedPrefFile, MODE_PRIVATE);


        String user = mPreferences.getString("user","");

        Toast loginToast;
        if (user != ""){  //non empty user string retrieved from cookie file means a prior successful login

            loginToast = Toast.makeText(this, "You are logged in as: " + user,
                    Toast.LENGTH_LONG);
        }
        else{ //empty user, no prior successful login, display login prompt
            loginToast = Toast.makeText(this, "Please log in",
                    Toast.LENGTH_LONG);

            showLoginUI();
        }
        loginToast.show();

    }

    private void showLoginUI(){  //show the UI for loggining in

        Intent loginIntent = new Intent(this, Login.class);
        startActivityForResult(loginIntent,TEXT_REQUEST);

    }
    private void saveUserLogin(String username){  //save user login to cookie, this happens after a successful login
        SharedPreferences.Editor preferencesEditor = mPreferences.edit();
        preferencesEditor.putBoolean("loggedin",true);
        preferencesEditor.putString("user",username);
        preferencesEditor.apply();
    }


    private void authenticateUser(String inputEmail, String inputPassword){  //use the api over network to check if user exists in record


        // Check the status of the network connection.
        ConnectivityManager connMgr = (ConnectivityManager)
                getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = null;
        if (connMgr != null) {
            networkInfo = connMgr.getActiveNetworkInfo();
        }

        // If the network is available, connected, and the search field
        // is not empty, start a FetchBook AsyncTask.
        if (networkInfo != null && networkInfo.isConnected()) {
            String[] serverArgs = new String[]{"auth", "mobile_app", inputEmail,inputPassword};
            serverFetch.execute(serverArgs);
        }

    }

    public void onActivityResult(int requestCode,
                                 int resultCode, Intent data) {  //  callback method after login button is pressed on the login ui activity
        String[] reply= {"",""};
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == TEXT_REQUEST) {
            if (resultCode == RESULT_OK) {
                reply =
                        data.getStringArrayExtra(Login.EXTRA_REPLY);

            }
        }

        inputEmail = reply[0];
        String inputPassword = reply[1];


        authenticateUser(inputEmail,inputPassword);

    }

    @Override
    public void processFinish(String output) {  //async method that is called after the API server GET operation returns

        int output_int = Integer.parseInt(output);

        if (output_int >=0) {
            Toast success = Toast.makeText(this, "Login Successful: "+output,
                    Toast.LENGTH_LONG);
            success.show();
            saveUserLogin(inputEmail);
        }
        else {  //login failed, show Login UI again

            //inputEmail=""; //reset input email variable, as extra care
            //loginIntent = null;
            showLoginUI();
        }
    }
}