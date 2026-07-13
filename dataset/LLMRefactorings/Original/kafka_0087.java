public class kafka_0087 {

        @Override
        public void print(MetadataNodePrinter printer) {
            StringBuilder bld = new StringBuilder();
            bld.append("ScramCredentialData");
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
