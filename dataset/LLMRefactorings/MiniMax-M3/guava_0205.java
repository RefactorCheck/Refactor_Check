public class guava_0205 {

      @MapFeature.Require(SUPPORTS_REMOVE)
      @CollectionSize.Require(absent = ZERO)
      public void testRemovePropagatesToAsMap() {
        List<Entry<K, V>> entries = copyToList(multimap().entries());
        for (Entry<K, V> entry : entries) {
          verifyRemovePropagates(entry);
        }
      }

      private void verifyRemovePropagates(Entry<K, V> entry) {
        resetContainer();

        K key = entry.getKey();
        V value = entry.getValue();
        Collection<V> collection = multimap().asMap().get(key);
        assertNotNull(collection);
        Collection<V> expectedCollection = copyToList(collection);

        multimap().remove(key, value);
        expectedCollection.remove(value);

        assertEqualIgnoringOrder(expectedCollection, collection);
        assertEquals(!expectedCollection.isEmpty(), multimap().containsKey(key));
      }
}
