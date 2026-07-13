public class guava_0035 {

        @Override
        public boolean retainAll(Collection<?> c) {
          try {
            return super.retainAll(checkNotNull(c));
          } catch (UnsupportedOperationException e) {
            return retainKeysByEntries(c);
          }
        }

        private boolean retainKeysByEntries(Collection<?> c) {
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
          return map().keySet().retainAll(keys);
        }
}
