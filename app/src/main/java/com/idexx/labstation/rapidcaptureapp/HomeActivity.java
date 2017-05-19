package com.idexx.labstation.rapidcaptureapp;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ExpandableListView;
import android.widget.ListView;

import com.idexx.labstation.rapidcaptureapp.adapter.HomeOptionExpandableListAdapter;
import com.idexx.labstation.rapidcaptureapp.adapter.model.HomeOptionItem;
import com.idexx.labstation.rapidcaptureapp.dao.GeneralSettingsDao;
import com.idexx.labstation.rapidcaptureapp.entity.User;
import com.idexx.labstation.rapidcaptureapp.model.StudyDto;
import com.idexx.labstation.rapidcaptureapp.util.UserUtils;
import com.idexx.labstation.rapidcaptureapp.util.network.NetworkActions;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class HomeActivity extends AppCompatActivity
{
    private List<StudyDto> studies;

    private View contentLayout;
    private View loadingLayout;
    private ListView studiesListView;
    private ExpandableListView optionsListView;

    private ArrayAdapter<StudyDto> studiesAdapter;
    private HomeOptionExpandableListAdapter homeOptionExpandableListAdapter;

    private boolean nameLoaded;
    private boolean studiesLoaded;

    private User activeUser;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        bindFields();
        populateLists();
        findActiveUser();
    }

    private boolean handleOptionItem(HomeOptionItem item)
    {
        switch (item)
        {
            case LOGOUT:
                signout();
                return true;
            case USER_SETTINGS:
                goToUserSettings();
                return true;
            case CREATE_STUDY:
                goToCreateStudy();
                return true;
            default:
                return false;
        }
    }

    private void bindFields()
    {
        contentLayout = findViewById(R.id.homeContentLayout);
        loadingLayout = findViewById(R.id.homeLoadingLayout);
        studiesListView = (ListView) findViewById(R.id.homeStudiesListView);
        optionsListView = (ExpandableListView) findViewById(R.id.homeOptionsExpandableListView);

        studiesAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        studiesListView.setAdapter(studiesAdapter);
        studiesListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                StudyDto selected = studiesAdapter.getItem(position);
                goToStudyDetails(selected);
            }
        });
    }

    private void populateOptions()
    {
        homeOptionExpandableListAdapter = new HomeOptionExpandableListAdapter(UserUtils.getHomeItemsForRole(this.activeUser.getRole()), this);
        optionsListView.setAdapter(homeOptionExpandableListAdapter);
        optionsListView.setOnChildClickListener(new ExpandableListView.OnChildClickListener()
        {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id)
            {
                HomeOptionItem item = homeOptionExpandableListAdapter.getChild(groupPosition, childPosition);
                return handleOptionItem(item);
            }
        });
    }

    private void populateLists()
    {
        updateStudies();
    }

    private void updateStudies()
    {
        new AsyncTask<Object, Object, Object>()
        {
            @Override
            protected Object doInBackground(Object... params)
            {
                studies = NetworkActions.getStudies();
                Collections.sort(studies, new Comparator<StudyDto>()
                {
                    @Override
                    public int compare(StudyDto lhs, StudyDto rhs)
                    {
                        return rhs.getCreatedAt().compareTo(lhs.getCreatedAt());
                    }
                });
                return null;
            }

            @Override
            protected void onPostExecute(Object obj)
            {
                studiesAdapter.clear();
                studiesAdapter.addAll(studies);
                studiesAdapter.notifyDataSetChanged();
                studiesLoaded = true;
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
                HomeActivity.this.activeUser = activeUser;
                return activeUser.getUser();
            }

            @Override
            protected void onPostExecute(String s)
            {
                populateOptions();
                homeOptionExpandableListAdapter.updateTitle("Welcome back, " + s);
                nameLoaded = true;
                checkForDone();
            }
        }.execute();
    }

    private void checkForDone()
    {
        if(nameLoaded && studiesLoaded)
        {
            loadingLayout.setVisibility(View.GONE);
            contentLayout.setVisibility(View.VISIBLE);
        }
    }

    private void goToStudyDetails(StudyDto studyDto)
    {
        //TODO
    }

    private void goToCreateStudy()
    {
        Intent intent = new Intent(this, CreateStudyActivity.class);
        startActivity(intent);
    }

    private void goToUserSettings()
    {
        //TODO
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
