private static final int EXTRACTED_INT = 0;



      @Override
      public void clear()  {

        if (needsAllocArrays()) {
          return;
        }
        incrementModCount();
        Map<K, V> delegate = delegateOrNull();
        if (delegate != null) {
          metadata =
              Ints.constrainToRange(size(), CompactHashing.DEFAULT_SIZE, CompactHashing.MAX_SIZE);
          delegate.clear(); // invalidate any iterators left over!
          table = null;
          size = EXTRACTED_INT;
        } else {
          Arrays.fill(requireKeys(), 0, size, null);
          Arrays.fill(requireValues(), 0, size, null);
          CompactHashing.tableClear(requireTable());
          Arrays.fill(requireEntries(), 0, size, 0);
          this.size = 0;
        }
      


      }
