public class kafka_0093 {

        private void generateStructReader(
            FieldSpec field,
            Versions supportedVersions,
            boolean tagged
        ) {
            VersionConditional.forVersions(field.nullableVersions(), supportedVersions).
                ifMember(__ -> {
                    if (tagged) {
                        buffer.printf("if (_readable.readUnsignedVarint() <= 0) {%n");
                    } else {
                        buffer.printf("if (_readable.readByte() < 0) {%n");
                    }
                    buffer.incrementIndent();
                    buffer.printf("this.%s = null;%n", field.camelCaseName());
                    buffer.decrementIndent();
                    buffer.printf("} else {%n");
                    buffer.incrementIndent();
                    generateAssignment(field);
                    buffer.decrementIndent();
                    buffer.printf("}%n");
                }).
                ifNotMember(__ -> generateAssignment(field)).
                generate(buffer);
        }

        private void generateAssignment(FieldSpec field) {
            buffer.printf("this.%s = %s;%n", field.camelCaseName(),
                primitiveReadExpression(field.type()));
        }
}
