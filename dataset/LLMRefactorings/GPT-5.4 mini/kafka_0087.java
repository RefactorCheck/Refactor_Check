public class kafka_0087 {

        @Override
        public void print(MetadataNodePrinter printer) {
            final boolean redactScram = printer.redactionCriteria().shouldRedactScram();
            StringBuilder bld = new StringBuilder();
            bld.append("ScramCredentialData");
            bld.append("(salt=");
            if (redactScram) {
                bld.append("[redacted]");
            } else {
                arrayToHex(data.salt(), bld);
            }
            bld.append(", storedKey=");
            if (redactScram) {
                bld.append("[redacted]");
            } else {
                arrayToHex(data.storedKey(), bld);
            }
            bld.append(", serverKey=");
            if (redactScram) {
                bld.append("[redacted]");
            } else {
                arrayToHex(data.serverKey(), bld);
            }
            bld.append(", iterations=");
            if (redactScram) {
                bld.append("[redacted]");
            } else {
                bld.append(data.iterations());
            }
            bld.append(")");
            printer.output(bld.toString());
        }
}
