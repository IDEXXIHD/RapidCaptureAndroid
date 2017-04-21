package com.idexx.labstation.rapidcaptureapp;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.idexx.labstation.rapidcaptureapp.model.UserLoginDto;
import com.idexx.labstation.rapidcaptureapp.util.NetworkAccessor;
import com.idexx.labstation.rapidcaptureapp.util.NetworkActions;

import java.util.Map;

public class LaunchActivity extends AppCompatActivity
{
    private LinearLayout loginWrapper;
    private ProgressBar spinner;
    private Button loginButton;
    private EditText userField;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        NetworkAccessor.getInstance().initialize("192.168.1.108", "", 3000);

        setContentView(R.layout.activity_launch);
        bindFields();
        bindHandlers();

        checkForCreds();
    }

    private void bindFields()
    {
        spinner = (ProgressBar) findViewById(R.id.launchProgressBar);
        loginWrapper = (LinearLayout) findViewById(R.id.launchLoginWrapper);
        loginButton = (Button) findViewById(R.id.launchLoginButton);
        userField = (EditText) findViewById(R.id.launchUserInputField);
        passwordField = (EditText) findViewById(R.id.launcPasswordInputField);
    }

    private void bindHandlers()
    {
        loginButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                checkLoginFields();
            }
        });
    }

    private void checkForCreds()
    {
        AsyncTask task = new AsyncTask()
        {
            @Override
            protected Object doInBackground(Object[] params)
            {
                try
                {
                    Thread.sleep(1500);
                }
                catch (InterruptedException e)
                {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(Object o)
            {
                spinner.setVisibility(View.GONE);
                loginWrapper.setVisibility(View.VISIBLE);
                super.onPostExecute(o);
            }
        };
        task.execute();
    }

    private void checkLoginFields()
    {
        String user = userField.getText().toString();
        String pass = passwordField.getText().toString();
        if(user == null || user.length() == 0)
        {
            new AlertDialog.Builder(this)
                    .setTitle("No User")
                    .setMessage("Username must not be empty")
                    .setPositiveButton("OK", null)
                    .show();
        }
        else if (pass == null || pass.length() == 0)
        {
            new AlertDialog.Builder(this)
                    .setTitle("No Password")
                    .setMessage("Password must not be empty")
                    .setPositiveButton("OK", null)
                    .show();
        }
        else
        {
            UserLoginDto userLoginDto = new UserLoginDto();
            userLoginDto.setUsername(user);
            userLoginDto.setPassword(pass);
            attemptLogin(userLoginDto);
        }
    }

    private void attemptLogin(final UserLoginDto userLoginDto)
    {
        final ProgressDialog progressDialog = ProgressDialog.show(this, "Logging in", "Attempting to log in");
        AsyncTask<UserLoginDto, Object, Map<String, Object>> loginTask = new AsyncTask<UserLoginDto, Object, Map<String,Object>>()
        {
            @Override
            protected Map<String, Object> doInBackground(UserLoginDto... params)
            {
                return NetworkActions.login(userLoginDto);
            }

            @Override
            protected void onPostExecute(Map<String, Object> stringObjectMap)
            {
                progressDialog.dismiss();
                if(stringObjectMap.containsKey("token"))
                {
                    //success, persist the token and proceed to the main page
                    new AlertDialog.Builder(LaunchActivity.this)
                            .setTitle("Success")
                            .setMessage("Successful Login")
                            .setPositiveButton("OK", null)
                            .show();
                }
                else
                {
                    //failure
                    new AlertDialog.Builder(LaunchActivity.this)
                            .setTitle("Invalid Login")
                            .setMessage("Invalid login credentials. Please try again.")
                            .setPositiveButton("OK", null)
                            .show();
                }
            }
        };
        loginTask.execute(userLoginDto);
    }
}
