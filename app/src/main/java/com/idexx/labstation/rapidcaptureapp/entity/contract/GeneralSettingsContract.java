package com.idexx.labstation.rapidcaptureapp.entity.contract;

/**
 * Created by mhansen on 4/24/2017.
 */
public class GeneralSettingsContract
{
    public static final String TABLE_NAME = "general_settings";
    public static final String ACTIVE_USER_COLUMN = "active_user";
    public static final String SERVER_HOST = "server_host";
    public static final String SERVER_CONTEXT = "server_context";
    public static final String SERVER_PORT = "server_port";

    public static String[] COLUMN_NAMES = new String[] {
            ACTIVE_USER_COLUMN,
            SERVER_HOST,
            SERVER_CONTEXT,
            SERVER_PORT
    };
}
