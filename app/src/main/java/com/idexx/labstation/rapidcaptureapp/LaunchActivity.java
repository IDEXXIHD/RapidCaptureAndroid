package com.idexx.labstation.rapidcaptureapp;

import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import com.idexx.labstation.rapidcaptureapp.db.DBHelper;
import com.idexx.labstation.rapidcaptureapp.db.UserSettingsContract;
import com.idexx.labstation.rapidcaptureapp.db.UserSettingsDbAccessor;
import com.idexx.labstation.rapidcaptureapp.model.UserLoginDto;
import com.idexx.labstation.rapidcaptureapp.util.NetworkAccessor;
import com.idexx.labstation.rapidcaptureapp.util.NetworkActions;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class LaunchActivity extends AppCompatActivity
{
    private LinearLayout loginWrapper;
    private ProgressBar spinner;
    private Button loginButton;
    private EditText userField;
    private EditText passwordField;

    private String activeUserName;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        DBHelper.initialize(getApplicationContext());

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
        AsyncTask<Object, Object, Boolean> task = new AsyncTask<Object, Object, Boolean>()
        {
            @Override
            protected Boolean doInBackground(Object[] params)
            {
                List<Map<String, Object>> users = DBHelper.getDbAccessor(UserSettingsDbAccessor.class).getActiveUsers();
                if(users.size() > 0)
                {
                    String token = (String) users.get(0).get(UserSettingsContract.TOKEN_COLUMN);
                    activeUserName = (String) users.get(0).get(UserSettingsContract.USER_COLUMN);
                    return NetworkActions.validateToken(token);
                }
                return false;
            }

            @Override
            protected void onPostExecute(Boolean contToHome)
            {
                spinner.setVisibility(View.GONE);
                if(contToHome)
                {
                    onSuccessfulLogin();
                }
                else
                {
                    userField.setText(activeUserName);
                    loginWrapper.setVisibility(View.VISIBLE);
                }
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
        AsyncTask<UserLoginDto, Object, Boolean> loginTask = new AsyncTask<UserLoginDto, Object, Boolean>()
        {
            @Override
            protected Boolean doInBackground(UserLoginDto... params)
            {
                Map<String, Object> resp = NetworkActions.login(userLoginDto);
                if(resp.containsKey("token"))
                {
                    //Check if user exists
                    Map<String, Object> userEntity = new HashMap<>();
                    userEntity.put(UserSettingsContract.USER_COLUMN, userLoginDto.getUsername());
                    userEntity.put(UserSettingsContract.TOKEN_COLUMN, resp.get("token"));
                    long key = DBHelper.getDbAccessor(UserSettingsDbAccessor.class).create(userEntity);

                    DBHelper.getInstance().getDbAccessor(UserSettingsDbAccessor.class).setUserActive(key);
                    activeUserName = userLoginDto.getUsername();
                    return true;
                }
                else
                {
                    return false;
                }
            }

            @Override
            protected void onPostExecute(Boolean success)
            {
                progressDialog.dismiss();
                if(success)
                {
                   onSuccessfulLogin();
                }
                else
                {
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

    private void onSuccessfulLogin()
    {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
