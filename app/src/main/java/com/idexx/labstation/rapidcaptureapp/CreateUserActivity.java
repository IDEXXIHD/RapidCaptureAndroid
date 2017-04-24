package com.idexx.labstation.rapidcaptureapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.idexx.labstation.rapidcaptureapp.dao.GeneralSettingsDao;
import com.idexx.labstation.rapidcaptureapp.dao.UserDao;
import com.idexx.labstation.rapidcaptureapp.entity.DatabaseHelper;
import com.idexx.labstation.rapidcaptureapp.entity.User;
import com.idexx.labstation.rapidcaptureapp.model.NewUserDto;
import com.idexx.labstation.rapidcaptureapp.util.TextChangedWatcher;
import com.idexx.labstation.rapidcaptureapp.util.network.NetworkActions;

import java.sql.SQLException;
import java.util.Map;

public class CreateUserActivity extends AppCompatActivity
{
    private Button submitButton;
    private EditText userNameInput;
    private EditText passwordInput;
    private EditText passwordConfirmInput;
    private EditText emailInput;
    private EditText firstNameInput;
    private EditText lastNameInput;

    private boolean passwordsMatch;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_user);

        bindFields();
    }

    private void bindFields()
    {
        submitButton = (Button) findViewById(R.id.createUserSubmitButton);
        userNameInput = (EditText) findViewById(R.id.createUserUsernameEditText);
        passwordInput = (EditText) findViewById(R.id.createUserPasswordEditText);
        passwordConfirmInput = (EditText) findViewById(R.id.createUserPasswordConfirmEditText);
        emailInput = (EditText) findViewById(R.id.createUserEmailEditText);
        firstNameInput = (EditText) findViewById(R.id.createUserFirstNameEditText);
        lastNameInput = (EditText) findViewById(R.id.createUserLastNameEditText);

        submitButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                handleSubmit();
            }
        });

        userNameInput.addTextChangedListener(new ValidatingTextWatcher());
        emailInput.addTextChangedListener(new ValidatingTextWatcher());
        firstNameInput.addTextChangedListener(new ValidatingTextWatcher());
        lastNameInput.addTextChangedListener(new ValidatingTextWatcher());

        passwordConfirmInput.addTextChangedListener(new ValidatingTextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (s.toString().equals(passwordInput.getText().toString()))
                {
                    passwordsMatch = true;
                    passwordConfirmInput.setError(null);
                }
                else
                {
                    passwordsMatch = false;
                    passwordConfirmInput.setError("Passwords do not match");
                }
                super.onTextChanged(s, start, before, count);
            }
        });
    }

    private void validate()
    {
        submitButton.setEnabled(
                passwordsMatch
                && userNameInput.getText().toString().trim().length() != 0
                && emailInput.getText().toString().trim().length() != 0
        );
    }

    private void handleSubmit()
    {
        final NewUserDto newUser = new NewUserDto();
        newUser.setUsername(userNameInput.getText().toString());
        newUser.setEmail(emailInput.getText().toString());
        newUser.setPassword(passwordInput.getText().toString());
        NewUserDto.UserProfileDto profileDto = new NewUserDto.UserProfileDto();
        profileDto.setGivenName(firstNameInput.getText().toString());
        profileDto.setFamilyName(lastNameInput.getText().toString());
        newUser.setProfile(profileDto);

        //noinspection unchecked
        new AsyncTask<Map<String, Object>, Object, NetworkActions.ResponseStatus>()
        {
            @Override
            protected NetworkActions.ResponseStatus doInBackground(Map<String, Object>... params)
            {
                NetworkActions.ResponseStatus responseStatus = NetworkActions.createUser(newUser);
                if(responseStatus == NetworkActions.ResponseStatus.SUCCESS)
                {
                    User user = new User();
                    user.setUser(newUser.getUsername());

                    user = UserDao.getInstance().createIfNotExists(user, getApplicationContext());
                    GeneralSettingsDao.getInstance().setUserActive(user, getApplicationContext());
                }
                return responseStatus;
            }

            @Override
            protected void onPostExecute(NetworkActions.ResponseStatus responseStatus)
            {
                if(responseStatus == NetworkActions.ResponseStatus.SUCCESS)
                {
                    new AlertDialog.Builder(CreateUserActivity.this)
                            .setTitle("Created Successfully")
                            .setMessage("User created successfully. You may now log in with your credentials")
                            .setPositiveButton("OK", null)
                            .setOnDismissListener(new DialogInterface.OnDismissListener()
                            {
                                @Override
                                public void onDismiss(DialogInterface dialog)
                                {
                                    userCreatedSuccessfully();
                                }
                            })
                            .show();
                }
                else if (responseStatus == NetworkActions.ResponseStatus.FAILURE)
                {
                    new AlertDialog.Builder(CreateUserActivity.this)
                            .setTitle("Error")
                            .setMessage("Error creating user. Make sure one does not already exist with the given username and e-mail address")
                            .setPositiveButton("OK", null)
                            .show();
                    clearPasswordFields();
                }
                else if (responseStatus == NetworkActions.ResponseStatus.NOT_AVAILABLE)
                {
                    new AlertDialog.Builder(CreateUserActivity.this)
                            .setTitle("Server Unavailable")
                            .setMessage("Error contacting RapidCapture server.")
                            .setPositiveButton("OK", null)
                            .show();
                    clearPasswordFields();
                }
            }
        }.execute();

    }

    private void clearPasswordFields()
    {
        passwordInput.getText().clear();
        passwordConfirmInput.getText().clear();
    }

    private void userCreatedSuccessfully()
    {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private class ValidatingTextWatcher extends TextChangedWatcher
    {

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count)
        {
            validate();
        }
    }
}
