package com.idexx.labstation.rapidcaptureapp.db;

import android.database.sqlite.SQLiteOpenHelper;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mhansen on 4/21/2017.
 */
public class UserSettingsDbAccessor extends AbstractDbAccessor
{
    public UserSettingsDbAccessor(SQLiteOpenHelper dbHelper)
    {
        super(dbHelper);
    }

    public void setUserActive(long key)
    {
        clearActiveUsers();
        Map<String, Object> user = get(key);
        if(user != null)
        {
            user.put(UserSettingsContract.ACTIVE_COLUMN, 1);
            update(user);
        }
    }

    public void clearActiveUsers()
    {
        List<Map<String, Object>> activeUsers = getActiveUsers();
        if(activeUsers.size() > 0)
        {
            for(Map<String, Object> user : activeUsers)
            {
                user.put(UserSettingsContract.ACTIVE_COLUMN, 0);
                user.put(UserSettingsContract.TOKEN_COLUMN, null);
                update(user);
            }
        }
    }

    public List<Map<String, Object>> getActiveUsers()
    {
        return getUsers(null, null, Boolean.TRUE);
    }

    @Override
    public String getTableName()
    {
        return UserSettingsContract.TABLE_NAME;
    }

    @Override
    protected Collection<Column> getColumns()
    {
        return UserSettingsContract.COLUMNS;
    }

    @Override
    protected Collection<String> getColumnNames()
    {
        return Arrays.asList(UserSettingsContract.COLUMN_NAMES);
    }

    private List<Map<String, Object>> getUsers(String userName, String jwtToken, Boolean active)
    {
        Map<Column, Object> filters = new HashMap<>();
        addIfNotNull(UserSettingsContract.USER_COLUMN, userName, filters);
        addIfNotNull(UserSettingsContract.TOKEN_COLUMN, jwtToken, filters);
        addIfNotNull(UserSettingsContract.ACTIVE_COLUMN, active, filters);
        return get(filters);
    }


}
