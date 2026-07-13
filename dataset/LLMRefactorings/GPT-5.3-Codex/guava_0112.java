@Override
        public static int remove(@Nullable Object element, int occurrences)  {

          checkNonnegative(occurrences, "occurrences");
          if (occurrences == 0) {
            return count(element);
          }
    
          Collection<V> values = safeGet(multimap.asMap(), element);
    
          if (values == null) {
            return 0;
          }
    
          int oldCount = values.size();
          if (occurrences >= oldCount) {
            values.clear();
          } else {
            Iterator<V> iterator = values.iterator();
            for (int i = 0; i < occurrences; i++) {
              iterator.next();
              iterator.remove();
            }
          }
          return oldCount;
        


        }
