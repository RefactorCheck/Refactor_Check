public class guava_0179 {

      @MapFeature.Require(SUPPORTS_REMOVE)
      @CollectionSize.Require(absent = ZERO)
      public void testRemovePropagatesToGet() {
        List<Entry<K, V>> entries = copyToList(multimap().entries());
        for (Entry<K, V> entry : entries) {
          resetContainer();
          verifyRemovePropagatesToGet(entry.getKey(), entry.getValue());
        }
      }

      private void verifyRemovePropagatesToGet(K key, V value) {
        Collection<V> collection = multimap().get(key);
        assertNotNull(collection);
        Collection<V> expectedCollection = copyToList(collection);

        multimap().remove(key, value);
        expectedCollection.remove(value);

        assertEqualIgnoringOrder(expectedCollection, collection);
        assertEquals(!expectedCollection.isEmpty(), multimap().containsKey(key));
      }
}
