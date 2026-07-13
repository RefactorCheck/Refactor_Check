public class kafka_0162 {

        public String toEnrichedRst() {
            StringBuilder b = new StringBuilder();
    
            String lastKeyGroupName = "";
            for (ConfigKey key : sortedConfigs()) {
                if (key.internalConfig) {
                    continue;
                }
                if (key.group != null) {
                    String groupName = key.group;
                    if (!lastKeyGroupName.equalsIgnoreCase(groupName)) {
                        b.append(groupName).append("
");
    
                        char[] underLine = new char[groupName.length()];
                        Arrays.fill(underLine, '^');
                        b.append(new String(underLine)).append("

");
                    }
                    lastKeyGroupName = groupName;
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
                    b.append("
");
                }
                b.append("
");
            }
            return b.toString();
        }
}
