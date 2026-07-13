public class guava_0264 {

      public static <E extends @Nullable Object> void assertCollectionIsUnmodifiable(
          Collection<E> collection, E sampleElement) {
        Collection<E> siblingCollection = new ArrayList<>();
        siblingCollection.add(sampleElement);
    
        Collection<E> copy = new ArrayList<>();
        copy.addAll(collection);

        assertUnmodifiable(() -> collection.add(sampleElement), copy, collection);
        assertUnmodifiable(() -> collection.addAll(siblingCollection), copy, collection);
        assertUnmodifiable(() -> collection.clear(), copy, collection);

        assertIteratorIsUnmodifiable(collection.iterator());
        assertCollectionsAreEquivalent(copy, collection);

        assertUnmodifiable(() -> collection.remove(sampleElement), copy, collection);
        assertUnmodifiable(() -> collection.removeAll(siblingCollection), copy, collection);
        assertUnmodifiable(() -> collection.retainAll(siblingCollection), copy, collection);
      }

      private static <E> void assertUnmodifiable(
          Runnable operation, Collection<E> copy, Collection<E> collection) {
        assertThrows(UnsupportedOperationException.class, operation::run);
        assertCollectionsAreEquivalent(copy, collection);
      }
}
