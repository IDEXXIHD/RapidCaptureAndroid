package com.idexx.labstation.rapidcaptureapp.db;

import android.provider.BaseColumns;

/**
 * Created by mhansen on 4/21/2017.
 */
public class UserSettingsContract implements BaseColumns
{
    public static final String TABLE_NAME = "user_settings";
    public static final String USER_COLUMN = "user";
    public static final String TOKEN_COLUMN = "jwt_token";
}