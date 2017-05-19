package com.idexx.labstation.rapidcaptureapp.util;

import com.idexx.labstation.rapidcaptureapp.adapter.model.HomeOptionItem;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mhansen on 5/19/2017.
 */
public class UserUtils
{
    public static boolean userCan(String userRole, String requiredRole)
    {
        return mapRoleToInt(userRole) <= mapRoleToInt(requiredRole);
    }

    public static HomeOptionItem[] getHomeItemsForRole(String userRole)
    {
        List<HomeOptionItem> list = new ArrayList<>();
        for(HomeOptionItem item : HomeOptionItem.values())
        {
            if(UserUtils.userCan(userRole, item.requiredRole))
            {
                list.add(item);
            }
        }
        return list.toArray(new HomeOptionItem[0]);
    }

    private static int mapRoleToInt(String role)
    {
        switch (role.toLowerCase())
        {
            case "root":
                return 0;
            case "admin":
                return 1;
            case "user":
                return 2;
            default:
                return Integer.MAX_VALUE;
        }
    }
}
