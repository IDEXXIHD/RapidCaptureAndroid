package com.idexx.labstation.rapidcaptureapp.util.network;
import android.util.Log;

import com.idexx.labstation.rapidcaptureapp.model.NewUserDto;
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
    private static final String AUTH_HEADER = "Authorization";
    private static final String JWT = "JWT ";

    public static Map login(UserLoginDto userLoginDto)
    {
        try
        {
            return NetworkAccessor.getInstance().getWebTarget()
                    .path(NetworkPaths.AUTH_PATH)
                    .request()
                    .post(Entity.entity(userLoginDto, MediaType.APPLICATION_JSON_TYPE))
                    .readEntity(Map.class);
        }
        catch (Exception e)
        {
            Log.e(NetworkActions.class.getSimpleName(), "Error logging in", e);
            return null;
        }
    }

    public static ResponseStatus createUser(NewUserDto user)
    {
        try
        {
            Response resp = NetworkAccessor.getInstance().getWebTarget()
                    .path(NetworkPaths.NEW_USER_PATH)
                    .request()
                    .post(Entity.entity(user, MediaType.APPLICATION_JSON_TYPE));
            return resp.getStatus() == Response.Status.OK.getStatusCode() ? ResponseStatus.SUCCESS : ResponseStatus.FAILURE;
        }
        catch (Exception e)
        {
            Log.e(NetworkActions.class.getSimpleName(), "Error creating new user", e);
            return ResponseStatus.NOT_AVAILABLE;
        }
    }

    public static ResponseStatus validateToken(String token)
    {
        //There should be a token validation endpoint on the RapidCapture webapp
        try
        {
            Response resp = NetworkAccessor.getInstance().getWebTarget()
                    .path(NetworkPaths.CLINICS_PATH)
                    .request()
                    .header(AUTH_HEADER, JWT + token)
                    .get();
            return resp.getStatus() == Response.Status.OK.getStatusCode() ? ResponseStatus.SUCCESS : ResponseStatus.FAILURE;
        }
        catch (Exception e)
        {
            Log.e(NetworkActions.class.getSimpleName(), "Error validating token", e);
            return ResponseStatus.NOT_AVAILABLE;
        }
    }

    public enum ResponseStatus
    {
        SUCCESS,
        FAILURE,
        NOT_AVAILABLE
    }
}
