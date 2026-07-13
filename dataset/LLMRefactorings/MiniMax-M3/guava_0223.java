public class guava_0223 {

    @Override
    public Map<K, Collection<V>> create(Object... elements) {
        Set<K> keySet = new HashSet<>();
        List<Entry<K, V>> builder = new ArrayList<>();
        for (Object o : elements) {
            Entry<?, ?> entry = (Entry<?, ?>) o;
            // These come from Entry<K, Collection<V>>> objects somewhere.
            @SuppressWarnings("unchecked")
            K key = (K) entry.getKey();
            keySet.add(key);
            addValuesToBuilder(key, (Collection<?>) entry.getValue(), builder);
        }
        checkArgument(keySet.size() == elements.length, "Duplicate keys");
        return multimapGenerator.create(builder.toArray()).asMap();
    }

    private void addValuesToBuilder(K key, Collection<?> values, List<Entry<K, V>> builder) {
        for (Object v : values) {
            // These come from Entry<K, Collection<V>>> objects somewhere.
            @SuppressWarnings("unchecked")
            V value = (V) v;
            builder.add(mapEntry(key, value));
        }
    }
}
