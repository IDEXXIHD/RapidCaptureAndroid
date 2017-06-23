package com.idexx.labstation.rapidcaptureapp.util;

import com.idexx.labstation.rapidcaptureapp.adapter.model.HomeOptionItem;
import com.idexx.labstation.rapidcaptureapp.entity.User;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mhansen on 5/19/2017.
 */
public class UserUtils
{
    public static boolean userCan(User user, String requiredRole)
    {
        for(String perm : user.getPermissions())
        {
            if(requiredRole.equalsIgnoreCase("default") || perm.equalsIgnoreCase(requiredRole))
            {
                return true;
            }
        }
        return false;
    }

    public static HomeOptionItem[] getItemsForUser(User user)
    {
        List<HomeOptionItem> list = new ArrayList<>();
        for(HomeOptionItem item : HomeOptionItem.values())
        {
            if(UserUtils.userCan(user, item.requiredPermission))
            {
                list.add(item);
            }
        }
        return list.toArray(new HomeOptionItem[0]);
    }
}
