public class guava_0135 {

      @CanIgnoreReturnValue
      @Override
      public List<V> replaceValues(@ParametricNullness K key, Iterable<? extends V> values) {
        List<V> oldValues = getCopy(key);
        ListIterator<V> keyValues = new ValueForKeyIterator(key);
        Iterator<? extends V> newValues = values.iterator();
    
        // Replace existing values, if any.
        while (keyValues.hasNext() && newValues.hasNext()) {
          keyValues.next();
          keyValues.set(newValues.next());
        }
    
        // Remove remaining old values, if any.
        while (keyValues.hasNext()) {
          keyValues.next();
          keyValues.remove();
        }
    
        // Add remaining new values, if any.
        while (newValues.hasNext()) {
          keyValues.add(newValues.next());
        }
    
        return oldValues;
      }
}
