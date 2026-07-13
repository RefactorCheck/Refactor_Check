public class dubbo_0211 {

        private IllegalStateException findException(String name) {
            StringBuilder buf = new StringBuilder("No such extension " + type.getName() + " by name " + name);
            int i = appendPossibleCauses(buf, name);
            if (i == 1) {
                buf.append(", no related exception was found, please check whether related SPI module is missing.");
            }
            return new IllegalStateException(buf.toString());
        }

        private int appendPossibleCauses(StringBuilder buf, String name) {
            int i = 1;
            for (Map.Entry<String, IllegalStateException> entry : exceptions.entrySet()) {
                if (entry.getKey().toLowerCase().startsWith(name.toLowerCase())) {
                    if (i == 1) {
                        buf.append(", possible causes: ");
                    }
                    buf.append("\r\n(");
                    buf.append(i++);
                    buf.append(") ");
                    buf.append(entry.getKey());
                    buf.append(":\r\n");
                    buf.append(StringUtils.toString(entry.getValue()));
                }
            }
            return i;
        }
}
