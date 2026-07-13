public class kafka_0087 {

        private void appendScramField(StringBuilder bld, MetadataNodePrinter printer, byte[] value) {
            if (printer.redactionCriteria().shouldRedactScram()) {
                bld.append("[redacted]");
            } else {
                arrayToHex(value, bld);
            }
        }

        private void appendScramField(StringBuilder bld, MetadataNodePrinter printer, int value) {
            if (printer.redactionCriteria().shouldRedactScram()) {
                bld.append("[redacted]");
            } else {
                bld.append(value);
            }
        }

        @Override
        public void print(MetadataNodePrinter printer) {
            StringBuilder bld = new StringBuilder();
            bld.append("ScramCredentialData");
            bld.append("(salt=");
            appendScramField(bld, printer, data.salt());
            bld.append(", storedKey=");
            appendScramField(bld, printer, data.storedKey());
            bld.append(", serverKey=");
            appendScramField(bld, printer, data.serverKey());
            bld.append(", iterations=");
            appendScramField(bld, printer, data.iterations());
            bld.append(")");
            printer.output(bld.toString());
        }
}
