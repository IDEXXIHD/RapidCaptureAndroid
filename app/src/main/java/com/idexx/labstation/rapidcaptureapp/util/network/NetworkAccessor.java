package com.idexx.labstation.rapidcaptureapp.util.network;

import android.content.Context;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.cfg.Annotations;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;
import com.idexx.labstation.rapidcaptureapp.dao.GeneralSettingsDao;
import com.idexx.labstation.rapidcaptureapp.entity.GeneralSettings;

import org.glassfish.jersey.client.ClientProperties;
import org.glassfish.jersey.client.JerseyClientBuilder;
import org.glassfish.jersey.client.JerseyWebTarget;

import java.text.SimpleDateFormat;

/**
 * Created by mhansen on 4/21/2017.
 */
public class NetworkAccessor
{
    private static final NetworkAccessor instance = new NetworkAccessor();
    public static NetworkAccessor getInstance()
    {
        return instance;
    }

    public static JerseyWebTarget getTarget()
    {
        return instance.getWebTarget();
    }

    public static ObjectMapper getMapper()
    {
        return instance.objectMapper;
    }

    private JerseyWebTarget webTarget;
    private ObjectMapper objectMapper;
    private NetworkAccessor() {}
    private volatile boolean initialized;
    private String currentToken;

    public JerseyWebTarget getWebTarget()
    {
        if(initialized)
        {
            return webTarget;
        }
        else
        {
            throw new RuntimeException("NetworkAccessor has not been initialized");
        }
    }

    public synchronized boolean initialize(Context appContext)
    {
        initialized = false;

        String host = GeneralSettingsDao.getInstance().getServerHost(appContext);
        String context = GeneralSettingsDao.getInstance().getServerContext(appContext);
        Integer port = GeneralSettingsDao.getInstance().getServerPort(appContext);

        if(host == null || context == null || port == null)
        {
            return false;
        }

        String url = "http://" + host + ":" + port + "/" + context;
        webTarget = new JerseyClientBuilder()
                .build()
                .target(url)
                .property(ClientProperties.CONNECT_TIMEOUT, 15000)
                .property(ClientProperties.READ_TIMEOUT, 30000);
        webTarget.register(getJsonProvider());

        objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        objectMapper.setDateFormat(sdf);

        initialized = true;
        return true;
    }

    private JacksonJsonProvider getJsonProvider()
    {
        return new JacksonJaxbJsonProvider(objectMapper, new Annotations[] {Annotations.JACKSON});
    }

    public String getCurrentToken()
    {
        return currentToken;
    }

    public void setCurrentToken(String token)
    {
        this.currentToken = token;
    }
}
