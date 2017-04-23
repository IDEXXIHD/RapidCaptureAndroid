package com.idexx.labstation.rapidcaptureapp.db;

import android.provider.BaseColumns;

import java.util.Arrays;
import java.util.List;

/**
 * Created by mhansen on 4/21/2017.
 */
public class UserSettingsContract implements BaseColumns
{
    public static final String TABLE_NAME = "user_settings";
    public static final String USER_COLUMN = "user";
    public static final String TOKEN_COLUMN = "jwt_token";
    public static final String ACTIVE_COLUMN = "active";

    public static List<Column> COLUMNS = Arrays.asList(
            new Column(_ID, Column.DBColumnDataType.INTEGER, Column.DBColumnOption.PRIMARY_KEY),
            new Column(USER_COLUMN, Column.DBColumnDataType.TEXT, Column.DBColumnOption.UNIQUE),
            new Column(TOKEN_COLUMN, Column.DBColumnDataType.TEXT),
            new Column(ACTIVE_COLUMN, Column.DBColumnDataType.INTEGER)
    );

    public static String[] COLUMN_NAMES = new String[] {
            _ID,
            USER_COLUMN,
            TOKEN_COLUMN,
            ACTIVE_COLUMN
    };
}
