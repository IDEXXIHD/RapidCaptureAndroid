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

    public void setUserActive(User user, Context context)
    {
        clearActiveUser(context);
        user.setActive(true);
        update(user, context);
    }

    public void clearActiveUser(Context context)
    {
        User oldActive = getActiveUser(context);
        if(oldActive != null)
        {
            oldActive.setActive(false);
            update(oldActive, context);
        }
    }

    public List<User> searchByUsername(String username, Context context)
    {
        try
        {
            return getInnerDao(context).queryForEq(UserContract.USER_COLUMN, username);
        }
        catch (SQLException e)
        {
            Log.e(getClass().getSimpleName(), "Error searching by username " + username, e);
            return null;
        }
    }

    public User getActiveUser(Context context)
    {
        try
        {
            List<User> userList = getInnerDao(context).queryForEq(UserContract.ACTIVE_COLUMN, Boolean.TRUE);
            if(userList != null && userList.size() == 1)
            {
                return userList.get(0);
            }
            else if (userList != null && userList.size() > 1)
            {
                throw new RuntimeException("Database corrupt - more than one active user specified");
            }
            else
            {
                return null;
            }
        }
        catch (SQLException e)
        {
            Log.e(UserDao.class.getSimpleName(), "Error getting active user", e);
            return null;
        }
    }

    @Override
    protected Class<User> getEntityClass()
    {
        return User.class;
    }
}
