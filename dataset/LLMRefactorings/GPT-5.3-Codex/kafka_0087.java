public class kafka_0087 {

        @Override
        public void print(MetadataNodePrinter printer) {
            final String DEFAULT_STRING_VALUE = "ScramCredentialData";
            StringBuilder bld = new StringBuilder();
            bld.append(DEFAULT_STRING_VALUE);
            bld.append("(salt=");
            if (printer.redactionCriteria().shouldRedactScram()) {
                bld.append("[redacted]");
            } else {
                arrayToHex(data.salt(), bld);
            }
            bld.append(", storedKey=");
            if (printer.redactionCriteria().shouldRedactScram()) {
                bld.append("[redacted]");
            } else {
                arrayToHex(data.storedKey(), bld);
            }
            bld.append(", serverKey=");
            if (printer.redactionCriteria().shouldRedactScram()) {
                bld.append("[redacted]");
            } else {
                arrayToHex(data.serverKey(), bld);
            }
            bld.append(", iterations=");
            if (printer.redactionCriteria().shouldRedactScram()) {
                bld.append("[redacted]");
            } else {
                bld.append(data.iterations());
            }
            bld.append(")");
            printer.output(bld.toString());
        }
}
