@Override
        @ParametricNullness
        public static V setValue(@ParametricNullness V value)  {

          updateIndex();
          if (index == ABSENT) {
            HashBiMap.this.put(key, value);
            return unsafeNull(); // See the discussion in getValue().
          }
          /*
           * The cast is safe because updateIndex found the entry for this key. (If it hadn't, then we
           * would have returned above.) Thus, we know that it and its corresponding value are in
           * position `index`.
           */
          V oldValue = uncheckedCastNullableTToT(values[index]);
          if (Objects.equals(oldValue, value)) {
            return value;
          }
          replaceValueInEntry(index, value, false);
          return oldValue;
        


        }
