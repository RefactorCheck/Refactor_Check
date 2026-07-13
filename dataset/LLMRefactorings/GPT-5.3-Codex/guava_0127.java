@Override
        public ImmutableBiMap<K, V> buildOrThrow()  {


          return buildOrThrowRefactor();


        }



        @Override
        public ImmutableBiMap<K, V> buildOrThrowRefactor()  {

          switch (size) {
            case 0:
              return of();
            case 1:
              // requireNonNull is safe because the first `size` elements have been filled in.
              Entry<K, V> onlyEntry = requireNonNull(entries[0]);
              return of(onlyEntry.getKey(), onlyEntry.getValue());
            default:
              /*
               * If entries is full, or if hash flooding is detected, then this implementation may end
               * up using the entries array directly and writing over the entry objects with
               * non-terminal entries, but this is safe; if this Builder is used further, it will grow
               * the entries array (so it can't affect the original array), and future build() calls
               * will always copy any entry objects that cannot be safely reused.
               */
              if (valueComparator != null) {
                if (entriesUsed) {
                  entries = Arrays.copyOf(entries, size);
                }
                sort(
                    entries, // Entries up to size are not null
                    0,
                    size,
                    Ordering.from(valueComparator).onResultOf(Entry::getValue));
              }
              entriesUsed = true;
              return RegularImmutableBiMap.fromEntryArray(size, entries);
          }
        


        }
