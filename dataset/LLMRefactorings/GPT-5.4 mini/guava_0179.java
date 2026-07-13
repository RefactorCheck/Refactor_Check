public class guava_0179 {

      @MapFeature.Require(SUPPORTS_REMOVE)
      @CollectionSize.Require(absent = ZERO)
      public void testRemovePropagatesToGetRefactored() {
        List<Entry<K, V>> entries = copyToList(multimap().entries());
        for (Entry<K, V> entry : entries) {
          resetContainer();
    
          K key = entry.getKey();
          V value = entry.getValue();
          Collection<V> collection = multimap().get(key);
          assertNotNull(collection);
          Collection<V> expectedCollection = copyToList(collection);
    
          multimap().remove(key, value);
          expectedCollection.remove(value);
    
          assertEqualIgnoringOrder(expectedCollection, collection);
          assertEquals(!expectedCollection.isEmpty(), multimap().containsKey(key));
        }
      }
}
