package com.idexx.labstation.rapidcaptureapp.db;

import android.database.Cursor;

/**
 * Created by mhansen on 4/21/2017.
 */
public class Column
{
    public final String columnName;
    public final DBColumnDataType dataType;
    public final DBColumnOption[] dbColumnOptions;

    public Column(String columnName, DBColumnDataType dataType, DBColumnOption... dbColumnOptions)
    {
        this.columnName = columnName;
        this.dataType = dataType;
        this.dbColumnOptions = dbColumnOptions;
    }

    public String compileColumnCommand()
    {
        StringBuilder sb = new StringBuilder();
        sb.append(columnName);
        sb.append(" " + dataType);
        if (dbColumnOptions != null && dbColumnOptions.length > 0)
        {
            for (DBColumnOption dbColumnOption : dbColumnOptions)
            {
                sb.append(" " + dbColumnOption.text);
            }
        }
        sb.append(",");
        return sb.toString();
    }

    public Object getValueFromCursor(Cursor cursor)
    {
        switch (dataType)
        {
            case INTEGER:
                return cursor.getInt(cursor.getColumnIndexOrThrow(columnName));
            case TEXT:
                return cursor.getString(cursor.getColumnIndexOrThrow(columnName));
        }
        return null;
    }

    public enum DBColumnDataType
    {
        INTEGER,
        TEXT
    }

    public enum DBColumnOption
    {
        PRIMARY_KEY("PRIMARY KEY");

        public final String text;

        DBColumnOption(String text)
        {
            this.text = text;
        }
    }
}
