package com.idexx.labstation.rapidcaptureapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.idexx.labstation.rapidcaptureapp.dao.GeneralSettingsDao;
import com.idexx.labstation.rapidcaptureapp.entity.GeneralSettings;
import com.idexx.labstation.rapidcaptureapp.util.TextChangedWatcher;
import com.idexx.labstation.rapidcaptureapp.util.network.NetworkAccessor;

public class ServerConfigActivity extends AppCompatActivity
{
    private EditText hostInput;
    private EditText contextInput;
    private EditText portInput;

    private Button saveButton;
    private ProgressBar loadingSpinner;
    private View serverConfigWrapperLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_server_config);

        bindFields();
        populateFields();
    }

    private void bindFields()
    {
        this.hostInput = (EditText) findViewById(R.id.serverConfigServerHostInput);
        this.contextInput = (EditText) findViewById(R.id.serverConfigServerContextInput);
        this.portInput = (EditText) findViewById(R.id.serverConfigServerPortInput);
        this.saveButton = (Button) findViewById(R.id.serverConfigSaveButton);
        this.loadingSpinner = (ProgressBar) findViewById(R.id.serverConfigLoadingSpinner);
        this.serverConfigWrapperLayout = findViewById(R.id.serverConfigWrapperLayout);

        TextChangedWatcher watcher = new TextChangedWatcher()
        {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count)
            {
                checkSaveButton();
            }
        };

        this.hostInput.addTextChangedListener(watcher);
        this.contextInput.addTextChangedListener(watcher);
        this.portInput.addTextChangedListener(watcher);

        this.saveButton.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                save();
            }
        });
    }

    private void populateFields()
    {
        new AsyncTask<Object, Object, GeneralSettings>()
        {
            @Override
            protected GeneralSettings doInBackground(Object... params)
            {
                return GeneralSettingsDao.getInstance().getSettings(getApplicationContext());
            }

            @Override
            protected void onPostExecute(GeneralSettings generalSettings)
            {
                hostInput.setText(generalSettings.getServerHost());
                contextInput.setText(generalSettings.getServerContext());
                portInput.setText(generalSettings.getServerPort() == null ? null : String.valueOf(generalSettings.getServerPort()));
                checkSaveButton();
                loadingSpinner.setVisibility(View.GONE);
                serverConfigWrapperLayout.setVisibility(View.VISIBLE);
            }
        }.execute();
    }

    private void checkSaveButton()
    {
        this.saveButton.setEnabled(
                this.hostInput.getText().toString().trim().length() > 0
                //&& this.contextInput.getText().toString().trim().length() > 0
                && this.portInput.getText().toString().trim().length() > 0
        );
    }

    private void save()
    {
        new AsyncTask<String, Object, Object>()
        {
            @Override
            protected Object doInBackground(String... params)
            {
                String host = params[0];
                String context = params[1];
                Integer port = Integer.valueOf(params[2]);

                GeneralSettings settings = GeneralSettingsDao.getInstance().getSettings(getApplicationContext());
                settings.setServerHost(host);
                settings.setServerContext(context);
                settings.setServerPort(port);
                GeneralSettingsDao.getInstance().update(settings, getApplicationContext());

                NetworkAccessor.getInstance().initialize(getApplicationContext());
                return null;
            }

            @Override
            protected void onPostExecute(Object o)
            {
                Toast.makeText(ServerConfigActivity.this, "Settings updated successfully", Toast.LENGTH_SHORT).show();
                finish();
            }
        }.execute(hostInput.getText().toString(), contextInput.getText().toString(), portInput.getText().toString());
    }
}
