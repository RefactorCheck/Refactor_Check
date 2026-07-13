public class kafka_0093 {

        private void generateStructReader(
            FieldSpec field,
            Versions supportedVersions,
            boolean tagged
        ) {
            final String fieldName = field.camelCaseName();
            final String readExpression = primitiveReadExpression(field.type());
            VersionConditional.forVersions(field.nullableVersions(), supportedVersions).
                ifMember(__ -> {
                    if (tagged) {
                        buffer.printf("if (_readable.readUnsignedVarint() <= 0) {%n");
                    } else {
                        buffer.printf("if (_readable.readByte() < 0) {%n");
                    }
                    buffer.incrementIndent();
                    buffer.printf("this.%s = null;%n", fieldName);
                    buffer.decrementIndent();
                    buffer.printf("} else {%n");
                    buffer.incrementIndent();
                    buffer.printf("this.%s = %s;%n", fieldName,
                        readExpression);
                    buffer.decrementIndent();
                    buffer.printf("}%n");
                }).
                ifNotMember(__ -> buffer.printf("this.%s = %s;%n", fieldName,
                    readExpression)).
                generate(buffer);
        }
}
