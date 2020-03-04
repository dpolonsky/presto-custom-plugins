package com.salesforce.prestosql.event_listener;

import io.airlift.configuration.Config;

public class CustomEventListenerConfig
{
    private String hostname;

    public String getHostname()
    {
        return hostname;
    }

    @Config("hostname")
    public void setHostname(String hostname)
    {
        this.hostname = hostname;
    }
}
