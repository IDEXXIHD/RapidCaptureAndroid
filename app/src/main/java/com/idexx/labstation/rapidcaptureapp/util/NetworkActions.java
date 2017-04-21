package com.idexx.labstation.rapidcaptureapp.util;

import com.idexx.labstation.rapidcaptureapp.model.UserLoginDto;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

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

    public static boolean validateToken(String token)
    {
        //There should be a token validation endpoint on the RapidCapture webapp
        Response resp = NetworkAccessor.getInstance().getWebTarget()
                            .path("api/clinics")
                            .request()
                            .header("Authorization", "JWT " + token)
                            .get();
        String respStr = resp.readEntity(String.class);
        return !respStr.equalsIgnoreCase("unauthorized");
    }
}
