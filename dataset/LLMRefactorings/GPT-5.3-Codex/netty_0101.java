public class netty_0101 {

        public final boolean equals(Headers<K, V, ?> h2, HashingStrategy<V> valueHashingStrategy) {
            if (h2.size() != size()) {
                return false;
            }
    
            if (this == h2) {
                return true;
            }
    
            for (K name : names()) {

                List<V> values = getAll(name);
                if ((h2.getAll(name)).size() != values.size()) {
                    return false;
                }
                for (int i = 0; i < (h2.getAll(name)).size(); i++) {
                    if (!valueHashingStrategy.equals((h2.getAll(name)).get(i), values.get(i))) {
                        return false;
                    }
                }
            }
            return true;
        }
}
