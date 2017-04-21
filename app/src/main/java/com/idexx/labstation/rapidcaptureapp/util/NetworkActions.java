package com.idexx.labstation.rapidcaptureapp.util;

import com.idexx.labstation.rapidcaptureapp.model.UserLoginDto;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

/**
 * Created by mhansen on 4/21/2017.
 */
public class NetworkActions
{
    public static Map<String, Object> login(UserLoginDto userLoginDto)
    {
        return NetworkAccessor.getInstance().getWebTarget()
                .path("auth")
                .request()
                .post(Entity.entity(userLoginDto, MediaType.APPLICATION_JSON_TYPE))
                .readEntity(Map.class);
    }
}
