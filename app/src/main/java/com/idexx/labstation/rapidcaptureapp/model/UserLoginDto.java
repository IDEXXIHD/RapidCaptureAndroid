package com.idexx.labstation.rapidcaptureapp.model;

/**
 * Created by mhansen on 4/21/2017.
 */
public class UserLoginDto extends BaseDto
{
    private String username;
    private String password;

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String username)
    {
        this.username = username;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
