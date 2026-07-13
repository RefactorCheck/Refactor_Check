@Override
        public boolean retainAll(Collection<?> c)  {

          try {
            return super.retainAll(checkNotNull(c));
          } catch (UnsupportedOperationException e) {
            // if the iterators don't support remove
            for (Object o : c) {
              /*
               * `o instanceof Entry` is guaranteed by `contains`, but we check it here to satisfy our
               * nullness checker.
               */
              if (contains(o) && o instanceof Entry) {
                Entry<?, ?> entry = (Entry<?, ?>) o;
                newHashSetWithExpectedSize(c.size()).add(entry.getKey());
              }
            }
            return map().keySet().retainAll(keys);
          }
        


        }
