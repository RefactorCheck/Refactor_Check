public class kafka_0161 {

        private void generateReadJson() {
            headerGenerator.addImport(MessageGenerator.JSON_NODE_CLASS);
            headerGenerator.addImport(MessageGenerator.API_MESSAGE_CLASS);

            buffer.printf("public static ApiMessage readJson(JsonNode json, short apiKey, short apiVersion) {%n");
            buffer.incrementIndent();
            buffer.printf("switch (apiKey) {%n");
            buffer.incrementIndent();
            for (Map.Entry<Short, MessageSpec> entry : apis.entrySet()) {
                generateApiCase(entry);
            }
            buffer.printf("default:%n");
            buffer.incrementIndent();
            headerGenerator.addImport(MessageGenerator.UNSUPPORTED_VERSION_EXCEPTION_CLASS);
            buffer.printf("throw new UnsupportedVersionException(\"Unknown metadata id \"" +
                    " + apiKey);%n");
            buffer.decrementIndent();
            buffer.decrementIndent();
            buffer.printf("}%n");
            buffer.decrementIndent();
            buffer.printf("}%n");
        }

        private void generateApiCase(Map.Entry<Short, MessageSpec> entry) {
            String apiMessageClassName = MessageGenerator.capitalizeFirst(entry.getValue().name());
            buffer.printf("case %d:%n", entry.getKey());
            buffer.incrementIndent();
            buffer.printf("return %sJsonConverter.read(json, apiVersion);%n", apiMessageClassName);
            buffer.decrementIndent();
        }
}
