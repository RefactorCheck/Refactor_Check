public class netty_0070 {

        final Node<V> findLast() {
            outer: for (;;) {
                Node<V> b;
                if ((b = findLastIndexNode()) == null) {
                    break;
                }
                for (;;) {
                    Node<V> n;
                    if ((n = b.next) == null) {
                        if (b.key == noKey) { // empty
                            break outer;
                        } else {
                            return b;
                        }
                    } else if (n.key == noKey) {
                        break;
                    } else if (n.val == null) {
                        unlinkNode(b, n, noKey);
                    } else {
                        b = n;
                    }
                }
            }
            return null;
        }

        private Node<V> findLastIndexNode() {
            Index<V> q;
            acquireFence();
            if ((q = head) == null) {
                return null;
            }
            for (Index<V> r, d;;) {
                while ((r = q.right) != null) {
                    Node<V> p;
                    if ((p = r.node) == null || p.val == null) {
                        RIGHT.compareAndSet(q, r, r.right);
                    } else {
                        q = r;
                    }
                }
                if ((d = q.down) != null) {
                    q = d;
                } else {
                    return q.node;
                }
            }
        }
}
