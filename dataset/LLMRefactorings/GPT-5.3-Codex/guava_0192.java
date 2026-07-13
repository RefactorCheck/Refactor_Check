private Set<E> delegate = delegateOrNull();



      @Override
      public void clear()  {

        if (needsAllocArrays()) {
          return;
        }
        incrementModCount();
        if (delegate != null) {
          metadata =
              Ints.constrainToRange(size(), CompactHashing.DEFAULT_SIZE, CompactHashing.MAX_SIZE);
          delegate.clear(); // invalidate any iterators left over!
          table = null;
          size = 0;
        } else {
          Arrays.fill(requireElements(), 0, size, null);
          CompactHashing.tableClear(requireTable());
          Arrays.fill(requireEntries(), 0, size, 0);
          this.size = 0;
        }
      


      }
