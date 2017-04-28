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
    @DatabaseField(columnName = GeneralSettingsContract.SERVER_HOST)
    private String serverHost;
    @DatabaseField(columnName = GeneralSettingsContract.SERVER_CONTEXT)
    private String serverContext;
    @DatabaseField(columnName = GeneralSettingsContract.SERVER_PORT)
    private Integer serverPort;

    public User getActiveUser()
    {
        return activeUser;
    }

    public void setActiveUser(User activeUser)
    {
        this.activeUser = activeUser;
    }

    public String getServerHost()
    {
        return serverHost;
    }

    public void setServerHost(String serverHost)
    {
        this.serverHost = serverHost;
    }

    public Integer getServerPort()
    {
        return serverPort;
    }

    public void setServerPort(Integer serverPort)
    {
        this.serverPort = serverPort;
    }

    public String getServerContext()
    {
        return serverContext;
    }

    public void setServerContext(String serverContext)
    {
        this.serverContext = serverContext;
    }
}
