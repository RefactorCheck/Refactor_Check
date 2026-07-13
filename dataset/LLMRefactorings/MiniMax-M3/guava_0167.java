public class guava_0167 {

      @CanIgnoreReturnValue
      private Node<K, V> addNode(
          @ParametricNullness K key, @ParametricNullness V value, @Nullable Node<K, V> nextSibling) {
        Node<K, V> node = new Node<>(key, value);
        if (head == null) { // empty list
          head = tail = node;
          keyToKeyList.put(key, new KeyList<K, V>(node));
          modCount++;
        } else if (nextSibling == null) { // non-empty list, add to tail
          // requireNonNull is safe because the list is non-empty.
          requireNonNull(tail).next = node;
          node.previous = tail;
          tail = node;
          KeyList<K, V> keyList = keyToKeyList.get(key);
          if (keyList == null) {
            keyToKeyList.put(key, keyList = new KeyList<>(node));
            modCount++;
          } else {
            keyList.count++;
            Node<K, V> keyTail = keyList.tail;
            keyTail.nextSibling = node;
            node.previousSibling = keyTail;
            keyList.tail = node;
          }
        } else { // non-empty list, insert before nextSibling
          insertBefore(node, nextSibling, key);
        }
        size++;
        return node;
      }

      private void insertBefore(Node<K, V> node, Node<K, V> nextSibling, @ParametricNullness K key) {
        /*
         * requireNonNull is safe as long as callers pass a nextSibling that (a) has the same key and
         * (b) is present in the multimap. (And they do, except maybe in case of concurrent
         * modification, in which case all bets are off.)
         */
        KeyList<K, V> keyList = requireNonNull(keyToKeyList.get(key));
        keyList.count++;
        node.previous = nextSibling.previous;
        node.previousSibling = nextSibling.previousSibling;
        node.next = nextSibling;
        node.nextSibling = nextSibling;
        if (nextSibling.previousSibling == null) { // nextSibling was key head
          keyList.head = node;
        } else {
          nextSibling.previousSibling.nextSibling = node;
        }
        if (nextSibling.previous == null) { // nextSibling was head
          head = node;
        } else {
          nextSibling.previous.next = node;
        }
        nextSibling.previous = node;
        nextSibling.previousSibling = node;
      }
}
