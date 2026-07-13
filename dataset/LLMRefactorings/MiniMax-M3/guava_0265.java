public class guava_0265 {

    private void insertSplicingIntoIterationOrder(
        Node<K, V> node,
        @Nullable Node<K, V> prevInKeyInsertionOrder,
        @Nullable Node<K, V> nextInKeyInsertionOrder) {
        insertIntoHashBucketsOnly(node);

        insertPrevInKeyInsertionOrder(node, prevInKeyInsertionOrder);
        insertNextInKeyInsertionOrder(node, nextInKeyInsertionOrder);
    }

    private void insertPrevInKeyInsertionOrder(
        Node<K, V> node, @Nullable Node<K, V> prevInKeyInsertionOrder) {
        node.prevInKeyInsertionOrder = prevInKeyInsertionOrder;
        if (prevInKeyInsertionOrder == null) {
            firstInKeyInsertionOrder = node;
        } else {
            prevInKeyInsertionOrder.nextInKeyInsertionOrder = node;
        }
    }

    private void insertNextInKeyInsertionOrder(
        Node<K, V> node, @Nullable Node<K, V> nextInKeyInsertionOrder) {
        node.nextInKeyInsertionOrder = nextInKeyInsertionOrder;
        if (nextInKeyInsertionOrder == null) {
            lastInKeyInsertionOrder = node;
        } else {
            nextInKeyInsertionOrder.prevInKeyInsertionOrder = node;
        }
    }
}
