package com.idexx.labstation.rapidcaptureapp.entity;

import com.idexx.labstation.rapidcaptureapp.entity.contract.UserContract;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by mhansen on 4/23/2017.
 */
@DatabaseTable(tableName = UserContract.TABLE_NAME)
public class User extends AbstractEntity
{
    @DatabaseField(columnName = UserContract.USER_COLUMN, unique = true)
    private String user;
    @DatabaseField(columnName = UserContract.TOKEN_COLUMN)
    private String jwtToken;
    @DatabaseField(columnName = UserContract.ROLE_COLUMN)
    private String role;

    public String getUser()
    {
        return user;
    }

    public void setUser(String user)
    {
        this.user = user;
    }

    public String getJwtToken()
    {
        return jwtToken;
    }

    public void setJwtToken(String jwtToken)
    {
        this.jwtToken = jwtToken;
    }

    public String getRole()
    {
        return role;
    }

    public void setRole(String role)
    {
        this.role = role;
    }
}
