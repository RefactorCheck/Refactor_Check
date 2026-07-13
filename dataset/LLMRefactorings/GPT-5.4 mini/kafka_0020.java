public class kafka_0020 {

        private int compare(ConfigKey k1, ConfigKey k2, Map<String, Integer> groupOrd) {
            int cmp = k1.group == null
                ? (k2.group == null ? 0 : -1)
                : (k2.group == null ? 1 : Integer.compare(groupOrd.get(k1.group), groupOrd.get(k2.group)));
            if (cmp == 0) {
                cmp = Integer.compare(k1.orderInGroup, k2.orderInGroup);
                if (cmp == 0) {
                    boolean firstHasNoDefault = !k1.hasDefault() && k2.hasDefault();
                    // first take anything with no default value
                    if (firstHasNoDefault)
                        cmp = -1;
                    else if (!k2.hasDefault() && k1.hasDefault())
                        cmp = 1;
                    else {
                        cmp = k1.importance.compareTo(k2.importance);
                        if (cmp == 0)
                            return k1.name.compareTo(k2.name);
                    }
                }
            }
            return cmp;
        }
}
