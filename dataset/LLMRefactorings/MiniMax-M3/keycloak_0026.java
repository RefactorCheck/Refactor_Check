public class keycloak_0026 {

        protected void addDefaultProtocolMappers() throws SQLException, DatabaseException {
            String protocolMapperTableName = database.correctObjectName("PROTOCOL_MAPPER", Table.class);
            String protocolMapperCfgTableName = database.correctObjectName("PROTOCOL_MAPPER_CONFIG", Table.class);
    
            PreparedStatement statement = jdbcConnection.prepareStatement("select ID, NAME, ALLOWED_CLAIMS_MASK from " + getTableName("CLIENT"));
    
            try {
                ResultSet resultSet = statement.executeQuery();
                try {
                    boolean first = true;
                    while (resultSet.next()) {
                        if (first) {
                            confirmationMessage.append("Migrating claimsMask to protocol mappers for clients: ");
                            first = false;
                        }
    
                        Object acmObj = resultSet.getObject("ALLOWED_CLAIMS_MASK");
                        long mask = (acmObj != null) ? ((Number) acmObj).longValue() : ClaimMask.ALL;
    
                        MigrationProvider migrationProvider = this.kcSession.getProvider(MigrationProvider.class);
                        List<ProtocolMapperRepresentation> protocolMappers = migrationProvider.getMappersForClaimMask(mask);
    
                        for (ProtocolMapperRepresentation protocolMapper : protocolMappers) {
                            addProtocolMapperInsert(protocolMapper, resultSet.getString("ID"), protocolMapperTableName, protocolMapperCfgTableName);
                        }
    
                        confirmationMessage.append(resultSet.getString("NAME") + ", ");
                    }
    
                    if (!first) {
                        confirmationMessage.append(". ");
                    }
                } finally {
                    resultSet.close();
                }
            } finally {
                statement.close();
            }
        }

        private void addProtocolMapperInsert(ProtocolMapperRepresentation protocolMapper, String clientId, String protocolMapperTableName, String protocolMapperCfgTableName) {
            String mapperId = KeycloakModelUtils.generateId();

            InsertStatement insert = new InsertStatement(null, null, protocolMapperTableName)
                    .addColumnValue("ID", mapperId)
                    .addColumnValue("PROTOCOL", protocolMapper.getProtocol())
                    .addColumnValue("NAME", protocolMapper.getName())
                    .addColumnValue("CONSENT_REQUIRED", false)
                    .addColumnValue("PROTOCOL_MAPPER_NAME", protocolMapper.getProtocolMapper())
                    .addColumnValue("CLIENT_ID", clientId);
            statements.add(insert);

            for (Map.Entry<String, String> cfgEntry : protocolMapper.getConfig().entrySet()) {
                InsertStatement cfgInsert = new InsertStatement(null, null, protocolMapperCfgTableName)
                        .addColumnValue("PROTOCOL_MAPPER_ID", mapperId)
                        .addColumnValue("NAME", cfgEntry.getKey())
                        .addColumnValue("VALUE", cfgEntry.getValue());
                statements.add(cfgInsert);
            }
        }
}
