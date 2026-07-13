private AvlNode<E> root = rootReference.get();



      private @Nullable AvlNode<E> firstNode()  {

        if (root == null) {
          return null;
        }
        AvlNode<E> node;
        if (range.hasLowerBound()) {
          // The cast is safe because of the hasLowerBound check.
          E endpoint = uncheckedCastNullableTToT(range.getLowerEndpoint());
          node = root.ceiling(comparator(), endpoint);
          if (node == null) {
            return null;
          }
          if (range.getLowerBoundType() == BoundType.OPEN
              && comparator().compare(endpoint, node.getElement()) == 0) {
            node = node.succ();
          }
        } else {
          node = header.succ();
        }
        return (node == header || !range.contains(node.getElement())) ? null : node;
      


      }
