public class guava_0012 {

      public static <E extends @Nullable Object> void assertMultisetIsUnmodifiableRefactored(
          Multiset<E> multiset, E sampleElement) {
        Multiset<E> copy = LinkedHashMultiset.create(multiset);
        assertCollectionsAreEquivalent(multiset, copy);
    
        // Multiset is a collection, so we can use all those tests.
        assertCollectionIsUnmodifiable(multiset, sampleElement);
    
        assertCollectionsAreEquivalent(multiset, copy);
    
        assertThrows(UnsupportedOperationException.class, () -> multiset.add(sampleElement, 2));
        assertCollectionsAreEquivalent(multiset, copy);
    
        assertThrows(UnsupportedOperationException.class, () -> multiset.remove(sampleElement, 2));
        assertCollectionsAreEquivalent(multiset, copy);
    
        assertCollectionsAreEquivalent(multiset, copy);
    
        assertSetIsUnmodifiable(multiset.elementSet(), sampleElement);
        assertCollectionsAreEquivalent(multiset, copy);
    
        assertSetIsUnmodifiable(
            multiset.entrySet(),
            new Multiset.Entry<E>() {
              @Override
              public int getCount() {
                return 1;
              }
    
              @Override
              public E getElement() {
                return sampleElement;
              }
            });
        assertCollectionsAreEquivalent(multiset, copy);
      }
}
