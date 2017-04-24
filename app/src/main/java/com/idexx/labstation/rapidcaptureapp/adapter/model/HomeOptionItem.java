package com.idexx.labstation.rapidcaptureapp.adapter.model;

/**
 * Created by mhansen on 4/24/2017.
 */
public enum HomeOptionItem
{
    CREATE_CLINIC ("New Clinic"),
    LOGOUT ("Logout");

    public final String label;

    HomeOptionItem(String label)
    {
        this.label = label;
    }
}
