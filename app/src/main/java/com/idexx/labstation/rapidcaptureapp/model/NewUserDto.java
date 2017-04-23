package com.idexx.labstation.rapidcaptureapp.model;

/**
 * Created by mhansen on 4/23/2017.
 */
public class NewUserDto extends BaseDto
{
    private String username;
    private String password;
    private String email;
    private UserProfileDto profile;

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

    public String getEmail()
    {
        return email;
    }

    public void setEmail(String email)
    {
        this.email = email;
    }

    public UserProfileDto getProfile()
    {
        return profile;
    }

    public void setProfile(UserProfileDto profile)
    {
        this.profile = profile;
    }

    public static class UserProfileDto
    {
        private String givenName;
        private String familyName;

        public String getGivenName()
        {
            return givenName;
        }

        public void setGivenName(String givenName)
        {
            this.givenName = givenName;
        }

        public String getFamilyName()
        {
            return familyName;
        }

        public void setFamilyName(String familyName)
        {
            this.familyName = familyName;
        }
    }
}
