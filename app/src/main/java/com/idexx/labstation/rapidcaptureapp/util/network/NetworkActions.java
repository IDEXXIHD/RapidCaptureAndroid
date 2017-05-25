package com.idexx.labstation.rapidcaptureapp.util.network;
import android.util.Log;

import com.fasterxml.jackson.databind.JavaType;
import com.idexx.labstation.rapidcaptureapp.model.ClinicDto;
import com.idexx.labstation.rapidcaptureapp.model.CreateClinicDto;
import com.idexx.labstation.rapidcaptureapp.model.LoginResponseDto;
import com.idexx.labstation.rapidcaptureapp.model.UpdateUserDto;
import com.idexx.labstation.rapidcaptureapp.model.StudyDto;
import com.idexx.labstation.rapidcaptureapp.model.UserDetailsDto;
import com.idexx.labstation.rapidcaptureapp.model.UserLoginDto;

import java.util.Collections;
import java.util.List;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by mhansen on 4/21/2017.
 */
public class NetworkActions
{
    private static final String AUTH_HEADER = "Authorization";
    private static final String JWT = "JWT ";

    /**Auth actions**/
    public static LoginResponseDto login(UserLoginDto userLoginDto)
    {
        return postForEntity(NetworkPaths.AUTH_PATH, userLoginDto, LoginResponseDto.class, null);
    }

    public static ResponseStatus validateToken(String token)
    {
        //There should probably be a token validation endpoint on the RapidCapture webapp
        return getForResponseStatus(NetworkPaths.CLINICS_PATH, token);
    }

    /**User management actions**/

    public static UserDetailsDto getUserDetails()
    {
        return getForType(NetworkPaths.USER_DETAILS_PATH, UserDetailsDto.class, NetworkAccessor.getInstance().getCurrentToken());
    }

    public static ResponseStatus updateUser(UpdateUserDto updateUserDto)
    {
        return putForResponseStatus(NetworkPaths.UPDATE_USER_PATH, updateUserDto, NetworkAccessor.getInstance().getCurrentToken());
    }

    /**API Actions**/

    public static List<StudyDto> getStudies()
    {
        return Collections.emptyList();
    }

    public static List<ClinicDto> getClinics()
    {
        return getForType(NetworkPaths.CLINICS_PATH, NetworkAccessor.getMapper().getTypeFactory().constructCollectionType(List.class, ClinicDto.class), NetworkAccessor.getInstance().getCurrentToken());
    }

    /**Admin actions**/

    public static ClinicDto createClinic(CreateClinicDto createClinicDto)
    {
        return postForEntity(NetworkPaths.CLINICS_PATH, createClinicDto, ClinicDto.class, NetworkAccessor.getInstance().getCurrentToken());
    }

    public static ResponseStatus createUser(UpdateUserDto user)
    {
        return postForResponseStatus(NetworkPaths.NEW_USER_PATH, user, NetworkAccessor.getInstance().getCurrentToken());
    }

    /**Helpers**/

    private static <T> T getForType(String path, Class<T> expectedResponseClass, String token)
    {
        try
        {
            Invocation.Builder builder = NetworkAccessor.getInstance().getWebTarget()
                    .path(path)
                    .request();
            builder = token != null ? builder.header(AUTH_HEADER, JWT + token) : builder;
            Response resp = builder.get();
            return resp.getStatus() == Response.Status.OK.getStatusCode() ? resp.readEntity(expectedResponseClass): null;
        }
        catch (Exception e)
        {
            Log.e(NetworkActions.class.getSimpleName(), "Error getting clinics", e);
            return null;
        }
    }

    private static <T> T getForType(String path, JavaType valueType, String token)
    {
        try
        {
            Invocation.Builder builder = NetworkAccessor.getInstance().getWebTarget()
                    .path(path)
                    .request();
            builder = token != null ? builder.header(AUTH_HEADER, JWT + token) : builder;
            Response resp = builder.get();
            return resp.getStatus() == Response.Status.OK.getStatusCode() ? (T) NetworkAccessor.getMapper().readValue(resp.readEntity(String.class), valueType) : null;
        }
        catch (Exception e)
        {
            Log.e(NetworkActions.class.getSimpleName(), "Error getting clinics", e);
            return null;
        }
    }

    private static <T> T postForEntity(String path, Object payload, Class<T> expectedResponseClass, String token)
    {
        try
        {
            Invocation.Builder builder = NetworkAccessor.getInstance().getWebTarget()
                    .path(path)
                    .request();
            builder = token != null ? builder.header(AUTH_HEADER, JWT + token) : builder;
            Response resp = builder.post(Entity.entity(payload, MediaType.APPLICATION_JSON_TYPE));
            return resp.getStatus() == Response.Status.OK.getStatusCode() ? resp.readEntity(expectedResponseClass) : null;
        }
        catch (Exception e)
        {
            Log.e(NetworkActions.class.getSimpleName(), "Error creating clinic", e);
            return null;
        }
    }

    private static ResponseStatus putForResponseStatus(String path, Object entity, String token)
    {
        try
        {
            Invocation.Builder builder = NetworkAccessor.getInstance().getWebTarget()
                    .path(path)
                    .request();
            builder = token != null ? builder.header(AUTH_HEADER, JWT + token) : builder;
            Response resp = builder.put(Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE));
            return resp.getStatus() == Response.Status.OK.getStatusCode() ? ResponseStatus.SUCCESS : ResponseStatus.FAILURE;
        }
        catch (Exception e)
        {
            Log.e(NetworkActions.class.getSimpleName(), "Error creating clinic", e);
            return ResponseStatus.NOT_AVAILABLE;
        }
    }

    private static ResponseStatus postForResponseStatus(String path, Object entity, String token)
    {
        try
        {
            Invocation.Builder builder = NetworkAccessor.getInstance().getWebTarget()
                    .path(path)
                    .request();
            builder = token != null ? builder.header(AUTH_HEADER, JWT + token) : builder;
            Response resp = builder.post(Entity.entity(entity, MediaType.APPLICATION_JSON_TYPE));
            return resp.getStatus() == Response.Status.OK.getStatusCode() ? ResponseStatus.SUCCESS : ResponseStatus.FAILURE;
        }
        catch (Exception e)
        {
            Log.e(NetworkActions.class.getSimpleName(), "Error creating clinic", e);
            return ResponseStatus.NOT_AVAILABLE;
        }
    }

    private static ResponseStatus getForResponseStatus(String path, String token)
    {
        try
        {
            Invocation.Builder builder = NetworkAccessor.getInstance().getWebTarget()
                    .path(path)
                    .request();
            builder = token != null ? builder.header(AUTH_HEADER, JWT + token) : builder;
            Response resp = builder.get();
            return resp.getStatus() == Response.Status.OK.getStatusCode() ? ResponseStatus.SUCCESS : ResponseStatus.FAILURE;
        }
        catch (Exception e)
        {
            Log.e(NetworkActions.class.getSimpleName(), "Error creating clinic", e);
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
