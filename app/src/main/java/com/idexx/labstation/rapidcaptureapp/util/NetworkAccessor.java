package com.idexx.labstation.rapidcaptureapp.util;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.jaxrs.cfg.Annotations;
import com.fasterxml.jackson.jaxrs.json.JacksonJaxbJsonProvider;
import com.fasterxml.jackson.jaxrs.json.JacksonJsonProvider;

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

    public synchronized void initialize(String host, String context, int port)
    {
        String url = "http://" + host + ":" + port + "/" + context;
        webTarget = new JerseyClientBuilder().build().target(url);
        webTarget.register(getJsonProvider());

        objectMapper = new ObjectMapper();
        objectMapper.setVisibility(PropertyAccessor.ALL, JsonAutoDetect.Visibility.NONE);
        objectMapper.setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
        objectMapper.setDateFormat(sdf);

        initialized = true;
    }

    private JacksonJsonProvider getJsonProvider()
    {
        return new JacksonJaxbJsonProvider(objectMapper, new Annotations[] {Annotations.JACKSON});
    }
}
