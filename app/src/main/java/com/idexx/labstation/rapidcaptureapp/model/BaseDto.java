package com.idexx.labstation.rapidcaptureapp.model;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by mhansen on 4/23/2017.
 */
public abstract class BaseDto implements Serializable
{
    private Map<String, Object> miscFields = new HashMap<>();

    public Map<String, Object> getMiscFields()
    {
        return miscFields;
    }

    public void setMiscFields(Map<String, Object> miscFields)
    {
        this.miscFields = miscFields;
    }

    @JsonAnySetter
    protected void handleUnknown(String key, Object value)
    {
        miscFields.put(key, value);
    }
    @JsonAnyGetter
    public Map<String, Object> getUnknownPropMap()
    {
        return miscFields;
    }
}
