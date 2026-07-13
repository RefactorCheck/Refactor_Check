public class guava_0069 {

      private @Nullable AvlNode<E> firstNode() {
        AvlNode<E> root = rootReference.get();
        if (root == null) {
          return null;
        }
        Comparator<? super E> comparator = comparator();
        AvlNode<E> node;
        if (range.hasLowerBound()) {
          E endpoint = uncheckedCastNullableTToT(range.getLowerEndpoint());
          node = root.ceiling(comparator, endpoint);
          if (node == null) {
            return null;
          }
          if (range.getLowerBoundType() == BoundType.OPEN
              && comparator.compare(endpoint, node.getElement()) == 0) {
            node = node.succ();
          }
        } else {
          node = header.succ();
        }
        return (node == header || !range.contains(node.getElement())) ? null : node;
      }
}
