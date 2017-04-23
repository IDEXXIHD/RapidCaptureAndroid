package com.idexx.labstation.rapidcaptureapp.model;

/**
 * Created by mhansen on 4/23/2017.
 */
public class CreateClinicDto extends BaseDto
{
    private String clinicName;

    public String getClinicName()
    {
        return clinicName;
    }

    public void setClinicName(String clinicName)
    {
        this.clinicName = clinicName;
    }
}
