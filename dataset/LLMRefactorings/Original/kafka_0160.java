public class kafka_0160 {

        private void generateWriteJson() {
            headerGenerator.addImport(MessageGenerator.JSON_NODE_CLASS);
            headerGenerator.addImport(MessageGenerator.API_MESSAGE_CLASS);
    
            buffer.printf("public static JsonNode writeJson(ApiMessage apiMessage, short apiVersion) {%n");
            buffer.incrementIndent();
            buffer.printf("switch (apiMessage.apiKey()) {%n");
            buffer.incrementIndent();
            for (Map.Entry<Short, MessageSpec> entry : apis.entrySet()) {
                String apiMessageClassName = MessageGenerator.capitalizeFirst(entry.getValue().name());
                buffer.printf("case %d:%n", entry.getKey());
                buffer.incrementIndent();
                buffer.printf("return %sJsonConverter.write((%s) apiMessage, apiVersion);%n", apiMessageClassName, apiMessageClassName);
                buffer.decrementIndent();
            }
            buffer.printf("default:%n");
            buffer.incrementIndent();
            headerGenerator.addImport(MessageGenerator.UNSUPPORTED_VERSION_EXCEPTION_CLASS);
            buffer.printf("throw new UnsupportedVersionException(\"Unknown metadata id \"" +
                    " + apiMessage.apiKey());%n");
            buffer.decrementIndent();
            buffer.decrementIndent();
            buffer.printf("}%n");
            buffer.decrementIndent();
            buffer.printf("}%n");
        }
}
