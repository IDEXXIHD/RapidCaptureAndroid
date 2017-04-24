package com.idexx.labstation.rapidcaptureapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.idexx.labstation.rapidcaptureapp.dao.GeneralSettingsDao;
import com.idexx.labstation.rapidcaptureapp.dao.UserDao;
import com.idexx.labstation.rapidcaptureapp.entity.GeneralSettings;
import com.idexx.labstation.rapidcaptureapp.entity.User;
import com.idexx.labstation.rapidcaptureapp.model.ClinicDto;
import com.idexx.labstation.rapidcaptureapp.util.network.NetworkActions;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeActivity extends AppCompatActivity
{
    private List<ClinicDto> clinics;

    private View contentLayout;
    private View loadingLayout;
    private TextView welcomeLabel;
    private ListView clinicsListView;

    private ArrayAdapter<ClinicDto> clinicsAdapter;

    private boolean nameLoaded;
    private boolean clinicsLoaded;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bindFields();
        populateLists();
        findActiveUser();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.home_action_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.homeMenuSignoutOption:
                signout();
                return true;
            case R.id.homeMenuCreateClinic:
                goToCreateClinic();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void bindFields()
    {
        contentLayout = findViewById(R.id.homeContentLayout);
        loadingLayout = findViewById(R.id.homeLoadingLayout);
        welcomeLabel = (TextView) findViewById(R.id.homeWelcomeTextView);
        clinicsListView = (ListView) findViewById(R.id.homeClinicsListView);

        clinicsAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        clinicsListView.setAdapter(clinicsAdapter);
        clinicsListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                ClinicDto selected = clinicsAdapter.getItem(position);
                goToClinicDetails(selected);
            }
        });
    }

    private void populateLists()
    {
        updateClinics();
    }

    private void updateClinics()
    {
        new AsyncTask<Object, Object, Object>()
        {
            @Override
            protected Object doInBackground(Object... params)
            {
                clinics = NetworkActions.getClinics();
                Collections.sort(clinics, new Comparator<ClinicDto>()
                {
                    @Override
                    public int compare(ClinicDto lhs, ClinicDto rhs)
                    {
                        return rhs.getCreatedAt().compareTo(lhs.getCreatedAt());
                    }
                });
                return null;
            }

            @Override
            protected void onPostExecute(Object obj)
            {
                clinicsAdapter.clear();
                clinicsAdapter.addAll(clinics);
                clinicsAdapter.notifyDataSetChanged();
                clinicsLoaded = true;
                checkForDone();
            }
        }.execute();
    }

    private void findActiveUser()
    {
        new AsyncTask<Object, Object, String>()
        {
            @Override
            protected String doInBackground(Object... params)
            {
                User activeUser = GeneralSettingsDao.getInstance().getActiveUser(getApplicationContext());
                return activeUser.getUser();
            }

            @Override
            protected void onPostExecute(String s)
            {
                welcomeLabel.setText("Welcome Back, " + s);
                nameLoaded = true;
                checkForDone();
            }
        }.execute();
    }

    private void checkForDone()
    {
        if(nameLoaded && clinicsLoaded)
        {
            loadingLayout.setVisibility(View.GONE);
            contentLayout.setVisibility(View.VISIBLE);
        }
    }

    private void goToClinicDetails(ClinicDto clinicDto)
    {
        Intent intent = new Intent(this, ClinicDetailsActivity.class);
        intent.putExtra(ClinicDetailsActivity.CLINIC_EXTRA, clinicDto);
        startActivity(intent);
    }

    private void goToCreateClinic()
    {
        Intent intent = new Intent(this, CreateClinicActivity.class);
        startActivity(intent);
    }

    private void signout()
    {
        GeneralSettingsDao.getInstance().clearActiveUser(getApplicationContext());
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }
}
