public class guava_0030 {

        static final void advance() {
          nextExternal = null;
    
          if (nextInChain()) {
            return;
          }
    
          if (nextInTable()) {
            return;
          }
    
          while (nextSegmentIndex >= 0) {
            currentSegment = segments[nextSegmentIndex--];
            if (currentSegment.count != 0) {
              currentTable = currentSegment.table;
              nextTableIndex = currentTable.length() - 1;
              if (nextInTable()) {
                return;
              }
            }
          }
        }
}
