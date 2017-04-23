package com.idexx.labstation.rapidcaptureapp.model;

import java.util.Date;

/**
 * Created by mhansen on 4/23/2017.
 */
public class ClinicDto extends BaseDto
{
    private String _id;
    private Date updatedAt;
    private Date createdAt;
    private String clinicName;
    private CreatorDto createdBy;

    public String get_id()
    {
        return _id;
    }

    public void set_id(String _id)
    {
        this._id = _id;
    }

    public Date getUpdatedAt()
    {
        return updatedAt;
    }

    public void setUpdatedAt(Date updatedAt)
    {
        this.updatedAt = updatedAt;
    }

    public Date getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }

    public String getClinicName()
    {
        return clinicName;
    }

    public void setClinicName(String clinicName)
    {
        this.clinicName = clinicName;
    }

    public CreatorDto getCreatedBy()
    {
        return createdBy;
    }

    public void setCreatedBy(CreatorDto createdBy)
    {
        this.createdBy = createdBy;
    }

    @Override
    public String toString()
    {
        return this.clinicName;
    }

    public static class CreatorDto extends BaseDto
    {
        private String _id;
        private String username;

        public String get_id()
        {
            return _id;
        }

        public void set_id(String _id)
        {
            this._id = _id;
        }

        public String getUsername()
        {
            return username;
        }

        public void setUsername(String username)
        {
            this.username = username;
        }
    }
}
