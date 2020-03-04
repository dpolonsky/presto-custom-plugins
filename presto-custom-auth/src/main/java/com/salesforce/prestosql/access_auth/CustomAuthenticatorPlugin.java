package com.salesforce.prestosql.access_auth;

import com.google.common.collect.ImmutableList;
import io.prestosql.spi.Plugin;
import io.prestosql.spi.security.PasswordAuthenticatorFactory;

public class CustomAuthenticatorPlugin
        implements Plugin
{
    @Override
    public Iterable<PasswordAuthenticatorFactory> getPasswordAuthenticatorFactories()
    {
        return ImmutableList.<PasswordAuthenticatorFactory>builder()
                .add(new CustomAuthenticatorFactory())
                .build();
    }
}
