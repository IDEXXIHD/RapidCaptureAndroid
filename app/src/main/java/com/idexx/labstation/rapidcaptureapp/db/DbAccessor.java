package com.idexx.labstation.rapidcaptureapp.db;

import android.database.sqlite.SQLiteOpenHelper;

import java.util.Map;

/**
 * Created by mhansen on 4/21/2017.
 */
public interface DbAccessor
{
    Map<String, Object> get(int key);
    void delete(int key);
    int update(Map<String, Object> entity);
    int insert(Map<String, Object> entity);
    int insertOrUpdate(Map<String, Object> entity);
    String getCreateTableCommand();
    String getDropTableCommand();
    String getTableName();
}
