package com.idexx.labstation.rapidcaptureapp;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.idexx.labstation.rapidcaptureapp.dao.GeneralSettingsDao;
import com.idexx.labstation.rapidcaptureapp.dao.UserDao;
import com.idexx.labstation.rapidcaptureapp.entity.User;
import com.idexx.labstation.rapidcaptureapp.model.LoginResponseDto;
import com.idexx.labstation.rapidcaptureapp.model.UserLoginDto;
import com.idexx.labstation.rapidcaptureapp.util.GeneralUtil;
import com.idexx.labstation.rapidcaptureapp.util.network.NetworkAccessor;
import com.idexx.labstation.rapidcaptureapp.util.network.NetworkActions;

public class LoginActivity extends AppCompatActivity
{
    private Button loginButton;
    private EditText userField;
    private EditText passwordField;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_login);
        bindFields();
        bindHandlers();
        checkForActiveUser();
    }


    private void bindFields()
    {
        loginButton = (Button) findViewById(R.id.loginLoginButton);
        userField = (EditText) findViewById(R.id.loginUserEditText);
        passwordField = (EditText) findViewById(R.id.loginPasswordEditText);
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

    private void checkForActiveUser()
    {
        new AsyncTask<Object, Object, User>()
        {
            @Override
            protected User doInBackground(Object... params)
            {
                return GeneralSettingsDao.getInstance().getActiveUser(getApplicationContext());
            }

            @Override
            protected void onPostExecute(User user)
            {
                if(user != null)
                {
                    userField.setText(user.getUser());
                    passwordField.requestFocus();
                }
            }
        }.execute();
    }

    private void checkLoginFields()
    {
        GeneralUtil.hideKeyboard(getCurrentFocus());
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
        final ProgressDialog progressDialog = ProgressDialog.show(this, null, "Signing in...");
        AsyncTask<UserLoginDto, Object, Boolean> loginTask = new AsyncTask<UserLoginDto, Object, Boolean>()
        {
            @Override
            protected Boolean doInBackground(UserLoginDto... params)
            {
                LoginResponseDto resp = NetworkActions.login(userLoginDto);
                if(resp != null && resp.getToken() != null)
                {
                    User userEntity = UserDao.getInstance().getByUsername(userLoginDto.getUsername(), getApplicationContext());
                    userEntity = userEntity == null ? new User() : userEntity;

                    userEntity.setUser(userLoginDto.getUsername());
                    userEntity.setJwtToken(resp.getToken());
                    userEntity.setRole(resp.getRole());

                    UserDao.getInstance().createOrUpdate(userEntity, getApplicationContext());
                    GeneralSettingsDao.getInstance().setUserActive(userEntity, getApplicationContext());
                    NetworkAccessor.getInstance().setCurrentToken(resp.getToken());
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
                    new AlertDialog.Builder(LoginActivity.this)
                            .setTitle("Unable to login")
                            .setMessage("There was an issue logging in. Please make sure your credentials are correct, and that the RapidCapture server is available.")
                            .setPositiveButton("OK", null)
                            .setNeutralButton("Configure Server", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    goToServerConfig();
                                }
                            })
                            .show();
                }
            }
        };
        loginTask.execute(userLoginDto);
    }

    private void onSignup()
    {
        Intent intent = new Intent(this, UpdateUserActivity.class);
        startActivity(intent);
    }

    private void onSuccessfulLogin()
    {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void goToServerConfig()
    {
        Intent intent = new Intent(this, ServerConfigActivity.class);
        startActivity(intent);
    }
}
