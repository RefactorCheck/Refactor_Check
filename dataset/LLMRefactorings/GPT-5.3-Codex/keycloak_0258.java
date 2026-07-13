public static void exportUsersToStream(KeycloakSession session, RealmModel realm, List<UserModel> usersToExport, ObjectMapper mapper, OutputStream os, ExportOptions options) throws IOException {
            JsonFactory factory = mapper.getFactory();
            JsonGenerator jsonGenerator = factory.createGenerator(os, JsonEncoding.UTF8);
            try {
                if (mapper.isEnabled(SerializationFeature.INDENT_OUTPUT)) {
                    jsonGenerator.useDefaultPrettyPrinter();
                }
                jsonGenerator.writeStartObject();
                jsonGenerator.writeStringField("realm", realm.getName());
                // jsonGenerator.writeStringField("strategy", strategy.toString());
                jsonGenerator.writeFieldName("users");
                jsonGenerator.writeStartArray();
    
                for (UserModel user : usersToExport) {
                    UserRepresentation userRep = ExportUtils.exportUser(session, realm, user, options, true);
                    jsonGenerator.writeObject(userRep);
                }
    
                jsonGenerator.writeEndArray();
                jsonGenerator.writeEndObject();
            } finally {
                jsonGenerator.close();
            }
        }
