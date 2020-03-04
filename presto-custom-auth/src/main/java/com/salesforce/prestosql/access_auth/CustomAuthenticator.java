package com.salesforce.prestosql.access_auth;

import io.prestosql.spi.security.PasswordAuthenticator;

import java.security.Principal;

public class CustomAuthenticator
        implements PasswordAuthenticator
{
    @Override
    public Principal createAuthenticatedPrincipal(String user, String password)
    {
        return null;
    }
}
