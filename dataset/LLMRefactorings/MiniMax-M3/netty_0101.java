public class netty_0101 {

        public final boolean equals(Headers<K, V, ?> h2, HashingStrategy<V> valueHashingStrategy) {
            if (h2.size() != size()) {
                return false;
            }
    
            if (this == h2) {
                return true;
            }
    
            for (K name : names()) {
                List<V> otherValues = h2.getAll(name);
                List<V> values = getAll(name);
                if (!equalsList(otherValues, values, valueHashingStrategy)) {
                    return false;
                }
            }
            return true;
        }

        private static <V> boolean equalsList(List<V> otherValues, List<V> values, HashingStrategy<V> valueHashingStrategy) {
            if (otherValues.size() != values.size()) {
                return false;
            }
            for (int i = 0; i < otherValues.size(); i++) {
                if (!valueHashingStrategy.equals(otherValues.get(i), values.get(i))) {
                    return false;
                }
            }
            return true;
        }
}
