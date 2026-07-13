public class keycloak_0258 {

        public static void exportUsersToStream(KeycloakSession session, RealmModel realm, List<UserModel> usersToExport, ObjectMapper mapper, OutputStream os, ExportOptions options) throws IOException {
            JsonFactory factory = mapper.getFactory();
            JsonGenerator generator = factory.createGenerator(os, JsonEncoding.UTF8);
            try {
                if (mapper.isEnabled(SerializationFeature.INDENT_OUTPUT)) {
                    generator.useDefaultPrettyPrinter();
                }
                generator.writeStartObject();
                generator.writeStringField("realm", realm.getName());
                // generator.writeStringField("strategy", strategy.toString());
                generator.writeFieldName("users");
                generator.writeStartArray();
    
                for (UserModel user : usersToExport) {
                    UserRepresentation userRep = ExportUtils.exportUser(session, realm, user, options, true);
                    generator.writeObject(userRep);
                }
    
                generator.writeEndArray();
                generator.writeEndObject();
            } finally {
                generator.close();
            }
        }
}
