package com.salesforce.prestosql.event_listener;

import com.google.inject.Injector;
import com.google.inject.Scopes;
import io.airlift.bootstrap.Bootstrap;
import io.prestosql.spi.eventlistener.EventListener;
import io.prestosql.spi.eventlistener.EventListenerFactory;

import java.util.Map;

import static com.google.common.base.Throwables.throwIfUnchecked;
import static io.airlift.configuration.ConfigBinder.configBinder;

public class CustomEventListenerFactory
        implements EventListenerFactory
{
    @Override
    public String getName()
    {
        return "custom-events-log";
    }

    @Override
    public EventListener create(Map<String, String> config)
    {
        try {
            Bootstrap app = new Bootstrap(
                    binder -> {
                        configBinder(binder).bindConfig(CustomEventListenerConfig.class);
                        binder.bind(CustomEventListener.class).in(Scopes.SINGLETON);
                    });

            Injector injector = app
                    .doNotInitializeLogging()
                    .setRequiredConfigurationProperties(config)
                    .initialize();

            return injector.getInstance(CustomEventListener.class);
        }
        catch (Exception e) {
            throwIfUnchecked(e);
            throw new RuntimeException(e);
        }
    }
}
