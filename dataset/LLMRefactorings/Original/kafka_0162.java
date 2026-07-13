public class kafka_0162 {

        public String toEnrichedRst() {
            StringBuilder b = new StringBuilder();
    
            String lastKeyGroupName = "";
            for (ConfigKey key : sortedConfigs()) {
                if (key.internalConfig) {
                    continue;
                }
                if (key.group != null) {
                    if (!lastKeyGroupName.equalsIgnoreCase(key.group)) {
                        b.append(key.group).append("\n");
    
                        char[] underLine = new char[key.group.length()];
                        Arrays.fill(underLine, '^');
                        b.append(new String(underLine)).append("\n\n");
                    }
                    lastKeyGroupName = key.group;
                }
    
                getConfigKeyRst(key, b);
    
                if (key.dependents != null && key.dependents.size() > 0) {
                    int j = 0;
                    b.append("  * Dependents: ");
                    for (String dependent : key.dependents) {
                        b.append("``");
                        b.append(dependent);
                        if (++j == key.dependents.size())
                            b.append("``");
                        else
                            b.append("``, ");
                    }
                    b.append("\n");
                }
                b.append("\n");
            }
            return b.toString();
        }
}
