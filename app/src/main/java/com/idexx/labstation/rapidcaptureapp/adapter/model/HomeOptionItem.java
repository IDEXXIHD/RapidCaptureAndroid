package com.idexx.labstation.rapidcaptureapp.adapter.model;

/**
 * Created by mhansen on 4/24/2017.
 */
public enum HomeOptionItem
{
    CREATE_STUDY ("New Study", "admin"),
    USER_SETTINGS("User Settings", "user"),
    LOGOUT ("Logout", "user");

    public final String label;
    public final String requiredRole;

    HomeOptionItem(String label, String requiredRole)
    {
        this.label = label;
        this.requiredRole = requiredRole;
    }
}
