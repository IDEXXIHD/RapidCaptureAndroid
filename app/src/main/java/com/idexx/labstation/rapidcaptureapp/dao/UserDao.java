package com.idexx.labstation.rapidcaptureapp.dao;

import android.content.Context;
import android.util.Log;

import com.idexx.labstation.rapidcaptureapp.entity.contract.UserContract;
import com.idexx.labstation.rapidcaptureapp.entity.User;

import java.sql.SQLException;
import java.util.List;

/**
 * Created by mhansen on 4/24/2017.
 */
public class UserDao extends AbstractDao<User>
{
    private static final UserDao instance = new UserDao();

    private UserDao() {}

    public static UserDao getInstance()
    {
        return instance;
    }

    public User getByUsername(String username, Context context)
    {
        try
        {
            List<User> userList = getInnerDao(context).queryForEq(UserContract.USER_COLUMN, username);
            if(userList == null || userList.size() == 0)
            {
                return null;
            }
            if(userList.size() == 1)
            {
                return userList.get(0);
            }
            else
            {
                throw new RuntimeException("Corrupt database, multiple users found for user " + username);
            }
        }
        catch (SQLException e)
        {
            Log.e(getClass().getSimpleName(), "Error searching by username " + username, e);
            return null;
        }
    }

    @Override
    protected Class<User> getEntityClass()
    {
        return User.class;
    }
}
