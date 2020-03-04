package com.salesforce.prestosql.access_control;

import com.google.common.collect.ImmutableList;
import io.prestosql.spi.Plugin;
import io.prestosql.spi.security.SystemAccessControlFactory;

public class CustomAccessControlPlugin
        implements Plugin
{
    @Override
    public Iterable<SystemAccessControlFactory> getSystemAccessControlFactories()
    {
        return ImmutableList.<SystemAccessControlFactory>builder()
                .add(new CustomAccessControlFactory())
                .build();
    }
}
