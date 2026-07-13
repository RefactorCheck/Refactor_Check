public class guava_0220 {

      @Override
      public void removePredecessor(N node) {
        checkNotNull(node);
    
        if (removePredecessorInternal(node)) {
          checkNonNegative(--predecessorCount);
    
          if (orderedNodeConnections != null) {
            orderedNodeConnections.remove(new NodeConnection.Pred<>(node));
          }
        }
      }

      private boolean removePredecessorInternal(N node) {
        Object previousValue = adjacentNodeValues.get(node);
        boolean removedPredecessor;
    
        if (previousValue == PRED) {
          adjacentNodeValues.remove(node);
          removedPredecessor = true;
        } else if (previousValue instanceof PredAndSucc) {
          adjacentNodeValues.put((N) node, ((PredAndSucc) previousValue).successorValue);
          removedPredecessor = true;
        } else {
          removedPredecessor = false;
        }
        return removedPredecessor;
      }
}
