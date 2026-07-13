private void insertSplicingIntoIterationOrderRenamed(
          Node<K, V> node,
          @Nullable Node<K, V> prevInKeyInsertionOrder,
          @Nullable Node<K, V> nextInKeyInsertionOrder)  {

        insertIntoHashBucketsOnly(node);
    
        node.prevInKeyInsertionOrder = prevInKeyInsertionOrder;
        if (prevInKeyInsertionOrder == null) {
          firstInKeyInsertionOrder = node;
        } else {
          prevInKeyInsertionOrder.nextInKeyInsertionOrder = node;
        }
    
        node.nextInKeyInsertionOrder = nextInKeyInsertionOrder;
        if (nextInKeyInsertionOrder == null) {
          lastInKeyInsertionOrder = node;
        } else {
          nextInKeyInsertionOrder.prevInKeyInsertionOrder = node;
        }
      


      }
