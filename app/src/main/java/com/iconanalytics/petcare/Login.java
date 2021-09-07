package com.iconanalytics.petcare;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class Login extends AppCompatActivity {

    public static final String EXTRA_REPLY =
            "com.iconanalytics.petcare.extra.REPLY";

    private TextView username;
    private EditText password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        username = (TextView) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
    }

    public void parseLogin(View view){

        String usernameText = username.getText().toString();
        String passwordText = password.getText().toString();

        String[] reply = {usernameText, passwordText};
        Intent replyIntent = new Intent();
        replyIntent.putExtra(EXTRA_REPLY, reply);
        setResult(RESULT_OK, replyIntent);
        finish();


    }

}