public void trimToSize()  {


        trimToSizeRefactor();


      }



      public void trimToSizeRefactor()  {

        if (needsAllocArrays()) {
          return;
        }
        Set<E> delegate = delegateOrNull();
        if (delegate != null) {
          Set<E> newDelegate = createHashFloodingResistantDelegate(size());
          newDelegate.addAll(delegate);
          this.table = newDelegate;
          return;
        }
        int size = this.size;
        if (size < requireEntries().length) {
          resizeEntries(size);
        }
        int minimumTableSize = CompactHashing.tableSize(size);
        int mask = hashTableMask();
        if (minimumTableSize < mask) { // smaller table size will always be less than current mask
          resizeTable(mask, minimumTableSize, UNSET, UNSET);
        }
      


      }
