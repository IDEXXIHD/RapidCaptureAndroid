package com.idexx.labstation.rapidcaptureapp.entity;

import android.provider.BaseColumns;

import com.j256.ormlite.field.DatabaseField;

/**
 * Created by mhansen on 4/24/2017.
 */
public class AbstractEntity implements BaseColumns
{
    @DatabaseField(generatedId = true, columnName = _ID)
    private int id;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }
}
