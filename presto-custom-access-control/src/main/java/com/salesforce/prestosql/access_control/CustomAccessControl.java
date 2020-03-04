package com.salesforce.prestosql.access_control;

import io.prestosql.spi.connector.CatalogSchemaName;
import io.prestosql.spi.connector.CatalogSchemaTableName;
import io.prestosql.spi.connector.ColumnMetadata;
import io.prestosql.spi.connector.SchemaTableName;
import io.prestosql.spi.security.PrestoPrincipal;
import io.prestosql.spi.security.Privilege;
import io.prestosql.spi.security.SystemAccessControl;
import io.prestosql.spi.security.SystemSecurityContext;

import java.security.Principal;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public class CustomAccessControl
        implements SystemAccessControl
{
    @Override
    public void checkCanSetUser(Optional<Principal> principal, String userName)
    {

    }

    @Override
    public void checkCanSetSystemSessionProperty(SystemSecurityContext context, String propertyName)
    {

    }

    @Override
    public void checkCanAccessCatalog(SystemSecurityContext context, String catalogName)
    {

    }

    @Override
    public Set<String> filterCatalogs(SystemSecurityContext context, Set<String> catalogs)
    {
        return null;
    }

    @Override
    public void checkCanCreateSchema(SystemSecurityContext context, CatalogSchemaName schema)
    {

    }

    @Override
    public void checkCanDropSchema(SystemSecurityContext context, CatalogSchemaName schema)
    {

    }

    @Override
    public void checkCanRenameSchema(SystemSecurityContext context, CatalogSchemaName schema, String newSchemaName)
    {

    }

    @Override
    public void checkCanShowSchemas(SystemSecurityContext context, String catalogName)
    {

    }

    @Override
    public Set<String> filterSchemas(SystemSecurityContext context, String catalogName, Set<String> schemaNames)
    {
        return null;
    }

    @Override
    public void checkCanCreateTable(SystemSecurityContext context, CatalogSchemaTableName table)
    {

    }

    @Override
    public void checkCanDropTable(SystemSecurityContext context, CatalogSchemaTableName table)
    {

    }

    @Override
    public void checkCanRenameTable(SystemSecurityContext context, CatalogSchemaTableName table, CatalogSchemaTableName newTable)
    {

    }

    @Override
    public void checkCanSetTableComment(SystemSecurityContext context, CatalogSchemaTableName table)
    {

    }

    @Override
    public void checkCanShowTablesMetadata(SystemSecurityContext context, CatalogSchemaName schema)
    {

    }

    @Override
    public Set<SchemaTableName> filterTables(SystemSecurityContext context, String catalogName, Set<SchemaTableName> tableNames)
    {
        return null;
    }

    @Override
    public void checkCanShowColumnsMetadata(SystemSecurityContext context, CatalogSchemaTableName table)
    {

    }

    @Override
    public List<ColumnMetadata> filterColumns(SystemSecurityContext context, CatalogSchemaTableName table, List<ColumnMetadata> columns)
    {
        return null;
    }

    @Override
    public void checkCanAddColumn(SystemSecurityContext context, CatalogSchemaTableName table)
    {

    }

    @Override
    public void checkCanDropColumn(SystemSecurityContext context, CatalogSchemaTableName table)
    {

    }

    @Override
    public void checkCanRenameColumn(SystemSecurityContext context, CatalogSchemaTableName table)
    {

    }

    @Override
    public void checkCanSelectFromColumns(SystemSecurityContext context, CatalogSchemaTableName table, Set<String> columns)
    {

    }

    @Override
    public void checkCanInsertIntoTable(SystemSecurityContext context, CatalogSchemaTableName table)
    {

    }

    @Override
    public void checkCanDeleteFromTable(SystemSecurityContext context, CatalogSchemaTableName table)
    {

    }

    @Override
    public void checkCanCreateView(SystemSecurityContext context, CatalogSchemaTableName view)
    {

    }

    @Override
    public void checkCanDropView(SystemSecurityContext context, CatalogSchemaTableName view)
    {

    }

    @Override
    public void checkCanCreateViewWithSelectFromColumns(SystemSecurityContext context, CatalogSchemaTableName table, Set<String> columns)
    {

    }

    @Override
    public void checkCanSetCatalogSessionProperty(SystemSecurityContext context, String catalogName, String propertyName)
    {

    }

    @Override
    public void checkCanGrantTablePrivilege(SystemSecurityContext context, Privilege privilege, CatalogSchemaTableName table, PrestoPrincipal grantee, boolean withGrantOption)
    {

    }

    @Override
    public void checkCanRevokeTablePrivilege(SystemSecurityContext context, Privilege privilege, CatalogSchemaTableName table, PrestoPrincipal revokee, boolean grantOptionFor)
    {

    }

    @Override
    public void checkCanShowRoles(SystemSecurityContext context, String catalogName)
    {

    }
}
