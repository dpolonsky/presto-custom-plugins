package com.salesforce.prestosql.access_auth;

import com.google.inject.Injector;
import com.google.inject.Scopes;
import io.airlift.bootstrap.Bootstrap;
import io.prestosql.spi.security.PasswordAuthenticator;
import io.prestosql.spi.security.PasswordAuthenticatorFactory;

import java.util.Map;

import static com.google.common.base.Throwables.throwIfUnchecked;
import static io.airlift.configuration.ConfigBinder.configBinder;

public class CustomAuthenticatorFactory
        implements PasswordAuthenticatorFactory
{
    @Override
    public String getName()
    {
        return "custom-auth";
    }

    @Override
    public PasswordAuthenticator create(Map<String, String> config)
    {
        try {
            Bootstrap app = new Bootstrap(
                    binder -> {
                        configBinder(binder).bindConfig(CustomAuthenticatorConfig.class);
                        binder.bind(CustomAuthenticator.class).in(Scopes.SINGLETON);
                    });

            Injector injector = app
                    .doNotInitializeLogging()
                    .setRequiredConfigurationProperties(config)
                    .initialize();

            return injector.getInstance(CustomAuthenticator.class);
        }
        catch (Exception e) {
            throwIfUnchecked(e);
            throw new RuntimeException(e);
        }
    }
}
