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
                        first = processClientRow(resultSet, protocolMapperTableName, protocolMapperCfgTableName, first);
                    }
    
                    // It means that some provider where processed
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

        private boolean processClientRow(ResultSet resultSet, String protocolMapperTableName, String protocolMapperCfgTableName, boolean first) throws SQLException, DatabaseException {
            if (first) {
                confirmationMessage.append("Migrating claimsMask to protocol mappers for clients: ");
                first = false;
            }

            Object acmObj = resultSet.getObject("ALLOWED_CLAIMS_MASK");
            long mask = (acmObj != null) ? ((Number) acmObj).longValue() : ClaimMask.ALL;

            MigrationProvider migrationProvider = this.kcSession.getProvider(MigrationProvider.class);
            List<ProtocolMapperRepresentation> protocolMappers = migrationProvider.getMappersForClaimMask(mask);

            for (ProtocolMapperRepresentation protocolMapper : protocolMappers) {
                String mapperId = KeycloakModelUtils.generateId();

                InsertStatement insert = new InsertStatement(null, null, protocolMapperTableName)
                        .addColumnValue("ID", mapperId)
                        .addColumnValue("PROTOCOL", protocolMapper.getProtocol())
                        .addColumnValue("NAME", protocolMapper.getName())
                        .addColumnValue("CONSENT_REQUIRED", false)
                        .addColumnValue("PROTOCOL_MAPPER_NAME", protocolMapper.getProtocolMapper())
                        .addColumnValue("CLIENT_ID", resultSet.getString("ID"));
                statements.add(insert);

                for (Map.Entry<String, String> cfgEntry : protocolMapper.getConfig().entrySet()) {
                    InsertStatement cfgInsert = new InsertStatement(null, null, protocolMapperCfgTableName)
                            .addColumnValue("PROTOCOL_MAPPER_ID", mapperId)
                            .addColumnValue("NAME", cfgEntry.getKey())
                            .addColumnValue("VALUE", cfgEntry.getValue());
                    statements.add(cfgInsert);
                }

            }

            confirmationMessage.append(resultSet.getString("NAME") + ", ");
            return first;
        }
}
