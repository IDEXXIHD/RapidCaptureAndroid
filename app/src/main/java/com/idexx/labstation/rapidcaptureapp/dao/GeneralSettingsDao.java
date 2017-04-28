package com.idexx.labstation.rapidcaptureapp.dao;

import android.content.Context;
import android.util.Log;

import com.idexx.labstation.rapidcaptureapp.entity.GeneralSettings;
import com.idexx.labstation.rapidcaptureapp.entity.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by mhansen on 4/24/2017.
 */
public class GeneralSettingsDao extends AbstractDao<GeneralSettings>
{
    private static final GeneralSettingsDao instance = new GeneralSettingsDao();

    public static GeneralSettingsDao getInstance()
    {
        return instance;
    }

    private GeneralSettingsDao() {}

    public GeneralSettings getSettings(Context context)
    {
        try
        {
            List<GeneralSettings> allRows = getInnerDao(context).queryForAll();
            if(allRows != null && allRows.size() == 1)
            {
                return allRows.get(0);
            }
            else if (allRows != null && allRows.size() != 1)
            {
                throw new RuntimeException("Database corrupt - There should be exactly one row of settings, but there are " + allRows.size());
            }
            else
            {
                return null;
            }
        }
        catch (SQLException e)
        {
            Log.e(getClass().getSimpleName(), "Error getting settings", e);
            return null;
        }
    }

    public User getActiveUser(Context context)
    {
        GeneralSettings settings = getSettings(context);
        return settings.getActiveUser();
    }

    public void clearActiveUser(Context context)
    {
        GeneralSettings settings = getSettings(context);
        settings.setActiveUser(null);
        update(settings, context);
    }

    public void setUserActive(User user, Context context)
    {
        GeneralSettings settings = getSettings(context);
        settings.setActiveUser(user);
        update(settings, context);
    }

    public String getServerHost(Context context)
    {
        return getSettings(context).getServerHost();
    }

    public String getServerContext(Context context)
    {
        return getSettings(context).getServerContext();
    }

    public Integer getServerPort(Context context)
    {
        return getSettings(context).getServerPort();
    }

    @Override
    public GeneralSettings createIfNotExists(GeneralSettings entity, Context context)
    {
        throw new RuntimeException("Cannot create duplicate instance of GeneralSettings");
    }

    @Override
    public int delete(GeneralSettings entity, Context context)
    {
        throw new RuntimeException("Cannot delete single instance of GeneralSettings");
    }

    @Override
    protected Class<GeneralSettings> getEntityClass()
    {
        return GeneralSettings.class;
    }
}
