package com.idexx.labstation.rapidcaptureapp.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by mhansen on 4/21/2017.
 */
public class DBHelper extends SQLiteOpenHelper
{
    private static volatile DBHelper instance;

    private static final String DB_NAME = "RapidCapture";
    private static final int DB_VERSION = 1;

    private final Map<Class<? extends DbAccessor>, DbAccessor> accessors = new HashMap<>();

    public static void initialize(Context context)
    {
        instance = new DBHelper(context);
    }

    public static DBHelper getInstance()
    {
        return instance;
    }

    public static <T extends DbAccessor> T getDbAccessor(Class<T> clazz)
    {
        return (T)getInstance().accessors.get(clazz);
    }

    private DBHelper(Context context)
    {
        super(context, DB_NAME, null, DB_VERSION);
        accessors.put(UserSettingsDbAccessor.class, new UserSettingsDbAccessor(this));
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        for(DbAccessor dbAccessor : accessors.values())
        {
            db.execSQL(dbAccessor.getCreateTableCommand());
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        for(DbAccessor dbAccessor : accessors.values())
        {
            db.execSQL(dbAccessor.getDropTableCommand());
        }
        onCreate(db);
    }
}
