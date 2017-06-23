package com.idexx.labstation.rapidcaptureapp.entity.contract;

import android.provider.BaseColumns;

/**
 * Created by mhansen on 4/21/2017.
 */
public class UserContract implements BaseColumns
{
    public static final String TABLE_NAME = "user_settings";
    public static final String USER_COLUMN = "user";
    public static final String TOKEN_COLUMN = "jwt_token";
    public static final String PERMISSIONS_COLUMN = "permissions";

    public static String[] COLUMN_NAMES = new String[] {
            _ID,
            USER_COLUMN,
            TOKEN_COLUMN,
    };
}
