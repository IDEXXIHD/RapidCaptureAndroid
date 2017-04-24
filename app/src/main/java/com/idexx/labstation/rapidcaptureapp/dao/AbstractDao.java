package com.idexx.labstation.rapidcaptureapp.dao;

import android.content.Context;
import android.util.Log;

import com.idexx.labstation.rapidcaptureapp.entity.DatabaseHelper;
import com.j256.ormlite.dao.Dao;

import java.sql.SQLException;

/**
 * Created by mhansen on 4/24/2017.
 */
public abstract class AbstractDao <T>
{
    protected abstract Class<T> getEntityClass();

    public T createIfNotExists(T entity, Context context)
    {
        try
        {
            return DatabaseHelper.getInstance(context).getDaoIfExists(getEntityClass()).createIfNotExists(entity);
        }
        catch (SQLException e)
        {
            Log.e(getClass().getSimpleName(), "Error creating entity", e);
            return null;
        }
    }

    public int update(T entity, Context context)
    {
        try
        {
            return DatabaseHelper.getInstance(context).getDaoIfExists(getEntityClass()).update(entity);
        }
        catch (SQLException e)
        {
            Log.e(getClass().getSimpleName(), "Error updating entity", e);
            return -1;
        }
    }

    public int delete(T entity, Context context)
    {
        try
        {
            return DatabaseHelper.getInstance(context).getDaoIfExists(getEntityClass()).delete(entity);
        }
        catch (SQLException e)
        {
            Log.e(getClass().getSimpleName(), "Error deleting entity", e);
            return -1;
        }
    }

    public Dao<T, ?> getInnerDao(Context context)
    {
        return DatabaseHelper.getInstance(context).getDaoIfExists(getEntityClass());
    }
}
