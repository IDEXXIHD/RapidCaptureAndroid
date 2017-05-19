package com.idexx.labstation.rapidcaptureapp.model;

/**
 * Created by mhansen on 4/23/2017.
 */
public class LoginResponseDto extends BaseDto
{
    private String token;
    private String role;

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
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
