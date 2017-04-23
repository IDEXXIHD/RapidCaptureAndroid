package com.idexx.labstation.rapidcaptureapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.idexx.labstation.rapidcaptureapp.db.DBHelper;
import com.idexx.labstation.rapidcaptureapp.db.UserSettingsContract;
import com.idexx.labstation.rapidcaptureapp.db.UserSettingsDbAccessor;

import java.util.Map;

public class HomeActivity extends AppCompatActivity
{
    private Button signoutButton;
    private TextView welcomeText;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bindFields();
        addListeners();
    }

    private void bindFields()
    {
        signoutButton = (Button) findViewById(R.id.homeSignoutButton);
        welcomeText = (TextView) findViewById(R.id.homeWelcomeLabel);

        findActiveUser();
    }

    private void addListeners()
    {
        signoutButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                signout();
            }
        });
    }

    private void findActiveUser()
    {
        Map<String, Object> activeUser = DBHelper.getDbAccessor(UserSettingsDbAccessor.class).getActiveUsers().get(0);
        String user = (String) activeUser.get(UserSettingsContract.USER_COLUMN);
        welcomeText.setText("Welcome " + user);
    }

    private void signout()
    {
        DBHelper.getDbAccessor(UserSettingsDbAccessor.class).clearActiveUsers();
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
