package com.idexx.labstation.rapidcaptureapp.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by mhansen on 4/21/2017.
 */
public abstract class AbstractDbAccessor implements DbAccessor
{
    private final SQLiteOpenHelper dbHelper;

    public AbstractDbAccessor(SQLiteOpenHelper dbHelper)
    {
        this.dbHelper = dbHelper;
    }

    protected abstract Collection<Column> getColumns();
    protected abstract Collection<String> getColumnNames();

    public String getCreateTableCommand()
    {
        return createTableCommand(getTableName(), getColumns());
    }

    public String getDropTableCommand()
    {
        return "DROP IF EXISTS " + getTableName();
    }

    public Map<String, Object> get(int key)
    {
        Cursor cursor = dbHelper.getReadableDatabase().query(
                getTableName(),
                getColumnNames().toArray(new String[0]),
                BaseColumns._ID + " = ?",
                new String[] {"" + key},
                null, null, null
        );
        List<Map<String, Object>> results = cursorToList(cursor);
        if(results.size() > 1)
        {
            throw new RuntimeException("Unique primary key query returned more than one result");
        }
        return results.size() == 1 ? results.get(0) : null;
    }

    public void delete(int key)
    {
        dbHelper.getWritableDatabase().delete(getTableName(), BaseColumns._ID + " = ?", new String[] { "" + key });
    }

    public int update(Map<String, Object> entity)
    {
        ContentValues contentValues = convertToContentValues(entity);
        dbHelper.getWritableDatabase().update(getTableName(), contentValues, BaseColumns._ID + " = ?", new String[] { entity.get(BaseColumns._ID).toString() });
        return (Integer)entity.get(BaseColumns._ID);
    }

    public int insert(Map<String, Object> entity)
    {
        ContentValues cv = convertToContentValues(entity);
        long key = dbHelper.getWritableDatabase().insert(getTableName(), null, cv);
        return (int)key;
    }

    public int insertOrUpdate(Map<String, Object> entity)
    {
        return entity.containsKey(BaseColumns._ID) ? update(entity) : insert(entity);
    }

    protected List<Map<String, Object>> get(Map<Column, Object> filters)
    {
        StringBuilder selectionBuilder = new StringBuilder();
        List<String> selectionArgsList = new ArrayList<>();
        boolean selStarted = false;

        for(Column column : filters.keySet())
        {
            if(selStarted)
            {
                selectionBuilder.append(" AND ");
            }
            Object val = filters.get(column);
            selectionBuilder.append(column.columnName + " = ?");
            if(column.dataType == Column.DBColumnDataType.INTEGER && val instanceof Boolean)
            {
                val = (Boolean)val ? 1 : 0;
            }
            selectionArgsList.add(val.toString());
            selStarted = true;
        }

        String selection = selStarted ? selectionBuilder.toString() : null;
        String[] args = selectionArgsList.toArray(new String[0]);
        Cursor cursor = dbHelper.getReadableDatabase().query(UserSettingsContract.TABLE_NAME, UserSettingsContract.COLUMN_NAMES, selection, args, null, null, null);
        List<Map<String, Object>> values = cursorToList(cursor);
        cursor.close();
        return values;
    }

    protected List<Map<String, Object>> cursorToList(Cursor cursor)
    {
        List<Map<String, Object>> valuesList = new ArrayList<>();
        if(cursor.getCount() > 0)
        {
            while (cursor.moveToNext())
            {
                Map<String, Object> values = new HashMap<>();
                for (Column column : getColumns())
                {
                    values.put(column.columnName, column.getValueFromCursor(cursor));
                }
                valuesList.add(values);
            }
        }
        return valuesList;
    }

    protected ContentValues convertToContentValues(Map<String, Object> entity)
    {
        ContentValues contentValues = new ContentValues();
        for(String columnName : entity.keySet())
        {
            Object val = entity.get(columnName);
            contentValues.put(columnName, val == null ? null : val.toString());
        }
        return contentValues;
    }

    protected void addIfNotNull(String columnName, Object value, Map<Column, Object> map)
    {
        if(value != null)
        {
            map.put(getColumn(columnName), value);
        }
    }

    protected Column getColumn(String columnName)
    {
        for(Column column : getColumns())
        {
            if(column.columnName.equalsIgnoreCase(columnName))
            {
                return column;
            }
        }
        return null;
    }

    private static String createTableCommand(String tableName, Collection<Column> columnCommands)
    {
        StringBuilder sb = new StringBuilder();
        sb.append("CREATE TABLE " + tableName + "(");
        StringBuilder sbIn = new StringBuilder();
        for (Column column : columnCommands)
        {
            sbIn.append(column.compileColumnCommand());
        }
        String columns = sbIn.toString();
        if (columns.trim().endsWith(","))
        {
            columns = columns.substring(0, columns.length() - 1);
        }
        sb.append(columns);
        sb.append(")");
        return sb.toString();
    }
}
