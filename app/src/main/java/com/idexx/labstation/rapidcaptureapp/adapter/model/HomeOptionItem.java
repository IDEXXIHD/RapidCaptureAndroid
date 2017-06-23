package com.idexx.labstation.rapidcaptureapp.adapter.model;

/**
 * Created by mhansen on 4/24/2017.
 */
public enum HomeOptionItem
{
    CREATE_STUDY ("New Study", "create studies"),
    USER_SETTINGS("User Settings", "default"),
    LOGOUT ("Logout", "default");

    public final String label;
    public final String requiredPermission;

    HomeOptionItem(String label, String requiredPermission)
    {
        this.label = label;
        this.requiredPermission = requiredPermission;
    }
}
