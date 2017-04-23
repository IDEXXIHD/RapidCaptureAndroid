package com.idexx.labstation.rapidcaptureapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.idexx.labstation.rapidcaptureapp.db.DBHelper;
import com.idexx.labstation.rapidcaptureapp.db.UserSettingsContract;
import com.idexx.labstation.rapidcaptureapp.db.UserSettingsDbAccessor;
import com.idexx.labstation.rapidcaptureapp.util.network.NetworkAccessor;
import com.idexx.labstation.rapidcaptureapp.util.network.NetworkActions;

import java.util.List;
import java.util.Map;

public class LauncherActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        DBHelper.initialize(getApplicationContext());
        NetworkAccessor.getInstance().initialize("192.168.1.106", "", 3000);

        setContentView(R.layout.activity_launcher);
        checkForCreds();
    }

    private void checkForCreds()
    {
        AsyncTask<Object, Object, NetworkActions.TokenValidation> task = new AsyncTask<Object, Object, NetworkActions.TokenValidation>()
        {
            @Override
            protected NetworkActions.TokenValidation doInBackground(Object[] params)
            {
                List<Map<String, Object>> users = DBHelper.getDbAccessor(UserSettingsDbAccessor.class).getActiveUsers();
                if(users.size() > 0)
                {
                    String token = (String) users.get(0).get(UserSettingsContract.TOKEN_COLUMN);
                    NetworkActions.TokenValidation validation = NetworkActions.validateToken(token);
                    if(validation == NetworkActions.TokenValidation.SUCCESS)
                    {
                        NetworkAccessor.getInstance().setCurrentToken(token);
                    }
                    return validation;
                }
                return NetworkActions.TokenValidation.FAILURE;
            }

            @Override
            protected void onPostExecute(NetworkActions.TokenValidation tokenValidation)
            {
                if(tokenValidation == NetworkActions.TokenValidation.SUCCESS)
                {
                    goToHome();
                }
                else if(tokenValidation == NetworkActions.TokenValidation.FAILURE)
                {
                    goToLogin();
                }
                else if (tokenValidation == NetworkActions.TokenValidation.NOT_AVAILABLE)
                {
                    findViewById(R.id.progressBar).setVisibility(View.GONE);
                    new AlertDialog.Builder(LauncherActivity.this)
                            .setTitle("Server Unavailable")
                            .setMessage("Unable to reach server. Please try again later.")
                            .setPositiveButton("OK", null)
                            .setOnDismissListener(new DialogInterface.OnDismissListener()
                            {
                                @Override
                                public void onDismiss(DialogInterface dialog)
                                {
                                    finishAndRemoveTask();
                                }
                            })
                            .show();
                }
            }
        };
        task.execute();
    }

    private void goToHome()
    {
        Intent intent = new Intent(this, HomeActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    private void goToLogin()
    {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

}
