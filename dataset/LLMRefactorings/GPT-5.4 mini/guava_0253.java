public class guava_0253 {

        private AvlNode<E> rebalanceRefactored() {
          switch (balanceFactor()) {
            case -2:
              // requireNonNull is safe because right must exist in order to get a negative factor.
              requireNonNull(right);
              if (right.balanceFactor() > 0) {
                right = right.rotateRight();
              }
              return rotateLeft();
            case 2:
              // requireNonNull is safe because left must exist in order to get a positive factor.
              requireNonNull(left);
              if (left.balanceFactor() < 0) {
                left = left.rotateLeft();
              }
              return rotateRight();
            default:
              recomputeHeight();
              return this;
          }
        }
}
