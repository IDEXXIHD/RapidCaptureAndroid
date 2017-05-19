package com.idexx.labstation.rapidcaptureapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.idexx.labstation.rapidcaptureapp.model.UpdateUserDto;
import com.idexx.labstation.rapidcaptureapp.model.UserDetailsDto;
import com.idexx.labstation.rapidcaptureapp.util.TextChangedWatcher;
import com.idexx.labstation.rapidcaptureapp.util.network.NetworkActions;

import java.util.Map;

public class UpdateUserActivity extends AppCompatActivity
{
    private Button submitButton;
    private EditText userNameInput;
    private EditText passwordInput;
    private EditText passwordConfirmInput;
    private EditText emailInput;
    private EditText firstNameInput;
    private EditText lastNameInput;

    private boolean passwordsMatch = true;
    private UserDetailsDto activeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);

        bindFields();
        findActiveUser();
    }

    private void bindFields()
    {
        submitButton = (Button) findViewById(R.id.updateUserSubmitButton);
        userNameInput = (EditText) findViewById(R.id.updateUserUsernameEditText);
        passwordInput = (EditText) findViewById(R.id.updateUserPasswordEditText);
        passwordConfirmInput = (EditText) findViewById(R.id.updateUserPasswordConfirmEditText);
        emailInput = (EditText) findViewById(R.id.updateUserEmailEditText);
        firstNameInput = (EditText) findViewById(R.id.updateUserFirstNameEditText);
        lastNameInput = (EditText) findViewById(R.id.updateUserLastNameEditText);

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

        ValidatingTextWatcher watcher = new ValidatingTextWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                if (passwordConfirmInput.getText().toString().equals(passwordInput.getText().toString()))
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
        };
        passwordInput.addTextChangedListener(watcher);
        passwordConfirmInput.addTextChangedListener(watcher);
    }

    private void findActiveUser()
    {
        new AsyncTask<Object, Object, UserDetailsDto>()
        {
            @Override
            protected UserDetailsDto doInBackground(Object... params)
            {
                UserDetailsDto userDetailsDto = NetworkActions.getUserDetails();
                return userDetailsDto;
            }

            @Override
            protected void onPostExecute(UserDetailsDto user)
            {
                super.onPostExecute(user);
                UpdateUserActivity.this.activeUser = user;
                initializeFields();
            }
        }.execute();
    }

    private void initializeFields()
    {
        this.userNameInput.setText(activeUser.getUserName());
        this.emailInput.setText(activeUser.getUserEmail());
        this.firstNameInput.setText(activeUser.getFirstName());
        this.lastNameInput.setText(activeUser.getLastName());
    }

    private void validate()
    {
        submitButton.setEnabled(
                passwordsMatch
        );
    }

    private String getNullIfEmpty(EditText input)
    {
        String val = input.getText().toString();
        return val.trim().length() == 0 ? null : val.trim();
    }

    private void handleSubmit()
    {
        final UpdateUserDto updatedUser = new UpdateUserDto();
        updatedUser.setUsername(getNullIfEmpty(userNameInput));
        updatedUser.setEmail(getNullIfEmpty(emailInput));
        updatedUser.setPassword(getNullIfEmpty(passwordInput));

        UpdateUserDto.UserProfileDto profileDto = new UpdateUserDto.UserProfileDto();
        profileDto.setGivenName(getNullIfEmpty(firstNameInput));
        profileDto.setFamilyName(getNullIfEmpty(lastNameInput));
        updatedUser.setProfile(profileDto.getGivenName() == null && profileDto.getFamilyName() == null ? null : profileDto);

        //noinspection unchecked
        new AsyncTask<Map<String, Object>, Object, NetworkActions.ResponseStatus>()
        {
            @Override
            protected NetworkActions.ResponseStatus doInBackground(Map<String, Object>... params)
            {
                return NetworkActions.updateUser(updatedUser);
            }

            @Override
            protected void onPostExecute(NetworkActions.ResponseStatus responseStatus)
            {
                if(responseStatus == NetworkActions.ResponseStatus.SUCCESS)
                {
                    new AlertDialog.Builder(UpdateUserActivity.this)
                            .setTitle("Created Successfully")
                            .setMessage("User updated successfully.")
                            .setPositiveButton("OK", null)
                            .setOnDismissListener(new DialogInterface.OnDismissListener()
                            {
                                @Override
                                public void onDismiss(DialogInterface dialog)
                                {
                                    userUpdatedSuccessfully();
                                }
                            })
                            .show();
                }
                else if (responseStatus == NetworkActions.ResponseStatus.FAILURE)
                {
                    new AlertDialog.Builder(UpdateUserActivity.this)
                            .setTitle("Error")
                            .setMessage("Error updating user.")
                            .setPositiveButton("OK", null)
                            .show();
                    clearPasswordFields();
                }
                else if (responseStatus == NetworkActions.ResponseStatus.NOT_AVAILABLE)
                {
                    new AlertDialog.Builder(UpdateUserActivity.this)
                            .setTitle("Server Unavailable")
                            .setMessage("Error contacting RapidCapture server.")
                            .setPositiveButton("OK", null)
                            .show();
                    clearPasswordFields();
                }
            }
        }.execute();
        //TODO
    }

    private void clearPasswordFields()
    {
        passwordInput.getText().clear();
        passwordConfirmInput.getText().clear();
    }

    private void userUpdatedSuccessfully()
    {
        Intent intent = new Intent(this, HomeActivity.class);
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
