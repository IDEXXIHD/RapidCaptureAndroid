package com.idexx.labstation.rapidcaptureapp.model;

import java.util.Date;

/**
 * Created by mhansen on 5/19/2017.
 */
public class StudyDto extends BaseDto
{
    private Date createdAt;

    public Date getCreatedAt()
    {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt)
    {
        this.createdAt = createdAt;
    }
}
