package com.idexx.labstation.rapidcaptureapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.idexx.labstation.rapidcaptureapp.dao.GeneralSettingsDao;
import com.idexx.labstation.rapidcaptureapp.dao.UserDao;
import com.idexx.labstation.rapidcaptureapp.entity.User;
import com.idexx.labstation.rapidcaptureapp.util.network.NetworkAccessor;
import com.idexx.labstation.rapidcaptureapp.util.network.NetworkActions;

import java.sql.SQLException;

public class LauncherActivity extends AppCompatActivity
{

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
    }

    @Override
    protected void onPostResume()
    {
        super.onPostResume();
        initialize();
    }

    private void initialize()
    {
        if(NetworkAccessor.getInstance().initialize(getApplicationContext()))
        {
            setContentView(R.layout.activity_launcher);
            checkForCreds();
        }
        else
        {
            //Server info not set, need to load server config screen
            new AlertDialog.Builder(this)
                    .setTitle("Setup Required")
                    .setMessage("Please configure the RapidCapture server settings")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            goToServerConfig();
                        }
                    }).show();
        }
    }

    private void checkForCreds()
    {
        AsyncTask<Object, Object, NetworkActions.ResponseStatus> task = new AsyncTask<Object, Object, NetworkActions.ResponseStatus>()
        {
            @Override
            protected NetworkActions.ResponseStatus doInBackground(Object[] params)
            {
                User activeUser = GeneralSettingsDao.getInstance().getActiveUser(getApplicationContext());
                if(activeUser != null)
                {
                    String token = activeUser.getJwtToken();
                    NetworkActions.ResponseStatus validation = NetworkActions.validateToken(token);
                    if(validation == NetworkActions.ResponseStatus.SUCCESS)
                    {
                        NetworkAccessor.getInstance().setCurrentToken(token);
                    }
                    return validation;
                }
                return NetworkActions.ResponseStatus.FAILURE;
            }

            @Override
            protected void onPostExecute(NetworkActions.ResponseStatus tokenValidation)
            {
                if(tokenValidation == NetworkActions.ResponseStatus.SUCCESS)
                {
                    goToHome();
                }
                else if(tokenValidation == NetworkActions.ResponseStatus.FAILURE)
                {
                    goToLogin();
                }
                else if (tokenValidation == NetworkActions.ResponseStatus.NOT_AVAILABLE)
                {
                    findViewById(R.id.progressBar).setVisibility(View.GONE);
                    new AlertDialog.Builder(LauncherActivity.this)
                            .setTitle("Server Unavailable")
                            .setMessage("Unable to reach server. Please check server configuration, or try again later.")
                            .setPositiveButton("Configure Server", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
                                {
                                    goToServerConfig();
                                }
                            })
                            .setNegativeButton("Close App", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int which)
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

    private void goToServerConfig()
    {
        Intent intent = new Intent(this, ServerConfigActivity.class);
        startActivity(intent);
    }

}
