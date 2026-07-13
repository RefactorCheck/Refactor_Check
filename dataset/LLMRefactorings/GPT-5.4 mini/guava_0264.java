public class guava_0264 {

      public static <E extends @Nullable Object> void assertCollectionIsUnmodifiableRefactored(
          Collection<E> collection, E sampleElement) {
        Collection<E> siblingCollection = new ArrayList<>();
        siblingCollection.add(sampleElement);
    
        Collection<E> copy = new ArrayList<>();
        copy.addAll(collection);
    
        assertThrows(UnsupportedOperationException.class, () -> collection.add(sampleElement));
        assertCollectionsAreEquivalent(copy, collection);
    
        assertThrows(UnsupportedOperationException.class, () -> collection.addAll(siblingCollection));
        assertCollectionsAreEquivalent(copy, collection);
    
        assertThrows(UnsupportedOperationException.class, () -> collection.clear());
        assertCollectionsAreEquivalent(copy, collection);
    
        assertIteratorIsUnmodifiable(collection.iterator());
        assertCollectionsAreEquivalent(copy, collection);
    
        assertThrows(UnsupportedOperationException.class, () -> collection.remove(sampleElement));
        assertCollectionsAreEquivalent(copy, collection);
    
        assertThrows(
            UnsupportedOperationException.class, () -> collection.removeAll(siblingCollection));
        assertCollectionsAreEquivalent(copy, collection);
    
        assertThrows(
            UnsupportedOperationException.class, () -> collection.retainAll(siblingCollection));
        assertCollectionsAreEquivalent(copy, collection);
      }
}
