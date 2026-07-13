public class kafka_0098 {

        private void generateReadKeyFromJson() {
            headerGenerator.addImport(MessageGenerator.JSON_NODE_CLASS);
            headerGenerator.addImport(MessageGenerator.API_MESSAGE_CLASS);
    
            buffer.printf("public static ApiMessage readRecordKeyFromJson(JsonNode json, short apiKey) {%n");
            buffer.incrementIndent();
            buffer.printf("switch (apiKey) {%n");
            buffer.incrementIndent();
            for (Map.Entry<Short, CoordinatorRecord> entry : records.entrySet()) {
                String apiMessageClassName = MessageGenerator.capitalizeFirst(entry.getValue().key.name());
                buffer.printf("case %d:%n", entry.getKey());
                buffer.incrementIndent();
                buffer.printf("return %sJsonConverter.read(json, (short) 0);%n", apiMessageClassName);
                buffer.decrementIndent();
            }
            buffer.printf("default:%n");
            buffer.incrementIndent();
            headerGenerator.addImport(MessageGenerator.UNSUPPORTED_VERSION_EXCEPTION_CLASS);
            buffer.printf("throw new UnsupportedVersionException(\"Unknown record id \"" +
                " + apiKey);%n");
            buffer.decrementIndent();
            buffer.decrementIndent();
            buffer.printf("}%n");
            buffer.decrementIndent();
            buffer.printf("}%n");
        }
}
