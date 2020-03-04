package com.salesforce.prestosql.event_listener;

import com.google.common.collect.ImmutableList;
import io.prestosql.spi.Plugin;
import io.prestosql.spi.eventlistener.EventListenerFactory;

public class CustomEventListenerPlugin
        implements Plugin
{
    @Override
    public Iterable<EventListenerFactory> getEventListenerFactories()
    {
        return ImmutableList.of(new CustomEventListenerFactory());
    }
}
