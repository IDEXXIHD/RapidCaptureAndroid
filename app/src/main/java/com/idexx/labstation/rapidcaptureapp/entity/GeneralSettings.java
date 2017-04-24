package com.idexx.labstation.rapidcaptureapp.entity;

import com.idexx.labstation.rapidcaptureapp.entity.contract.GeneralSettingsContract;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by mhansen on 4/24/2017.
 */
@DatabaseTable(tableName = GeneralSettingsContract.TABLE_NAME)
public class GeneralSettings extends AbstractEntity
{
    @DatabaseField(columnName = GeneralSettingsContract.ACTIVE_USER_COLUMN, foreign = true, foreignAutoRefresh = true)
    private User activeUser;

    public User getActiveUser()
    {
        return activeUser;
    }

    public void setActiveUser(User activeUser)
    {
        this.activeUser = activeUser;
    }
}
