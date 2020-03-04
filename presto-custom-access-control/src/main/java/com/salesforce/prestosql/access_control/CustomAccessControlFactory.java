package com.salesforce.prestosql.access_control;

import com.google.inject.Injector;
import com.google.inject.Scopes;
import io.airlift.bootstrap.Bootstrap;
import io.prestosql.spi.security.SystemAccessControl;
import io.prestosql.spi.security.SystemAccessControlFactory;

import java.util.Map;

import static com.google.common.base.Throwables.throwIfUnchecked;
import static io.airlift.configuration.ConfigBinder.configBinder;

public class CustomAccessControlFactory
        implements SystemAccessControlFactory
{
    @Override
    public String getName()
    {
        return "custom-access";
    }

    @Override
    public SystemAccessControl create(Map<String, String> config)
    {
        try {
            Bootstrap app = new Bootstrap(
                    binder -> {
                        configBinder(binder).bindConfig(JdbcAccessConfig.class);
                        binder.bind(CustomAccessControl.class).in(Scopes.SINGLETON);
                    });

            Injector injector = app
                    .doNotInitializeLogging()
                    .setRequiredConfigurationProperties(config)
                    .initialize();

            return injector.getInstance(CustomAccessControl.class);
        }
        catch (Exception e) {
            throwIfUnchecked(e);
            throw new RuntimeException(e);
        }
    }
}
