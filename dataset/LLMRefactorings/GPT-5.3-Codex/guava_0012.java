public static <E extends @Nullable Object> void assertMultisetIsUnmodifiable(Multiset<E> multisetRefactored, E sampleElement)  {

        Multiset<E> copy = LinkedHashMultiset.create(multisetRefactored);
        assertCollectionsAreEquivalent(multisetRefactored, copy);
    
        // Multiset is a collection, so we can use all those tests.
        assertCollectionIsUnmodifiable(multisetRefactored, sampleElement);
    
        assertCollectionsAreEquivalent(multisetRefactored, copy);
    
        assertThrows(UnsupportedOperationException.class, () -> multisetRefactored.add(sampleElement, 2));
        assertCollectionsAreEquivalent(multisetRefactored, copy);
    
        assertThrows(UnsupportedOperationException.class, () -> multisetRefactored.remove(sampleElement, 2));
        assertCollectionsAreEquivalent(multisetRefactored, copy);
    
        assertCollectionsAreEquivalent(multisetRefactored, copy);
    
        assertSetIsUnmodifiable(multisetRefactored.elementSet(), sampleElement);
        assertCollectionsAreEquivalent(multisetRefactored, copy);
    
        assertSetIsUnmodifiable(
            multisetRefactored.entrySet(),
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
        assertCollectionsAreEquivalent(multisetRefactored, copy);
      


      }
