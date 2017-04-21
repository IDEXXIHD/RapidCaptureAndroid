package com.idexx.labstation.rapidcaptureapp.db;

import android.database.sqlite.SQLiteOpenHelper;

import java.util.Map;

/**
 * Created by mhansen on 4/21/2017.
 */
public interface DbAccessor
{
    Map<String, Object> get(long key);
    void update(Map<String, Object> entity);
    long create(Map<String, Object> entity);
    String getCreateTableCommand();
    String getDropTableCommand();
    String getTableName();
}
