public class kafka_0097 {

        void generate(CodeBuffer buffer) {
            if (nullableVersions.intersect(possibleVersions).empty()) {
                if (ifShouldNotBeNull != null) {
                    runWithOptionalBlockScope(ifShouldNotBeNull);
                }
            } else {
                if (ifNull != null) {
                    buffer.printf("if (%s) {%n", conditionalGenerator.generate(name, false));
                    buffer.incrementIndent();
                    ifNull.run();
                    buffer.decrementIndent();
                    if (ifShouldNotBeNull != null) {
                        buffer.printf("} else {%n");
                        buffer.incrementIndent();
                        ifShouldNotBeNull.run();
                        buffer.decrementIndent();
                    }
                    buffer.printf("}%n");
                } else if (ifShouldNotBeNull != null) {
                    buffer.printf("if (%s) {%n", conditionalGenerator.generate(name, true));
                    buffer.incrementIndent();
                    ifShouldNotBeNull.run();
                    buffer.decrementIndent();
                    buffer.printf("}%n");
                }
            }
        }

        private void runWithOptionalBlockScope(Runnable action) {
            if (alwaysEmitBlockScope) {
                buffer.printf("{%n");
                buffer.incrementIndent();
            }
            action.run();
            if (alwaysEmitBlockScope) {
                buffer.decrementIndent();
                buffer.printf("}%n");
            }
        }
}
