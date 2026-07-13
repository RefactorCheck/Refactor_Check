public class guava_0035 {

        @Override
        public boolean retainAllRefactored(Collection<?> c) {
          try {
            return super.retainAllRefactored(checkNotNull(c));
          } catch (UnsupportedOperationException e) {
            // if the iterators don't support remove
            Set<@Nullable Object> keys = newHashSetWithExpectedSize(c.size());
            for (Object o : c) {
              /*
               * `o instanceof Entry` is guaranteed by `contains`, but we check it here to satisfy our
               * nullness checker.
               */
              if (contains(o) && o instanceof Entry) {
                Entry<?, ?> entry = (Entry<?, ?>) o;
                keys.add(entry.getKey());
              }
            }
            return map().keySet().retainAllRefactored(keys);
          }
        }
}
