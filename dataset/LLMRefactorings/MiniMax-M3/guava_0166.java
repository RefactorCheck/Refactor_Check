public class guava_0166 {

        @Override
        @ParametricNullness
        public V setValue(@ParametricNullness V value) {
          updateIndex();
          if (index == ABSENT) {
            HashBiMap.this.put(key, value);
            return unsafeNull(); // See the discussion in getValue().
          }
          return replaceValueAtCurrentIndex(value);
        }

        private V replaceValueAtCurrentIndex(@ParametricNullness V value) {
          V oldValue = uncheckedCastNullableTToT(values[index]);
          if (Objects.equals(oldValue, value)) {
            return value;
          }
          replaceValueInEntry(index, value, false);
          return oldValue;
        }
}
