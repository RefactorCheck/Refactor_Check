public class guava_0253 {

        private AvlNode<E> rebalance() {
          switch (balanceFactor()) {
            case -2:
              return rebalanceRightHeavy();
            case 2:
              return rebalanceLeftHeavy();
            default:
              recomputeHeight();
              return this;
          }
        }

        private AvlNode<E> rebalanceRightHeavy() {
          requireNonNull(right);
          if (right.balanceFactor() > 0) {
            right = right.rotateRight();
          }
          return rotateLeft();
        }

        private AvlNode<E> rebalanceLeftHeavy() {
          requireNonNull(left);
          if (left.balanceFactor() < 0) {
            left = left.rotateLeft();
          }
          return rotateRight();
        }
}
