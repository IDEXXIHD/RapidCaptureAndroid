package com.idexx.labstation.rapidcaptureapp.util.network;
import android.util.Log;

import com.idexx.labstation.rapidcaptureapp.model.ClinicDto;
import com.idexx.labstation.rapidcaptureapp.model.CreateClinicDto;
import com.idexx.labstation.rapidcaptureapp.model.LoginResponseDto;
import com.idexx.labstation.rapidcaptureapp.model.NewUserDto;
import com.idexx.labstation.rapidcaptureapp.model.StudyDto;
import com.idexx.labstation.rapidcaptureapp.model.UserLoginDto;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by mhansen on 4/21/2017.
 */
public class NetworkActions
{
    private static final String AUTH_HEADER = "Authorization";
    private static final String JWT = "JWT ";

    public static LoginResponseDto login(UserLoginDto userLoginDto)
    {
        try
        {
            Response resp = NetworkAccessor.getInstance().getWebTarget()
                    .path(NetworkPaths.AUTH_PATH)
                    .request()
                    .post(Entity.entity(userLoginDto, MediaType.APPLICATION_JSON_TYPE));
            if(resp.getStatus() == Response.Status.OK.getStatusCode())
            {
                return resp.readEntity(LoginResponseDto.class);
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            Log.e(NetworkActions.class.getSimpleName(), "Error logging in", e);
            return null;
        }
    }

    public static ClinicDto createClinic(CreateClinicDto createClinicDto)
    {
        try
        {
            Response resp = NetworkAccessor.getInstance().getWebTarget()
                    .path(NetworkPaths.CLINICS_PATH)
                    .request()
                    .header(AUTH_HEADER, JWT + NetworkAccessor.getInstance().getCurrentToken())
                    .post(Entity.entity(createClinicDto, MediaType.APPLICATION_JSON_TYPE));
            if(resp.getStatus() == Response.Status.OK.getStatusCode())
            {
                return resp.readEntity(ClinicDto.class);
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            Log.e(NetworkActions.class.getSimpleName(), "Error creating clinic", e);
            return null;
        }
    }

    public static List<StudyDto> getStudies()
    {
        return Collections.emptyList();
    }

    public static List<ClinicDto> getClinics()
    {
        try
        {
            Response resp = NetworkAccessor.getInstance().getWebTarget()
                    .path(NetworkPaths.CLINICS_PATH)
                    .request()
                    .header(AUTH_HEADER, JWT + NetworkAccessor.getInstance().getCurrentToken())
                    .get();
            if(resp.getStatus() == Response.Status.OK.getStatusCode())
            {
                return NetworkAccessor.getMapper().readValue(resp.readEntity(String.class), NetworkAccessor.getMapper().getTypeFactory().constructCollectionType(List.class, ClinicDto.class));
            }
            else
            {
                return null;
            }
        }
        catch (Exception e)
        {
            Log.e(NetworkActions.class.getSimpleName(), "Error getting clinics", e);
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
